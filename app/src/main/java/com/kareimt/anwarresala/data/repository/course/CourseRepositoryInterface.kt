package com.kareimt.anwarresala.data.repository.course

import com.kareimt.anwarresala.data.local.branch.BranchEntity

interface CourseRepositoryInterface {
    suspend fun addBranchOnFirebase(newBranch: String) : Result<BranchEntity>
    fun getBranches(onResult: (Result<List<BranchEntity>>) -> Unit)
    fun removeBranchesListener()
    fun deleteBranchOnFirebase()
}