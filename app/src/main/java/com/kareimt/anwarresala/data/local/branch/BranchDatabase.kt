package com.kareimt.anwarresala.data.local.branch

import androidx.room.Database

@Database (entities = [BranchEntity::class], version = 2, exportSchema = false)
abstract class BranchDatabase : androidx.room.RoomDatabase() {
    abstract fun branchDao(): BranchDao

    companion object {
        const val DATABASE_NAME = "branch_database"
    }
}