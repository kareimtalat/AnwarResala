package com.kareimt.anwarresala.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kareimt.anwarresala.R

class VolunteerViewModel : ViewModel() {

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



class VolunteerViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolunteerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolunteerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}