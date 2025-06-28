package com.kareimt.anwarresala.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import kotlinx.coroutines.tasks.await

class FirebaseVolunteerRepository : VolunteerRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    override suspend fun loginVolunteer(email: String, password: String): Result<VolunteerEntity> =
        try {
            // Authenticate user
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            // Fetch volunteer data from Firestore
            val volunteerDoc = db.collection("volunteers").document(uid).get().await()

            if (volunteerDoc.exists()) {
                val volunteer = VolunteerEntity(
                    name = volunteerDoc.getString("name") ?: "",
                    email = email,
                    password = password, // Note: Consider if you really need to store password
                    responsibility = volunteerDoc.getString("responsibility") ?: "",
                    branch = volunteerDoc.getString("branch") ?: "",
                    committee = volunteerDoc.getString("committee") ?: ""
                )
                Result.success(volunteer)
            } else {
                Result.failure(Exception("Volunteer data not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun registerVolunteer(volunteer: VolunteerEntity): Result<VolunteerEntity> {
        return try {
            // Create authentication account
            val authResult = auth.createUserWithEmailAndPassword(
                volunteer.email,
                volunteer.password
            ).await()

            val volunteerData = mapOf(
                "name" to volunteer.name,
                "email" to volunteer.email,
                "responsibility" to volunteer.responsibility,
                "branch" to volunteer.branch,
                "committee" to volunteer.committee,
                )


            // Store additional volunteer data
            db.collection("volunteers")
                .document(authResult.user?.uid ?: "")
                .set(volunteerData)
                .await()

            Result.success(volunteer)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}