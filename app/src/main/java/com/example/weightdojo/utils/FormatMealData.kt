package com.example.weightdojo.utils

import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownIngredient
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownMeal
import com.example.weightdojo.datatransferobjects.NutrimentTotalsByDay

interface FormatMealData {
    fun formatMealData(mealData: List<MealWithNutrimentDataDTO>): FormattedMealData {

        val nutrimentBreakdownById = mutableMapOf<Long, MutableList<NutrimentBreakdownMeal>>()
        val mealById = mutableMapOf<Long, MealWithNutrimentData>()
        var grandTotalCals = 0f
        val inputtedMealCalories = mutableMapOf<Long, Boolean>()
        val nutrimentTotalsByDay = mutableMapOf<Long, NutrimentTotalsByDay>()

        for (meal in mealData) {
            val currList = nutrimentBreakdownById.getOrPut(
                meal.mealId
            ) {
                mutableListOf()
            }

            if (inputtedMealCalories[meal.mealId] == null) grandTotalCals += meal.totalCalories

            inputtedMealCalories[meal.mealId] = true

            val nutrimentTotal = nutrimentTotalsByDay.getOrPut(
                meal.nutrimentId
            ) {
                NutrimentTotalsByDay(
                    totalGrams = 0f,
                    nutrimentName = meal.nutrimentName,
                    dailyIntakeTarget = meal.dailyIntakeTarget
                )
            }

            nutrimentTotal.totalGrams += meal.totalGrams

            val nutrimentBreakdown = NutrimentBreakdownMeal(
                nutrimentId = meal.nutrimentId,
                nutrimentName = meal.nutrimentName,
                totalGrams = meal.totalGrams
            )

            currList.add(nutrimentBreakdown)

            mealById.putIfAbsent(
                meal.mealId, MealWithNutrimentData(
                    mealId = meal.mealId,
                    mealName = meal.mealName,
                    nutriments = mutableListOf(),
                    totalCalories = meal.totalCalories
                )
            )
        }

        return FormattedMealData(
            totalCalories = grandTotalCals,
            nutrimentTotalsByDay = nutrimentTotalsByDay.values.toList(),
            mealWithNutrimentData = mealById.map {
                it.value.copy(nutriments = nutrimentBreakdownById[it.value.id] ?: mutableListOf())
            }
        )
    }
}

data class FormattedMealData(
    val totalCalories: Float,
    val nutrimentTotalsByDay: List<NutrimentTotalsByDay>,
    val mealWithNutrimentData: List<MealWithNutrimentData>
)