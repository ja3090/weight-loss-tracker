package com.example.weightdojo

import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.WeightUnits

const val PASSCODE_LENGTH = 4
const val DEPRECATED_MESSAGE = "Used for testing purposes. Do not use in production"
sealed class AppConfig {
    companion object {
        const val seedDatabase = false
        // Default units data will be saved as into the database
        val internalDefaultWeightUnit = WeightUnits.KG
        val internalDefaultCalorieUnit = CalorieUnits.KCAL
    }
}

enum class TestTags {
    DELETE_BUTTON
}