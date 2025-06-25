package com.kareimt.anwarresala.data.local.volunteer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteer")
data class VolunteerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val responsibility: String,
    val branch: String,
    val committee: String,
    val email: String,
    val password: String,
)
