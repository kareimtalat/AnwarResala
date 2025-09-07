package com.kareimt.anwarresala.presentation.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.volunteer.VolunteerDao
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ApprovalViewModel (application: Application, private val localVolunteerDao: VolunteerDao): AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()

    val pendingVolunteers = mutableStateOf<List<VolunteerEntity>>(emptyList())
    val approvedVolunteers = mutableStateOf<List<VolunteerEntity>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    var currentUserResponsibility = mutableStateOf("")
    var currentUserBranch = mutableStateOf("")
    var currentUserCommittee = mutableStateOf("")

    // Define user responsibility levels
    private val activityOfficer: String = application.getString(R.string.activity_officer)
    private val leader: String = application.getString(R.string.leader)
    private val head: String = application.getString(R.string.head)
    private val committeeMember: String = application.getString(R.string.committee_member)

    init {
        fetchCurrentUserAndLoadVolunteers()
    }

    fun refreshVolunteers() {
        fetchCurrentUserAndLoadVolunteers()
    }

    private fun fetchCurrentUserAndLoadVolunteers() {
        isLoading.value = true
        viewModelScope.launch (Dispatchers.IO) {
            val currentUser = localVolunteerDao.getVolunteer()
            println("currentUser = ${currentUser.toString()}")

            if (currentUser != null) {
                currentUserResponsibility.value = currentUser.responsibility
                println("currentUserResponsibility = ${currentUserResponsibility.value}")
                currentUserBranch.value = currentUser.branch
                println("currentUserBranch = ${currentUserBranch.value}")
                currentUserCommittee.value = currentUser.committee
                println("currentUserCommittee = ${currentUserCommittee.value}")
                loadVolunteers()
            } else {
                errorMessage.value = "Logged-in user not found in local database"
            }
        }
        isLoading.value = false
    }

    fun loadVolunteers() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                errorMessage.value = null

                println("Current user responsibility: ${currentUserResponsibility.value}")
                println("Comparing with activityOfficer: $activityOfficer")
                println("Comparing with leader: $leader")
                println("Comparing with head: $head")

                // Determine which volunteers to show based on user responsibility
                val query = when (currentUserResponsibility.value) {
                    activityOfficer -> {
                        println("Querying for leaders")
                        db.collection("volunteers").whereEqualTo("responsibility", leader) }
                    leader -> {
                        println("Querying for heads in branch: ${currentUserBranch.value}")
                        db.collection("volunteers").whereEqualTo("responsibility", head).whereEqualTo("branch", currentUserBranch.value)}
                    head -> {
                        println("Querying for heads in branch: ${currentUserBranch.value}")
                        db.collection("volunteers").whereEqualTo("responsibility", committeeMember).whereEqualTo("branch", currentUserBranch.value).whereEqualTo("committee", currentUserCommittee.value)}
                    else -> {
                        println("No match responsibility: ${currentUserResponsibility.value}")
                        pendingVolunteers.value = emptyList()
                        isLoading.value = false
                        return@launch
                    }
                }

                println("responsibilities: $activityOfficer, $leader, $head, $committeeMember")

                println("Executing query...")
                val snapshot = query.get().await()
                println("Query returned ${snapshot.documents.size} documents")

                snapshot.documents.forEach { doc->
                    println("Document: ${doc.id} - ${doc.data}")
                }

                val allVolunteers = snapshot.documents.map { doc ->
                    VolunteerEntity(
                        firebaseId = doc.id,
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: "",
                        responsibility = doc.getString("responsibility") ?: "",
                        branch = doc.getString("branch") ?: "",
                        committee = doc.getString("committee") ?: "",
                        approved = doc.getBoolean("approved") ?: false
                    )
                }

                pendingVolunteers.value = allVolunteers.filter{!it.approved}
                approvedVolunteers.value = allVolunteers.filter { it.approved }

                println("pendingVolunteers = ${pendingVolunteers.value}")
                println("approvedVolunteers = ${approvedVolunteers.value}")
            } catch (e: Exception) {
                errorMessage.value = "Failed to load volunteers: ${e.message}"
            }
        }
    }

    fun approveVolunteer(volunteerId: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                db.collection("volunteers").document(volunteerId).update("approved", true).await()
                loadVolunteers()
            } catch (e: Exception) {
                errorMessage.value = "Failed to approve volunteer: ${e.message}"
            }
        }
    }

    // For deleting the ((pending volunteers))
    fun rejectVolunteer(volunteerId: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                db.collection("volunteers").document(volunteerId).delete().await()
                loadVolunteers()
            } catch (e: Exception) {
                errorMessage.value = "Failed to reject volunteer: ${e.message}"
            }
        }
    }

    // The same of rejectVolunteer function but for the deleting the ((approved volunteers))
    fun deleteVolunteer(volunteerId: String){
        rejectVolunteer(volunteerId)
    }
}

class ApprovalViewModelFactory(
    private val application: Application,
    private val volunteerDao: VolunteerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApprovalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApprovalViewModel(application, volunteerDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}