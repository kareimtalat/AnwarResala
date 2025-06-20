package com.kareimt.anwarresala.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kareimt.anwarresala.R

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val branch: String,
    val imagePath: String? = "drawable/anwar_resala_logo",
    val category: String,
    val title: String,
    val type: String, // enum سيتم تعريفه // Use String or Int for enums in Room
    val instructorName: String,
    val instructorBio: String,
    val instructorImagePath: String? = "drawable/anwar_resala_logo",
    val startDate: String,
    val wGLink: String? = null, // Optional
    val courseDetails: String? = null, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int,
    val nextLecture: String? = null, // Optional
    val organizerName: String? = null, // Optional
    val organizerWhatsapp: String? = null, // Optional
    val progress: Float =(noOfLiteraturesFinished.toFloat()/totalLectures) // from 0 to 1
)