package com.kareimt.anwarresala.data.remote.repository.course

import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import kotlinx.coroutines.tasks.await

class CourseRepository: CourseRepositoryInterface {
    private val db = Firebase.firestore
    private var branchesListener: ListenerRegistration? = null

    override suspend fun addBranchOnFirebase(newBranch: String): Result<BranchEntity> =
        try{
            val newBranchMap = hashMapOf("branch" to newBranch)

            val docRef = db.collection("branches").document()
            docRef.set(newBranchMap).await()
            val branchId = docRef.id

            println("branch data stored successfully in Firestore for: $newBranch")

            Result.success(BranchEntity(
                branch = newBranch,
                id = branchId
            ))
        } catch (e: Exception) {
            println("Add branch error: ${e.message}")
            Result.failure(e)
        }

    override fun getBranches(onResult: (Result<List<BranchEntity>>) -> Unit) {
        branchesListener?.remove()

        branchesListener = db.collection("branches")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onResult(Result.failure(error ))
                    return@addSnapshotListener
                }

                if (snapshots == null) {
                    onResult(Result.failure(Exception("No data received")))
                    return@addSnapshotListener
                }

                val branches = mutableListOf<BranchEntity>()

                for (document in snapshots.documents) {
                    val branchName  = document.getString("branch") ?: continue
                    branches.add(BranchEntity(id = document.id, branch = branchName))
                }
                
                onResult(Result.success(branches))
            }
    }
    
    override fun removeBranchesListener() {
        branchesListener?.remove()
        branchesListener = null
    }

    override suspend fun deleteBranchOnFirebase(BranchId: String) : String {
        try {
            db.collection("branches").document(BranchId).delete().await()
            println("$BranchId deleted successfully from firestore")
            return ""
        } catch (e: Exception) {
            return "${e.message}"
        }
    }
}