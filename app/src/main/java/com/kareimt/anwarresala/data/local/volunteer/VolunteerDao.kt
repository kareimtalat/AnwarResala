package com.kareimt.anwarresala.data.local.volunteer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VolunteerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(volunteer: VolunteerEntity)

    @Update
    suspend fun update(volunteer: VolunteerEntity)

    @Delete
    suspend fun delete(volunteer: VolunteerEntity)

//    @Query("SELECT * FROM volunteer")
//    fun getAllVolunteers(): Flow<List<VolunteerEntity>>
//
//    @Query("SELECT * FROM volunteer WHERE id = :id")
//    suspend fun getVolunteerById(id: String): VolunteerEntity?
//
//    @Query("SELECT * FROM volunteer WHERE email = :email")
//    suspend fun getVolunteerByEmail(email: String): VolunteerEntity?
}