package com.example.weightdojo.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
    fun makeMealIngredient(mealIngredient: MealIngredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceIngredient(ingredient: Ingredient): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceNutrimentIngredient(nutrimentIngredient: NutrimentIngredient): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceMeal(meal: Meal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceNutrimentMeal(nutrimentMeal: NutrimentMeal): Long
}
