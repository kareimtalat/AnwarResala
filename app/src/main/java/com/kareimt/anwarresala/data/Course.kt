package com.kareimt.anwarresala.data

import android.os.Parcelable
import com.kareimt.anwarresala.R
import kotlinx.parcelize.Parcelize



@Parcelize
data class Course(
    val id: Int,
    val branch: String,
    val imagePath: String = "drawable/anwar_resala_logo",
    val category: String,
    val title: String,
    val type: CourseType, // enum سيتم تعريفه
    val instructor: Instructor,
    val startDate: String,
    val wGLink: String? = null, // Optional
    val courseDetails: String?= null, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int,
    val nextLecture: String? = null, // Optional
    val organizer: Organizer? = null, // Optional
    val progress: Float=(noOfLiteraturesFinished.toFloat()/totalLectures), // نسبة التقدم في الكورس
): Parcelable  {
    @Parcelize
    data class Instructor(
        val name: String,
        val bio: String,
        val imagePath: String= "drawable/anwar_resala_logo",
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