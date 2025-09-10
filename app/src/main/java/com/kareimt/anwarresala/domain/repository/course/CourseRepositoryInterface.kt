package com.kareimt.anwarresala.domain.repository.course

import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.CourseEntity

interface CourseRepositoryInterface {
    suspend fun addBranchOnFirebase(newBranch: String) : Result<BranchEntity>
    fun getBranches(onResult: (Result<List<BranchEntity>>) -> Unit)
    fun removeBranchesListener()
    suspend fun deleteBranchOnFirebase(branchId: String)  : String
    suspend fun addCourseOnFirebase(course: CourseEntity): Result<String>
    fun getCourses(onResult: (Result<List<CourseEntity>>) -> Unit)
    suspend fun updateCourseOnFirebase(course: CourseEntity): Result<Unit>
    fun removeCoursesListener()
    suspend fun deleteCourse(courseId: String): Result<Unit>
}