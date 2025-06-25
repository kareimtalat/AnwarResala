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
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import com.kareimt.anwarresala.data.repository.VolunteerRepository
import kotlinx.coroutines.launch

class VolunteerViewModel (private val repository: VolunteerRepository): ViewModel() {
    fun validateRegistrationData(): Boolean {
        return when{
            name.isBlank() -> false
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false
            password.length < 6 -> false
            password != rePassword -> false
            responsibility.isBlank() -> false
            branch.isBlank() -> false
            committee.isBlank() -> false
            else -> true
        }
    }

    fun registerVolunteer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                // Call your repository/API to register the volunteer
                repository.registerVolunteer(
                    VolunteerEntity(
                        name = name,
                        responsibility = responsibility,
                        branch = branch,
                        committee = committee,
                        email = email,
                        password = password,
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                // Handle the error, e.g., show a toast or log it
                onError(e.message ?: "Registration failed")
            }
        }
    }

    // 1. Dropdown Options Data Structures
//    sealed class Responsibility{
//        data class SpecificResponsibility(val name: String): Responsibility()
//        object None: Responsibility()
//    }
//
//    sealed class Branch{
//        data class SpecificBranch(val name: String): Branch()
//        object None: Branch()
//    }
//
//    sealed class Committee{
//        data class SpecificCommittee(val name: String): Committee()
//        object None: Committee()
//    }

    // 2. State Variables: Hold the data for your form fields&dropdown option types.
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

    // TODO: Connect it to the BranchEntity of Room
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

    // 3. Optional: Validation State and Functions: If you need to validate user input.
    var isFormValid by mutableStateOf(false)
        private set

    // Add validation functions (e.g., validateEmail(), validatePassword())
    // and update isFormValid accordingly.
    fun showError(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // 4. Optional: Actions (e.g., Form Submission):
    fun submitForm() {
        // Implement logic to handle form submission (e.g., save data, navigate).
        // You might call a repository or use a use case here.

        // For now, let's just print the data:
        println("Form submitted: Name = $name, Responsibility = $responsibility, ...")

        // Reset the form after submission (if needed):
        resetForm()
    }

    private fun resetForm() {
        name = ""
        responsibility = ""
        branch = ""
        committee = ""
        email = ""
        password = ""
    }




}



class VolunteerViewModelFactory(
    private val repository: VolunteerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolunteerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolunteerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}