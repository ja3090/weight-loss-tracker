package com.example.weightdojo.utils.seeder

import com.example.weightdojo.database.AppDatabase

class CalorieTotals(
    private val database: AppDatabase,
) {
    private var calorieTotals: MutableMap<Long, Float> = mutableMapOf()

    private fun reset() {
        calorieTotals = mutableMapOf()
    }

    fun updateMealTotals() {
        for (key in calorieTotals.keys) {
            database.mealDao().updateCalorieTotals(
                mealId = key,
                totalCalories = getTotal(key)
            )
        }

        reset()
    }

    fun updateTotal(mealId: Long, value: Float) {
        calorieTotals[mealId] = (calorieTotals[mealId] ?: 0f) + value
    }

    private fun getTotal(mealId: Long): Float {
        return calorieTotals[mealId] ?: 0f
    }
}