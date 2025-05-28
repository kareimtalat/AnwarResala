package com.kareimt.anwarresala.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: Complete the BranchEntity Room files
@Entity(tableName = "branches")
data class BranchEntity (
        @PrimaryKey val id: Int,
        val branch: String,
    )