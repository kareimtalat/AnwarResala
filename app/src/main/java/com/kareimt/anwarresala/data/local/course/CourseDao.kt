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
    suspend fun getCourseById(id: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()

    @Query("""
    SELECT * FROM courses WHERE 
    title LIKE '%' || :searchQuery || '%' COLLATE NOCASE OR 
    category LIKE '%' || :searchQuery || '%' COLLATE NOCASE OR 
    courseDetails LIKE '%' || :searchQuery || '%' COLLATE NOCASE OR 
    instructorName LIKE '%' || :searchQuery || '%' COLLATE NOCASE OR 
    instructorBio LIKE '%' || :searchQuery || '%' COLLATE NOCASE OR 
    id LIKE '%' || :searchQuery || '%'
""")
    suspend fun searchCourses(searchQuery: String): List<CourseEntity>
}
