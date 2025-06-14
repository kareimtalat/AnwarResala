package com.kareimt.anwarresala.data.local.branch

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: Complete the BranchEntity Room files
@Entity(tableName = "branches")
data class BranchEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val branch: String,
    )