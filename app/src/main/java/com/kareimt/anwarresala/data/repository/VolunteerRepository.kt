package com.kareimt.anwarresala.data.repository

import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity

interface VolunteerRepository {
    suspend fun registerVolunteer(volunteer: VolunteerEntity): Result<String>
}