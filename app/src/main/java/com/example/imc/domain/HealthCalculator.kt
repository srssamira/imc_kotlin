package com.example.imc.domain

import kotlin.math.log10
import kotlin.math.pow

object HealthCalculator {

    // Requisito Base: IMC
    fun calculateIMC(weight: Double, heightCm: Double): Double {
        val heightM = heightCm / 100
        return if (heightM > 0) weight / (heightM * heightM) else 0.0
    }

    fun getImcClassification(imc: Double): String {
        return when {
            imc < 18.5 -> "Abaixo do peso"
            imc < 25.0 -> "Peso normal"
            imc < 30.0 -> "Sobrepeso"
            imc < 35.0 -> "Obesidade Grau I"
            imc < 40.0 -> "Obesidade Grau II"
            else -> "Obesidade Grau III"
        }
    }

    // Extra 1: TMB (Mifflin-St Jeor)
    fun calculateTMB(weight: Double, heightCm: Double, age: Int, isMale: Boolean): Double {
        return if (isMale) {
            (10 * weight) + (6.25 * heightCm) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * heightCm) - (5 * age) - 161
        }
    }

    // Extra 2: Necessidade Calórica (TMB * Fator Atividade)
    fun calculateCaloricNeed(tmb: Double, activityLevel: ActivityLevel): Double {
        return tmb * activityLevel.factor
    }

    // Extra 3: Estimativa de Gordura Corporal (Fórmula IMC simples para adultos)
    // Nota: Fórmulas precisas precisam de circunferência, esta é uma estimativa baseada em IMC aceita para triagem
    fun calculateBodyFat(imc: Double, age: Int, isMale: Boolean): Double {
        val genderFactor = if (isMale) 1 else 0
        return (1.20 * imc) + (0.23 * age) - (10.8 * genderFactor) - 5.4
    }
}

enum class ActivityLevel(val factor: Double, val label: String) {
    SEDENTARY(1.2, "Sedentário"),
    LIGHT(1.375, "Levemente ativo"),
    MODERATE(1.55, "Moderadamente ativo"),
    INTENSE(1.725, "Muito ativo")
}