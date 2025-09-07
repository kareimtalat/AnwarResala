package com.kareimt.anwarresala.data.local.course

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Course(
    val id: Int,
    val branch: String,
    val coursesOfMonth: List<String>,
    val imagePath: String,
    val category: String,
    val title: String,
    val type: CourseType, // enum سيتم تعريفه
    val instructor: Instructor,
    val startDate: String,
    val wGLink: String, // Optional
    val courseDetails: String, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int,
    val nextLecture: String, // Optional
    val organizer: Organizer, // Optional
    val progress: Float=(noOfLiteraturesFinished.toFloat()/totalLectures), // نسبة التقدم في الكورس
): Parcelable  {
    @Parcelize
    data class Instructor(
        val name: String,
        val bio: String,
        val imagePath: String,
    ): Parcelable
    @Parcelize
    data class Organizer(
        val name: String,
        val whatsapp: String
    ) : Parcelable
}

enum class CourseType {
    ONLINE, OFFLINE, HYBRID
}