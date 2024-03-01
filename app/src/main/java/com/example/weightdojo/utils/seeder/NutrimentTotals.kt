package com.example.weightdojo.utils.seeder

import com.example.weightdojo.database.AppDatabase


typealias NutrimentTotalsKey = Pair<Long, Long>

class NutrimentTotals(
    private val database: AppDatabase,
) {
    private var nutrimentTotals: MutableMap<NutrimentTotalsKey, Float> = mutableMapOf()

    private fun reset() {
        nutrimentTotals = mutableMapOf()
    }

    fun updateMealTotals() {
        for (key in nutrimentTotals.keys) {
            database.nutrimentMealDao().updateNutrimentTotals(
                nutrimentId = key.first,
                mealId = key.second,
                totalGrams = getTotal(key)
            )
        }

        reset()
    }

    fun updateTotal(nutrimentId: Long, mealId: Long, value: Float) {
        val key = generateKey(nutrimentId = nutrimentId, mealId = mealId)

        nutrimentTotals[key] = (nutrimentTotals[key] ?: 0f) + value
    }

    private fun getTotal(key: NutrimentTotalsKey): Float {
        return nutrimentTotals[key] ?: 0f
    }

    private fun generateKey(
        nutrimentId: Long,
        mealId: Long,
    ): NutrimentTotalsKey {
        return Pair(nutrimentId, mealId)
    }
}