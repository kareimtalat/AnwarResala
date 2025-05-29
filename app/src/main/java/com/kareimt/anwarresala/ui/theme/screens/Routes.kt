package com.kareimt.anwarresala.ui.theme.screens

object Routes{
    const val Main = "main"
    const val Home = "home"
    const val Beneficiary = "beneficiary"
    const val Registration = "registration"
    const val CourseDetails = "courseDetails/{courseId}"
    const val AddEditCourse = "addEditCourse?courseId={courseId}"

    // Helper functions for routes with parameters
    fun courseDetails(courseId: Int) = "courseDetails/$courseId"
    fun addEditCourse(courseId: Int? = null) = "addEditCourse?courseId=$courseId"
}