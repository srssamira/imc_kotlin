package com.example.imc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HealthRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "health_db").build()
                    .also { INSTANCE = it }
            }
        }
    }
}