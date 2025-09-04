package com.kareimt.anwarresala.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import com.kareimt.anwarresala.data.repository.VolunteerRepository
import kotlinx.coroutines.launch

class VolunteerViewModel (private val repository: VolunteerRepository,
    private val localVolunteerDao: com.kareimt.anwarresala.data.local.volunteer.VolunteerDao
    ): ViewModel() {
    // 1. Initialization: Set up the ViewModel with any necessary dependencies.
    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        isLoggedIn = Firebase.auth.currentUser != null
        Firebase.auth.addAuthStateListener { auth ->
        isLoggedIn = auth.currentUser != null}
        // You can initialize any data or state here if needed.
        // For example, you might want to load initial data from the repository.
    }

    // 2. State Variables: Hold the data for your form fields&dropdown option types.
    // TODO: use isLoading and onError on the UI
    var isLoading = false
    var onError by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set // Use private setters to control updates from the UI.

    var responsibility by mutableStateOf<String>("")
        private set

    var branch by mutableStateOf<String>("")
        private set

    var committee by mutableStateOf<String>("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var rePassword by mutableStateOf("")
        private set

    var activityCode by mutableStateOf("")
        private set

    var volunteerCode by mutableStateOf("")
        private set

    var currentVolunteer by mutableStateOf<VolunteerEntity?>( null)
        private set


    // 3. Event Handling Functions: Respond to user input (e.g., typing in a text field, selecting from a dropdown).
    fun onNameChanged(newName: String) {
        name = newName
    }

    fun onResponsibilityChanged(newResponsibility: String) {
        responsibility = newResponsibility
    }

    fun onBranchChanged(newBranch: String) {
        branch = newBranch
    }

    fun onCommitteeChanged(newCommittee: String) {
        committee = newCommittee
    }

    fun onEmailChanged(newEmail: String) {
        email = newEmail.takeWhile { char -> char != '\n' }
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword.takeWhile { char -> char != '\n' }
    }

    fun onRePasswordChanged(newRePassword: String) {
        rePassword = newRePassword.takeWhile { char -> char != '\n' }
    }

    fun onActivityCodeChanged(newActivityCode: String) {
        activityCode = newActivityCode.takeWhile { char -> char != '\n' }
    }

    fun onVolunteerCodeChanged(newVolunteerCode: String) {
        volunteerCode = newVolunteerCode.takeWhile { char -> char != '\n' }
    }

    fun getCurrentVolunteer() {
        viewModelScope.launch {
            try {
                isLoading = true
                currentVolunteer = localVolunteerDao.getVolunteer()
            } catch (e: Exception) {
                onError =
                    "Failed to fetch the current volunteer data from the local database: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun emptyOnError(){
        onError = ""
    }

    //The list of options at the menus
    @Composable
    fun getResponsibilityOptions(): List<String> {
        return listOf(
            stringResource(R.string.committee_member),
            stringResource(R.string.head),
            stringResource(R.string.leader),
            stringResource(R.string.activity_officer),
        )
    }

    // TODO: Connect it to the BranchEntity of Room, but remember add to it (The centeral)
    @Composable
    fun getBranchOptions(): List<String> {
        return listOf(
            stringResource(R.string.central),
            stringResource(R.string.tenth_of_ramadan),
            stringResource(R.string.helwan),
            stringResource(R.string.maadi),
            stringResource(R.string.faisal),
            stringResource(R.string.nasr_city),
            stringResource(R.string.other),
        )
    }
    @Composable
    fun getCommitteeOptions(): List<String> {
        return listOf(
            stringResource(R.string.br),
            stringResource(R.string.social),
            stringResource(R.string.organizing),
            stringResource(R.string.hr),
            stringResource(R.string.other),
        )
    }

    // Add validation functions (e.g., validateEmail(), validatePassword())
    // and update isFormValid accordingly.
    fun showError(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Error states
    private var _isNameError = mutableStateOf(false)
    val isNameError: Boolean get() = _isNameError.value

    private var _isResponsibilityError = mutableStateOf(false)
    val isResponsibilityError: Boolean get() = _isResponsibilityError.value

    private var _isBranchError = mutableStateOf(false)
    val isBranchError: Boolean get() = _isBranchError.value

    private var _isCommitteeError = mutableStateOf(false)
    val isCommitteeError: Boolean get() = _isCommitteeError.value

    private var _isEmailError = mutableStateOf(false)
    val isEmailError: Boolean get() = _isEmailError.value

    private var _isPasswordError = mutableStateOf(false)
    val isPasswordError: Boolean get() = _isPasswordError.value

    private var _isRePasswordError = mutableStateOf(false)
    val isRePasswordError: Boolean get() = _isRePasswordError.value

    // Validation function for each field
    private fun validateName(): Boolean {
        _isNameError.value = name.trim().length < 3
        return !_isNameError.value
    }

    private fun validateResponsibility(): Boolean {
        _isResponsibilityError.value = responsibility.trim().isEmpty()
        return !_isResponsibilityError.value
    }

    private fun validateBranch(): Boolean {
        _isBranchError.value = branch.trim().isEmpty()
        return !_isBranchError.value
    }

    private fun validateCommittee(): Boolean {
        _isCommitteeError.value = committee.trim().isEmpty()
        return !_isCommitteeError.value
    }

    private fun validateEmail(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        _isEmailError.value = !email.trim().matches(emailPattern.toRegex())
        return !_isEmailError.value
    }

    private fun validatePassword(): Boolean {
        _isPasswordError.value = password.length < 6
        return !_isPasswordError.value
    }

    private fun validateRePassword(): Boolean {
        _isRePasswordError.value = password != rePassword
        return !_isRePasswordError.value
    }

    private fun resetForm() {
        // Reset all form fields
        name = ""
        responsibility = ""
        branch = ""
        committee = ""
        email = ""
        password = ""
        rePassword = ""

        resetErrors()
    }

    fun validateRegistrationData(): Boolean {
        return validateName() &&
                validateResponsibility() &&
                validateBranch() &&
                validateCommittee() &&
                validateEmail() &&
                validatePassword() &&
                validateRePassword()
    }

    fun validateLoginData(): Boolean {
        return validateEmail() &&
                validatePassword()
    }

    // Reset error states
    private fun resetErrors() {
        _isNameError.value = false
        _isResponsibilityError.value = false
        _isBranchError.value = false
        _isCommitteeError.value = false
        _isEmailError.value = false
        _isPasswordError.value = false
        _isRePasswordError.value = false
    }

    // 5. Optional: Actions (e.g., Form Submission):
    fun registerVolunteer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
            val volunteer = VolunteerEntity(
                name = name,
                responsibility = responsibility,
                branch = branch,
                committee = committee,
                email = email
            )
            println("Registering Volunteer: $volunteer")

            val result = repository.registerVolunteer(volunteer, password)
            println("Registration attempt completed. and about to go on fold(success or failure)")

            result.fold(
                onSuccess = { registeredVolunteer ->
                    println("Registration Result: $registeredVolunteer")
                    resetForm()
                    localVolunteerDao.replace(registeredVolunteer)
                    // Prevent auto-login
                    logout()
                    onSuccess()
                },
                onFailure = {error ->
                    println("Registration failed: ${error.message}")
                    onError(error.message ?: "Registration failed")
                }
            )
            } catch (e: Exception) {
                println("Unexpected error during registration: ${e.message}")
                onError("An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun loginVolunteer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                onError("Email and password cannot be empty")
                return@launch
            }

            println("email: $email")
            val approved = repository.checkApproval(email)
            println("approved: $approved")

            try {
                if (approved.getOrNull() != null) {
                    if (approved.getOrNull()!!) {
                        val result = repository.loginVolunteer(email, password)
                        result.fold(
                            onSuccess = { registeredVolunteer ->
                                // Handle successful login, e.g., navigate to the next screen
                                localVolunteerDao.replace(registeredVolunteer)
                                currentVolunteer = registeredVolunteer
                                println("From login fun: ${currentVolunteer.toString()}")
                                isLoggedIn = true
                                onSuccess()
                            },
                            onFailure = { error ->
                                // Handle login failure, e.g., show an error message
                                onError(error.message ?: "Login failed")
                            }
                        )
                    } else {
                        onError("Your account hadn't approved yet.")
                    }
                } else {
                    onError("Your account not found. Check e-mail typo or it might had been rejected or deleted.")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message}")
            }
        }
    }

    fun storeCurrentVolunteerLocally() {
        viewModelScope.launch {
            try {
                val result = repository.fetchCurrentVolunteerData()
                        result.fold(
                            onSuccess = { currentVolunteerData ->
                                localVolunteerDao.replace(currentVolunteerData)
                                currentVolunteer = currentVolunteerData
                                println("From storeCurrentVolunteerLocally fun: ${currentVolunteer.toString()}")
                            },
                            onFailure = { error ->
                                onError = "Fetching Current Volunteer Data failed: ${error.message}"
                            }
                        )
            } catch (e: Exception) {
                onError="Exception: ${e.message}"
            }
        }
    }

    fun logout() {
        repository.signOutVolunteer()
        isLoggedIn = false
    }

    fun deleteAccount() {
        // TODO: Handel it's functionality
    }
}



class VolunteerViewModelFactory(
    private val repository: VolunteerRepository,
    private val localVolunteerDao: com.kareimt.anwarresala.data.local.volunteer.VolunteerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolunteerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolunteerViewModel(repository,localVolunteerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}