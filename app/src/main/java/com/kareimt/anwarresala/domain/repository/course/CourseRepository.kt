package com.kareimt.anwarresala.domain.repository.course

import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.kareimt.anwarresala.data.local.branch.BranchEntity
import com.kareimt.anwarresala.data.local.course.CourseEntity
import kotlinx.coroutines.tasks.await

class CourseRepository: CourseRepositoryInterface {
    private val db = Firebase.firestore
    private var branchesListener: ListenerRegistration? = null
    private var coursesListener: ListenerRegistration? = null

    // Branch operations
    override suspend fun addBranchOnFirebase(newBranch: String): Result<BranchEntity> =
        try{
            val newBranchMap = hashMapOf("branch" to newBranch)

            val docRef = db.collection("branches").document()
            docRef.set(newBranchMap).await()
            val branchId = docRef.id

            println("branch data stored successfully in Firestore for: $newBranch")

            Result.success(
                BranchEntity(
                    branch = newBranch,
                    id = branchId
                )
            )
        } catch (e: Exception) {
            println("Add branch error: ${e.message}")
            Result.failure(e)
        }

    override fun getBranches(onResult: (Result<List<BranchEntity>>) -> Unit) {
        branchesListener?.remove()

        branchesListener = db.collection("branches")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onResult(Result.failure(error ))
                    return@addSnapshotListener
                }

                if (snapshots == null) {
                    onResult(Result.failure(Exception("No data received")))
                    return@addSnapshotListener
                }

                val branches = mutableListOf<BranchEntity>()

                for (document in snapshots.documents) {
                    val branchName  = document.getString("branch") ?: continue
                    branches.add(BranchEntity(id = document.id, branch = branchName))
                }

                onResult(Result.success(branches))
            }
    }

    override fun removeBranchesListener() {
        branchesListener?.remove()
        branchesListener = null
    }

    override suspend fun deleteBranchOnFirebase(branchId: String) : String {
        try {
            db.collection("branches").document(branchId).delete().await()
            println("$branchId deleted successfully from firestore")
            return ""
        } catch (e: Exception) {
            return "${e.message}"
        }
    }

    // Course operations
    override suspend fun addCourseOnFirebase(course: CourseEntity): Result<String> =
        try {
            val docRef = db.collection("courses").document()
            course.id = docRef.id

            val courseData = hashMapOf(
                "id" to course.id,
                "branch" to course.branch,
                "coursesOfMonth" to course.coursesOfMonth,
                "imagePath" to course.imagePath,
                "category" to course.category,
                "title" to course.title,
                "type" to course.type,
                "instructorName" to course.instructorName,
                "instructorBio" to course.instructorBio,
                "instructorImagePath" to course.instructorImagePath,
                "startDate" to course.startDate,
                "wGLink" to course.wGLink,
                "courseDetails" to course.courseDetails,
                "totalLectures" to course.totalLectures,
                "noOfLiteraturesFinished" to course.noOfLiteraturesFinished,
                "nextLecture" to course.nextLecture,
                "organizerName" to course.organizerName,
                "organizerWhatsapp" to course.organizerWhatsapp,
                "lastTouch" to course.lastTouch
            )

            docRef.set(courseData).await()

            println("Course data stored successfully in Firestore with ID: ${course.id}")
            Result.success(course.id)
        } catch (e: Exception) {
            println("Add course error: ${e.message}")
            Result.failure(e)
        }

    override suspend fun updateCourseOnFirebase(course: CourseEntity): Result<Unit> =
        try {
            val courseData = hashMapOf(
                "branch" to course.branch,
                "coursesOfMonth" to course.coursesOfMonth,
                "imagePath" to course.imagePath,
                "category" to course.category,
                "title" to course.title,
                "type" to course.type,
                "instructorName" to course.instructorName,
                "instructorBio" to course.instructorBio,
                "instructorImagePath" to course.instructorImagePath,
                "startDate" to course.startDate,
                "wGLink" to course.wGLink,
                "courseDetails" to course.courseDetails,
                "totalLectures" to course.totalLectures,
                "noOfLiteraturesFinished" to course.noOfLiteraturesFinished,
                "nextLecture" to course.nextLecture,
                "organizerName" to course.organizerName,
                "organizerWhatsapp" to course.organizerWhatsapp,
                "lastTouch" to course.lastTouch
            )

            db.collection("courses").document(course.id)
                .set(courseData).await()

            println("Course updated successfully in FireStore with ID: ${course.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Update course error: ${e.message}")
            Result.failure(e)
        }

    override fun getCourses(onResult: (Result<List<CourseEntity>>) -> Unit) {
        coursesListener?.remove()

        coursesListener = db.collection("courses")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onResult(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshots == null) {
                    onResult(Result.failure(Exception("No data received")))
                    return@addSnapshotListener
                }

                val courses = mutableListOf<CourseEntity>()
                for (document in snapshots.documents) {
                    try {
                        val coursesOfMonthList = when (val field = document.get("coursesOfMonth")) {
                            is List<*> -> field.filterIsInstance<String>()
                            else -> emptyList()
                        }
                        val course = CourseEntity(
                            id = document.id,
                            branch = document.getString("branch") ?: "",
                            coursesOfMonth = coursesOfMonthList,
                            imagePath = document.getString("imagePath") ?: "",
                            category = document.getString("category") ?: "",
                            title = document.getString("title") ?: "",
                            type = document.getString("type") ?: "",
                            instructorName = document.getString("instructorName") ?: "",
                            instructorBio = document.getString("instructorBio") ?: "",
                            instructorImagePath = document.getString("instructorImagePath") ?: "",
                            startDate = document.getString("startDate") ?: "",
                            wGLink = document.getString("wGLink") ?: "",
                            courseDetails = document.getString("courseDetails") ?: "",
                            totalLectures = (document.get("totalLectures") as? Long)?.toInt() ?: 0,
                            noOfLiteraturesFinished = (document.get("noOfLiteraturesFinished") as? Long)?.toInt() ?: 0,
                            nextLecture = document.getString("nextLecture") ?: "",
                            organizerName = document.getString("organizerName") ?: "",
                            organizerWhatsapp = document.getString("organizerWhatsapp") ?: "",
                            lastTouch = document.getString("lastTouch") ?: ""
                        )
                        courses.add(course)
                    } catch (e: Exception) {
                        println("Error parsing course document: ${e.message}")
                    }
                }
                onResult(Result.success(courses))
            }
    }

    override suspend fun deleteCourse(courseId: String) : Result<Unit> {
        return try {
            db.collection("courses").document(courseId).delete().await()
            println("$courseId deleted successfully from firestore")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Delete course error: ${e.message}")
            Result.failure(e)
        }
    }

    override fun removeCoursesListener() {
        coursesListener?.remove()
        coursesListener = null
    }
}