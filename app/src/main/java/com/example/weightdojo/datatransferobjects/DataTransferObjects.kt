package com.example.weightdojo.datatransferobjects

import java.time.LocalDate

data class EarliestDate(
    val date: LocalDate?
)

data class ChartData(
    val max: Float?,
    val min: Float?,
    val date: LocalDate,
    val value: Float?,
)

data class NutritionTotals(
    val totalCalories: Float?,
    val totalCarbohydrates: Float?,
    val totalProtein: Float?,
    val totalFat: Float?
)

data class NutritionTotalsIngredients(
    val totalCalories: Float,
    val totalCarbohydrates: Float?,
    val totalProtein: Float?,
    val totalFat: Float?,
    val dayId: Long
)