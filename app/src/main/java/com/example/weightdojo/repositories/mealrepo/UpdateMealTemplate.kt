package com.example.weightdojo.repositories.mealrepo

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.seeder.CalorieTotals
import com.example.weightdojo.utils.seeder.NutrimentTotals
import com.example.weightdojo.utils.totalGrams

interface UpdateMealTemplate {
    val database: AppDatabase
    val mealDao: MealDao
    val calorieTotals: CalorieTotals
    val nutrimentTotals: NutrimentTotals

    fun updateMealTemplateHandler(singleMealDetailed: SingleMealDetailed) {
        val toMeal = singleMealDetailed.toMeal().copy(dayId = null)

        database.mealDao().replaceMeal(toMeal)

        singleMealDetailed.ingredients.forEach {
            processIngredient(
                it,
                singleMealDetailed.mealId
            )
        }

        calorieTotals.updateMealTotals()
        nutrimentTotals.updateMealTotals()
    }

    private fun processIngredient(
        ingredient: SingleMealDetailedIngredient,
        mealId: Long
    ) {
        if (ingredient.markedFor == Marked.DELETE) return deleteIngredient(ingredient, mealId)

        val toIngredient = ingredient.toIngredient()

        mealDao.replaceIngredient(toIngredient)

        val totalCals = totalGrams(ingredient.grams, ingredient.caloriesPer100)

        calorieTotals.updateTotal(mealId, totalCals)

        ingredient.nutriments.forEach { processNutriment(ingredient, it, mealId) }
    }

    private fun processNutriment(
        ingredient: SingleMealDetailedIngredient,
        nutriment: SingleMealDetailedNutriment,
        mealId: Long
    ) {
        val gPer100 = nutriment.gPer100AsString.toFloat()

        val nutrimentIngredient = NutrimentIngredient(
            ingredientId = ingredient.ingredientId,
            nutrimentId = nutriment.nutrimentId,
            gPer100 = gPer100
        )

        database.mealDao().replaceNutrimentIngredient(nutrimentIngredient)

        nutrimentTotals.updateTotal(
            nutrimentId = nutriment.nutrimentId,
            mealId = mealId,
            value = totalGrams(ingredient.grams, gPer100)
        )
    }

    private fun deleteIngredient(ingredient: SingleMealDetailedIngredient, mealId: Long) {
        if (ingredient.ingredientId == 0L) return

        database.mealIngredientDao().deleteMealIngredient(
            mealId = mealId,
            ingredientId = ingredient.ingredientId
        )

        database.nutrimentIngredientDao().deleteNutrimentIngredient(ingredient.ingredientId)
    }
}