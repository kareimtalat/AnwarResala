package com.kareimt.anwarresala.data.local.branch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "branches")
data class BranchEntity (
    @PrimaryKey
    val id: String,
    val branch: String,
    )