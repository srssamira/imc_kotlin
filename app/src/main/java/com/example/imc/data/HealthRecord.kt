package com.example.imc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val weight: Double,
    val height: Double,
    val imc: Double,
    val imcClassification: String,
    val tmb: Double,          // Taxa Metabólica Basal
    val bodyFat: Double,      // Estimativa de gordura
    val caloricNeed: Double   // Necessidade calórica
)