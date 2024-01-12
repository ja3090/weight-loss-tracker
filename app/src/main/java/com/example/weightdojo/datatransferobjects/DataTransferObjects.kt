package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.Day
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

data class DayData(
    val day: Day,
    val meals: List<MealData>?
)

data class MealData(
    val totalCalories: Float,
    val mealId: Long,
    val mealName: String,
    val dayId: Long
)

data class CalorieEntryIngredients(
    val totalCalories: Float,
    val caloriesId: Long,
    val name: String,
)

data class CalorieEntryForEditing(
    val calorieId: Long,
    val name: String,
    val caloriesPer100: Float,
    val grams: Float
)


enum class Marked { DELETE, EDIT }

data class IngredientState(
    var markedFor: Marked = Marked.EDIT,
    val calorieId: Long,
    val name: String,
    val caloriesPer100: Float,
    val grams: Float
)