package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.utils.totals

@Dao
interface IngredientDao : CommonMethods {
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
            totalCarbs = total.carbs,
            totalProtein = total.protein,
            totalFat = total.fat,
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
                "WHERE id = :id "
    )
    fun deleteIngredient(id: Long)

    @Query(
        "DELETE FROM meal " +
                "WHERE id = :id "
    )
    fun deleteMeal(id: Long)

    @Transaction
    fun ingredientUpdateHandler(
        ingredientStates: List<IngredientState>,
        mealId: Long,
        dayId: Long
    ) {
        var deleteCounter = 0

        for (entry in ingredientStates) {
            if (entry.markedFor == Marked.DELETE || entry.grams == 0f) {
                deleteIngredient(entry.ingredientId)
                deleteCounter++
            } else {
                val ingredient = ConvertTemplates().toIngredient(entry, mealId)
                updateIngredient(ingredient)
            }
        }

        if (deleteCounter == ingredientStates.size) {
            throw Exception("Please validate that all ingredients being deleted doesn't pass")
        }

        val totals = totals(ingredientStates.filter { it.markedFor !== Marked.DELETE })

        updateMeal(
            id = mealId,
            totalCals = totals.totalCals,
            totalFat = totals.fat,
            totalProtein = totals.protein,
            totalCarbs = totals.carbs
        )

        updateDay(dayId)
    }
}