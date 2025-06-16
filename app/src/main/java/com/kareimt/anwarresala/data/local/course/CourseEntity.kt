package com.kareimt.anwarresala.data.local.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val branch: String,
    val coursesOfMonth: List<String>,
    val imagePath: String, // Optional
    val category: String,
    val title: String,
    val type: String, // enum سيتم تعريفه // Use String or Int for enums in Room
    val instructorName: String,
    val instructorBio: String,
    val instructorImagePath: String, // Optional
    val startDate: String, // Optional
    val wGLink: String, // Optional
    val courseDetails: String, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int, // Optional
    val nextLecture: String, // Optional
    val organizerName: String, // Optional
    val organizerWhatsapp: String, // Optional
    val progress: Float =((noOfLiteraturesFinished.toFloat())/totalLectures) // from 0 to 1
)