package com.kareimt.anwarresala.data.local


@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val branch: String,
    val imageResId: Int,
    val category: String,
    val title: String,
    val type: String, // enum سيتم تعريفه // Use String or Int for enums in Room
    val instructorName: String,
    val instructorBio: String,
    val instructorImageResId: Int,
    val startDate: String,
    val wGLink: String? = null, // Optional
    val courseDetails: String? = null, // Optional
    val totalLectures: Int,
    val noOfLiteraturesFinished: Int,
    val nextLecture: String? = null, // Optional
    val organizerName: String? = null, // Optional
    val organizerWhatsapp: String? = null // Optional
    val process: Float
)