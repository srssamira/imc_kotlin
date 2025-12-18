package com.example.imc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.imc.data.AppDatabase
import com.example.imc.data.HealthRecord
import com.example.imc.domain.ActivityLevel
import com.example.imc.domain.HealthCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).healthDao()

    // Estado da Lista (Histórico)
    val history = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Função para calcular e salvar
    fun calculateAndSave(
        weightStr: String,
        heightStr: String,
        ageStr: String,
        isMale: Boolean,
        activity: ActivityLevel
    ): Result<Unit> {
        val weight = weightStr.toDoubleOrNull()
        val height = heightStr.toDoubleOrNull()
        val age = ageStr.toIntOrNull()

        if (weight == null || height == null || age == null || height == 0.0) {
            return Result.failure(Exception("Dados inválidos"))
        }

        val imc = HealthCalculator.calculateIMC(weight, height)
        val tmb = HealthCalculator.calculateTMB(weight, height, age, isMale)
        val fat = HealthCalculator.calculateBodyFat(imc, age, isMale)
        val calories = HealthCalculator.calculateCaloricNeed(tmb, activity)
        val classification = HealthCalculator.getImcClassification(imc)

        val record = HealthRecord(
            date = System.currentTimeMillis(),
            weight = weight,
            height = height,
            imc = imc,
            imcClassification = classification,
            tmb = tmb,
            bodyFat = fat,
            caloricNeed = calories
        )

        viewModelScope.launch {
            dao.insert(record)
        }
        return Result.success(Unit)
    }

    fun deleteRecord(record: HealthRecord) {
        viewModelScope.launch {
            dao.delete(record)
        }
    }
}