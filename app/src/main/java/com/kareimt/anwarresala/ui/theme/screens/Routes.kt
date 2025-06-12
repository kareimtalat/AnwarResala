package com.kareimt.anwarresala.ui.theme.screens

object Routes{
    const val Main = "main"
    const val CoursesScreen = "courses_screen"
    const val Beneficiary = "beneficiary"
    const val Registration = "registration"
    const val CourseDetails = "courseDetails/{courseId}"
    const val AddEditCourse = "addEditCourse/{courseId}"
    const val ForgetPassword = "forget_password_screen"
    const val LoginScreen = "login_screen"
    const val ChooseBranch = "choose_branch"

    // Helper functions for routes with parameters
    fun courseDetails(courseId: Int) = "courseDetails/$courseId"
    fun addEditCourse(courseId: Int) = "addEditCourse/$courseId"
}