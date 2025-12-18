package com.example.imc.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {
    @Query("SELECT * FROM health_records ORDER BY date DESC")
    fun getAll(): Flow<List<HealthRecord>>

    @Insert
    suspend fun insert(record: HealthRecord)

    @Delete
    suspend fun delete(record: HealthRecord)
}