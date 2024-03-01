package com.example.weightdojo.utils

import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownIngredient
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownMeal

interface FormatMealData {
    fun formatMealData(mealData: List<MealWithNutrimentDataDTO>): List<MealWithNutrimentData> {

        val nutrimentBreakdownById = mutableMapOf<Long, MutableList<NutrimentBreakdownMeal>>()
        val mealById = mutableMapOf<Long, MealWithNutrimentData>()

        for (meal in mealData) {
            val currList = nutrimentBreakdownById.getOrPut(
                meal.mealId
            ) {
                mutableListOf()
            }

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

        return mealById.map {
            it.value.copy(nutriments = nutrimentBreakdownById[it.value.id] ?: mutableListOf())
        }
    }
}