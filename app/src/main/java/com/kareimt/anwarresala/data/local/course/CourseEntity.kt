package com.kareimt.anwarresala.data.local.course

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey var id: String,
    val branch: String,
    @TypeConverters(Converters::class)
    val coursesOfMonth: List<String>,
    val imagePath: String?, // Optional
    val category: String,
    val title: String,
    val type: String, // enum سيتم تعريفه // Use String or Int for enums in Room
    val instructorName: String,
    val instructorBio: String,
    val instructorImagePath: String?, // Optional
    val startDate: String?, // Optional
    val wGLink: String?, // Optional
    val courseDetails: String?, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int = 0, // Optional
    val nextLecture: String?, // Optional
    val organizerName: String?, // Optional
    val organizerWhatsapp: String?, // Optional
    val lastTouch: String,
    val progress: Float = if (totalLectures>0) noOfLiteraturesFinished.toFloat() / totalLectures else 0f // from 0 to 1
) {

}