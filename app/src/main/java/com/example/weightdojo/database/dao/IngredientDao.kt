package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.utils.totals

@Dao
interface IngredientDao : NormalisationMethods {
    @Query(
        "SELECT * FROM ingredient " +
                "WHERE meal_id = :mealId"
    )
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>

    @Transaction
    fun handleIngredientInsert(ingredients: List<Ingredient>, dayId: Long, mealId: Long) {
        for (ingredient in ingredients) {
            insertIngredient(ingredient)
        }

        val total = totals(ingredients)

        updateMeal(
            id = mealId,
            totalCals = total.totalCals
        )

        updateDay(dayId)
    }

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM ingredient")
    fun _DELETE_ALL()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateIngredient(ingredient: Ingredient): Long

    @Query(
        "DELETE FROM ingredient " +
                "WHERE ingredientId = :id "
    )
    fun deleteIngredient(id: Long)

    @Query(
        "DELETE FROM meal " +
                "WHERE mealId = :id "
    )
    fun deleteMeal(id: Long)
}