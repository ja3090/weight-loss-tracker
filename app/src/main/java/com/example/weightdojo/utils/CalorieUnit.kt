package com.example.weightdojo.utils

import com.example.weightdojo.AppConfig

class CalorieUnit {
    companion object {
        fun convert(
            from: CalorieUnits,
            to: CalorieUnits = AppConfig.internalDefaultCalorieUnit,
            value: Float
        ): Float {
            return when (from) {
                CalorieUnits.KCAL -> when (to) {
                    CalorieUnits.KJ -> value * 4.184F
                    CalorieUnits.KCAL -> value
                }

                CalorieUnits.KJ -> when (to) {
                    CalorieUnits.KCAL -> value * 0.453592F
                    CalorieUnits.KJ -> value
                }
            }
        }
    }
}

enum class CalorieUnits {
    KCAL, KJ
}