package com.kareimt.anwarresala.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import kotlinx.coroutines.tasks.await

class FirebaseVolunteerRepository : VolunteerRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    override suspend fun registerVolunteer(volunteer: VolunteerEntity): Result<String> {
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

            Result.success(authResult.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}