package com.example.weightdojo.datatransferobjects

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
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
    val ingredientId: Long = 0,
    val name: String = "",
    val caloriesPer100: Float = 0f,
    val grams: Float = 0f,
    val proteinPer100: Float = 0f,
    val carbsPer100: Float = 0f,
    val fatPer100: Float = 0f,
    val ingredientTemplateId: Long = 0L
)

data class MealState(
    val id: Long = 0,
    val name: String,
    val totalCarbohydrates: Float = 0f,
    val totalProtein: Float = 0f,
    val totalFat: Float = 0f,
    val totalCalories: Float = 0f,
    val dayId: Long? = null,
    val mealTemplateId: Long = 0
)

data class MealWithIngredients(
    val meal: Meal,
    val ingredient: List<Ingredient>
)

data class RepoResponse<T>(
    val success: Boolean,
    val data: T,
    val errorMessage: String? = null
)