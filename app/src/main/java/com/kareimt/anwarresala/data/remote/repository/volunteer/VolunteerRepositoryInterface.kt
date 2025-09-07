package com.kareimt.anwarresala.data.remote.repository.volunteer

import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity

interface VolunteerRepositoryInterface {
    suspend fun registerVolunteer(volunteer: VolunteerEntity,password: String) : Result<VolunteerEntity>
    suspend fun loginVolunteer(email: String, password: String): Result<VolunteerEntity>
    fun signOutVolunteer()
    suspend fun checkApproval(email: String): Result<Boolean>
    suspend fun fetchCurrentVolunteerData(): Result<VolunteerEntity>
    suspend fun setIsFirebaseQuotaExceeded () : Result<Boolean>

//    suspend fun fetchCurrentVolunteerData(): Result<VolunteerEntity>

}