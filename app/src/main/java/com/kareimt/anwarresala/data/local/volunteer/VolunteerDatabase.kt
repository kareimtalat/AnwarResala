package com.kareimt.anwarresala.data.local.volunteer

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VolunteerEntity::class], version = 7, exportSchema = false)
abstract class VolunteerDatabase : RoomDatabase() {
    abstract fun volunteerDao(): VolunteerDao
}