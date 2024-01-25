package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.MealWithIngredients

@Dao
interface MealDao {
    @Transaction
    fun insertMeal(mealState: Meal, ingredientList: List<IngredientState>) {
        val mealId = insertMealEntry(mealState)

        for (ingredient in ingredientList) {
            if (ingredient.markedFor == Marked.DELETE) continue

            val ing = Ingredient(
                mealId = mealId,
                name = ingredient.name,
                caloriesPer100 = ingredient.caloriesPer100,
                proteinPer100 = ingredient.proteinPer100,
                fatPer100 = ingredient.fatPer100,
                carbohydratesPer100 = ingredient.carbsPer100,
                grams = ingredient.grams
            )

            insertIngredient(ing)
        }
    }

    @Insert
    fun insertIngredient(ingredients: Ingredient)
    @Insert
    fun insertMealEntry(meal: Meal): Long
    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()
}