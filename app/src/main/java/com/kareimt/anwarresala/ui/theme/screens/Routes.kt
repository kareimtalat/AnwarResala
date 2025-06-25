package com.kareimt.anwarresala.ui.theme.screens

import com.kareimt.anwarresala.data.CourseType
import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType
import com.kareimt.anwarresala.viewmodels.VolunteerViewModel

object Routes{
    const val Main = "main"
    const val CoursesScreen = "courses_screen/{screenType}/{branch}"
    const val Beneficiary = "beneficiary"
    const val Registration = "registration"
    const val CourseDetails = "course_details/{courseId}"
    const val AddEditCourse = "add_edit_course/{courseId}"
    const val ForgetPassword = "forget_password_screen"
    const val LoginScreen = "login_screen"
    const val ChooseBranch = "choose_branch"
    const val VolunteerCode = "volunteer_code"

    // Helper functions for routes with parameters
    fun courseDetails(courseId: Int) = "course_details/$courseId"
    fun addEditCourse(courseId: Int) = "add_edit_course/$courseId"
    fun coursesScreen(screenType: ScreenType, branch: String="") = "courses_screen/${screenType.name}/$branch"
}