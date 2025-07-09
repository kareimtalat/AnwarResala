package com.kareimt.anwarresala.data.local.volunteer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteer")
data class VolunteerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // For Room's local ID
    val firebaseId: String? = null, // To store the Firebase User UID
    val name: String,
    val responsibility: String,
    val branch: String,
    val committee: String,
    val email: String,
)