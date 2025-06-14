package com.kareimt.anwarresala.data.local.branch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kareimt.anwarresala.data.local.course.CourseEntity

@Dao
interface BranchDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertBranch(branch: BranchEntity)

     @Query("SELECT * FROM branches")
     suspend fun getAllBranches(): List<BranchEntity>

     @Delete
     suspend fun deleteBranch(branch: BranchEntity)

    @Query("SELECT * FROM branches WHERE id = :id")
    suspend fun getBranchById(id: Int): BranchEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBranches(branches: List<BranchEntity>)

    @Update
    suspend fun updateBranch(branch: BranchEntity)

    @Query("SELECT * FROM branches WHERE branch LIKE :searchQuery ")
    suspend fun searchBranches(searchQuery: String): List<BranchEntity>
}