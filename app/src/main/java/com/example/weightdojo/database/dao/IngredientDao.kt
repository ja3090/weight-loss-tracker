package com.example.weightdojo.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.Totals
import com.example.weightdojo.utils.totalGramsNonNull
import com.example.weightdojo.utils.totalGramsNullable

@Dao
interface IngredientDao {
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

        updateMeal(mealId)

        updateDay(dayId)
    }

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM ingredient")
    fun _DELETE_ALL()

    @Query(
        "UPDATE ingredient " +
        "SET grams = :grams " +
        "WHERE id = :id "
    )
    fun updateIngredient(grams: Float, id: Long)

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
                updateIngredient(
                    id = entry.ingredientId,
                    grams = entry.grams,
                )
            }
        }

        if (deleteCounter == ingredientStates.size) {
            deleteMeal(mealId)
        } else {
            updateMeal(mealId)
        }



        updateDay(dayId)
    }

    @Query(
        "WITH CalorieTotals AS ( " +
                "    SELECT SUM(total_carbohydrates) as totalCarbs, " +
                "     SUM(total_calories) as totalCalories, " +
                "     SUM(total_fat) as totalFat, " +
                "     SUM(total_protein) as totalProtein " +
                "     FROM meal " +
                "    WHERE meal.day_id = :dayId " +
                ") " +
                " " +
                "UPDATE day " +
                "SET total_carbohydrates = (SELECT totalCarbs FROM CalorieTotals), " +
                "total_protein = (SELECT totalProtein FROM CalorieTotals), " +
                " total_fat = (SELECT totalFat FROM CalorieTotals), " +
                " total_calories = (SELECT totalCalories FROM CalorieTotals) " +
                "WHERE id = :dayId"
    )
    fun updateDay(dayId: Long)

    @Query(
        "WITH CalorieTotals AS ( " +
                "    SELECT SUM((grams / 100) * calories_per_100g) as totalCalories, " +
                "     SUM((grams / 100) * fat_per_100g) as totalFat, " +
                "     SUM((grams / 100) * carbohydrates_per_100g) as totalCarbs, " +
                "     SUM((grams / 100) * protein_per_100g) as totalProtein " +
                "     FROM ingredient " +
                "    WHERE ingredient.meal_id = :mealId " +
                ") " +
                " " +
                "UPDATE meal " +
                "SET total_carbohydrates = (SELECT totalCarbs FROM CalorieTotals), " +
                "total_protein = (SELECT totalProtein FROM CalorieTotals), " +
                " total_fat = (SELECT totalFat FROM CalorieTotals), " +
                " total_calories = (SELECT totalCalories FROM CalorieTotals) " +
                "WHERE id = :mealId"
    )
    fun updateMeal(mealId: Long)
}