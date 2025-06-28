package com.kareimt.anwarresala.ui.theme.screens

import com.kareimt.anwarresala.ui.theme.screens.courses_screens.ScreenType

object Routes{
    const val MAIN = "main"
    const val COURSES_SCREEN = "courses_screen/{screenType}/{branch}"
    const val BENEFICIARY = "beneficiary"
    const val REGISTRATION = "registration"
    const val COURSE_DETAILS = "course_details/{courseId}"
    const val ADD_EDIT_COURSE = "add_edit_course/{courseId}"
    const val FORGET_PASSWORD = "forget_password_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val CHOOSE_BRANCH = "choose_branch"
    const val VOLUNTEER_CODE = "volunteer_code"

    // Helper functions for routes with parameters
    fun courseDetails(courseId: Int) = "course_details/$courseId"
    fun addEditCourse(courseId: Int) = "add_edit_course/$courseId"
    fun coursesScreen(screenType: ScreenType, branch: String="") = "courses_screen/${screenType.name}/$branch"
}