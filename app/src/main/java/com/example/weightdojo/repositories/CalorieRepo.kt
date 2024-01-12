package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.CalorieDao
import com.example.weightdojo.datatransferobjects.CalorieEntryForEditing
import com.example.weightdojo.datatransferobjects.CalorieEntryIngredients
import com.example.weightdojo.datatransferobjects.IngredientState

interface CalorieRepo {
    fun getIngredientsForDayAndMeal(dayId: Long, mealId: Long): List<CalorieEntryIngredients>?
    fun getIngredientsDetailedView(dayId: Long, mealId: Long): List<IngredientState>?
    fun updateIngredients(dayId: Long, ingredients: List<IngredientState>)
}

class CalorieRepoImpl(
    val database: AppDatabase,
    private val calorieDao: CalorieDao = database.calorieDao()
) : CalorieRepo {

    override fun getIngredientsForDayAndMeal(
        dayId: Long,
        mealId: Long
    ): List<CalorieEntryIngredients>? {
        return calorieDao.getCalorieIngredientsByDayAndMeal(dayId = dayId, mealId = mealId)
    }

    override fun getIngredientsDetailedView(
        dayId: Long,
        mealId: Long
    ): List<IngredientState>? {
        val rows = calorieDao.getCalorieIngredientsDetailed(dayId = dayId, mealId = mealId)

        return generateListState(rows)
    }

    override fun updateIngredients(dayId: Long, ingredients: List<IngredientState>) {
        calorieDao.calorieEntryUpdateHandler(dayId, ingredients)
    }

    private fun generateListState(
        ingredientList: List<CalorieEntryForEditing>?
    ): List<IngredientState>? {
        val ingredientListOrEmptyList = ingredientList ?: return null

        val mutableIngredientList = mutableListOf<IngredientState>()

        for (ingredient in ingredientListOrEmptyList) {
            mutableIngredientList.add(
                IngredientState(
                    calorieId = ingredient.calorieId,
                    name = ingredient.name,
                    caloriesPer100 = ingredient.caloriesPer100,
                    grams = ingredient.grams
                )
            )
        }

        return mutableIngredientList
    }
}