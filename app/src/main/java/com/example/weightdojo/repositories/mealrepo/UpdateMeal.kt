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

interface UpdateMeal {
    val database: AppDatabase
    val mealDao: MealDao
    val calorieTotals: CalorieTotals
    val nutrimentTotals: NutrimentTotals

    fun updateHandler(singleMealDetailed: SingleMealDetailed) {
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
        if (ingredient.markedFor == Marked.DELETE) return deleteIngredient(ingredient)

        val toIngredientTable = ingredient.toIngredient()

        val newIngredient = mealDao.ingredientInsertWhenEditing(toIngredientTable, mealId)

        val totalCals = totalGrams(ingredient.grams, ingredient.caloriesPer100)

        calorieTotals.updateTotal(mealId, totalCals)

        val ingredientFinal = ingredient.copy(
            ingredientIsTemplate = newIngredient.isTemplate,
            ingredientId = newIngredient.ingredientId
        )

        ingredient.nutriments.forEach { processNutriment(ingredientFinal, it, mealId) }
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

    private fun deleteIngredient(ingredient: SingleMealDetailedIngredient) {
        if (ingredient.ingredientId == 0L || ingredient.ingredientIsTemplate) return

        database.ingredientDao().deleteIngredient(ingredient.ingredientId)

        database.nutrimentIngredientDao().deleteNutrimentIngredient(ingredient.ingredientId)
    }
}