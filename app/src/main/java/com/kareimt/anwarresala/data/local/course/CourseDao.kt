package com.kareimt.anwarresala.data.local.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<CourseEntity>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: Int): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCourse(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Query("SELECT * FROM courses WHERE " +
            "title LIKE :searchQuery " +
            "OR category LIKE :searchQuery " +
            "OR courseDetails LIKE :searchQuery " +
            "OR instructorName LIKE :searchQuery " +
            "OR instructorBio LIKE :searchQuery " +
            "OR id LIKE :searchQuery")
    suspend fun searchCourses(searchQuery: String): List<CourseEntity>
}
