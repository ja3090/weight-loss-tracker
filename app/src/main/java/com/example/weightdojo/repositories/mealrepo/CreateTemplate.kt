package com.example.weightdojo.repositories.mealrepo

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.MealIngredient
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.seeder.CalorieTotals
import com.example.weightdojo.utils.seeder.NutrimentTotals
import com.example.weightdojo.utils.totalGrams

interface CreateTemplate {
    val database: AppDatabase
    val mealDao: MealDao
    val calorieTotals: CalorieTotals
    val nutrimentTotals: NutrimentTotals

    fun createTemplateHandler(singleMealDetailed: SingleMealDetailed) {
        val toMeal = singleMealDetailed.toMeal().copy(mealId = 0, isTemplate = true, dayId = null)

        val mealId = mealDao.insertMealEntry(toMeal)

        singleMealDetailed.ingredients.forEach {
            insertIngredient(it, mealId)
        }

        calorieTotals.updateMealTotals()
        nutrimentTotals.updateMealTotals()
    }

    private fun insertIngredient(ingredient: SingleMealDetailedIngredient, mealId: Long) {
        if (ingredient.markedFor == Marked.DELETE) return

        val toIngredient = ingredient.toIngredient().copy(
            ingredientId = 0,
            isTemplate = true,
            mealId = null
        )

        val ingredientId = database.ingredientDao().insertIngredient(toIngredient)

        insertMealIngredient(ingredientId, mealId)

        calorieTotals.updateTotal(mealId, totalGrams(ingredient.grams, ingredient.caloriesPer100))

        ingredient.nutriments.forEach {
            insertNutrimentIngredient(
                ingredient.copy(ingredientId = ingredientId),
                mealId,
                it
            )

            insertNutrimentMeal(
                ingredient.copy(ingredientId = ingredientId),
                mealId,
                it
            )
        }
    }

    private fun insertMealIngredient(
        ingredientId: Long,
        mealId: Long,
    ) {
        val mealIngredient = MealIngredient(
            mealId = mealId,
            ingredientId = ingredientId
        )

        database.mealDao().insertMealIngredient(mealIngredient)
    }

    private fun insertNutrimentIngredient(
        ingredient: SingleMealDetailedIngredient,
        mealId: Long,
        nutriment: SingleMealDetailedNutriment
    ) {
        val nutrimentIngredient = NutrimentIngredient(
            gPer100 = nutriment.gPer100AsString.toFloat(),
            ingredientId = ingredient.ingredientId,
            nutrimentId = nutriment.nutrimentId
        )

        database.nutrimentIngredientDao().insertNutrimentIngredient(nutrimentIngredient)

        nutrimentTotals.updateTotal(
            nutrimentId = nutriment.nutrimentId,
            mealId = mealId,
            value = totalGrams(ingredient.grams, nutriment.gPer100AsString.toFloat())
        )
    }

    private fun insertNutrimentMeal(
        ingredient: SingleMealDetailedIngredient,
        mealId: Long,
        nutriment: SingleMealDetailedNutriment
    ) {
        val totalGrams = totalGrams(ingredient.grams, nutriment.gPer100AsString.toFloat())

        val nutrimentMeal = NutrimentMeal(
            nutrimentId = nutriment.nutrimentId,
            totalGrams = totalGrams,
            mealId = mealId
        )

        database.nutrimentMealDao().insertNutrimentMeal(nutrimentMeal)
    }
}