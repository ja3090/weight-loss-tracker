package com.example.weightdojo.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredient
import com.example.weightdojo.database.models.Nutriment
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal

interface Inserters {

    @Insert
    fun insertMealEntry(meal: Meal): Long

    @Insert
    fun insertNutrimentMeal(nutrimentMeal: NutrimentMeal)

    @Insert
    fun insertNutrimentIngredient(nutrimentIngredient: NutrimentIngredient)

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Insert
    fun insertMealIngredient(mealIngredient: MealIngredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceIngredient(ingredient: Ingredient): Long

    @Transaction
    fun ingredientInsertWhenEditing(ingredient: Ingredient, mealId: Long): Ingredient {
        val toInsert = if (ingredient.isTemplate) {
            ingredient.copy(ingredientId = 0, isTemplate = false, mealId = mealId)
        } else ingredient

        val newId = replaceIngredient(toInsert)

        return toInsert.copy(ingredientId = newId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceNutrimentIngredient(nutrimentIngredient: NutrimentIngredient): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceMeal(meal: Meal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceNutrimentMeal(nutrimentMeal: NutrimentMeal): Long
}
