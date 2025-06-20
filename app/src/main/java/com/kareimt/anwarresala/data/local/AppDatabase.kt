package com.kareimt.anwarresala.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}