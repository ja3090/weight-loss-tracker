package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MealIngredientDao {

    @Query(
        "DELETE FROM meal_ingredient WHERE ingredientId = :ingredientId AND mealId = :mealId "
    )
    fun deleteMealIngredient(mealId: Long, ingredientId: Long)

    @Query(
        "DELETE FROM meal_ingredient WHERE mealId = :mealId "
    )
    fun deleteMealIngredientByMealId(mealId: Long)
}