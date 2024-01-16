package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
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
    val totalCalories: Float?,
    val mealId: Long,
    val mealName: String,
    val dayId: Long
)

enum class Marked { DELETE, EDIT }

data class IngredientState(
    var markedFor: Marked = Marked.EDIT,
    val ingredientId: Long,
    val name: String,
    val caloriesPer100: Float,
    val grams: Float
)

data class Totals(
    var totalCalories: Float,
    var totalFat: Float,
    var totalProtein: Float,
    var totalCarbs: Float
)

data class MealWithIngredients(
    val meal: Meal,
    val ingredient: List<Ingredient>
)