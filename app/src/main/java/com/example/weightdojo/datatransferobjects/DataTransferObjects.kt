package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.Day
import java.time.LocalDate
import java.util.UUID

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
    val meals: List<MealWithNutrimentData>,
    val nutrimentTotals: List<NutrimentTotalsByDay>
)

enum class Marked { DELETE, EDIT }

data class IngredientState(
    val internalId: UUID = UUID.randomUUID(),
    var markedFor: Marked = Marked.EDIT,
    val ingredientId: Long = 0,
    val name: String = "",
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

data class NutrimentTotalsByDay(
    var totalGrams: Float,
    val nutrimentName: String,
    val dailyIntakeTarget: Float?
)