package com.kareimt.anwarresala.data.local.course

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kareimt.anwarresala.data.local.volunteer.VolunteerDao

@Database(entities = [CourseEntity::class], version = 10, exportSchema = false)
@TypeConverters(CourseTypeConverters::class)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}