package com.example.weightdojo.utils

import android.util.Log
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.NutrimentBreakdown

interface FormatMealData {
    fun formatMealData(mealData: List<MealWithNutrimentDataDTO>): List<MealWithNutrimentData> {

        val nutrimentBreakdownById = mutableMapOf<Long, MutableList<NutrimentBreakdown>>()
        val mealById = mutableMapOf<Long, MealWithNutrimentData>()

        for (meal in mealData) {
            val currList = nutrimentBreakdownById.getOrPut(
                meal.mealId
            ) {
                mutableListOf()
            }

            val nutrimentBreakdown = NutrimentBreakdown(
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
            it.value.copy(nutriments = nutrimentBreakdownById[it.value.mealId] ?: listOf())
        }
    }
}