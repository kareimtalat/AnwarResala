package com.kareimt.anwarresala.data.local.course

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CourseEntity::class], version = 9, exportSchema = false)
@TypeConverters(CourseTypeConverters::class)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}