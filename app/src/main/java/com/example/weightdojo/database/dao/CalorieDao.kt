package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.Calorie
import com.example.weightdojo.datatransferobjects.CalorieEntryForEditing
import com.example.weightdojo.datatransferobjects.CalorieEntryIngredients

const val WARNING_MESSAGE = "For internal use only. Please use calorieInsertionHandler"

@Dao
interface CalorieDao {
    @Transaction
    fun calorieInsertionHandler(instance: Calorie) {
        createCaloriesEntry(instance)

        updateDay(instance.dayId)
    }

    @Deprecated(WARNING_MESSAGE)
    @Query(
        "WITH CalorieTotals AS ( " +
                "    SELECT SUM(total_carbohydrates) as totalCarbs, " +
                "     SUM(total_calories) as totalCalories, " +
                "     SUM(total_fat) as totalFat, " +
                "     SUM(total_protein) as totalProtein " +
                "     FROM calorie " +
                "    WHERE calorie.day_id = :dayId " +
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
        "SELECT total_calories as totalCalories, calorie.id as caloriesId, ingredient.name " +
                "FROM calorie " +
                "JOIN ingredient ON calorie.ingredient_id = ingredient.id " +
                "WHERE calorie.day_id = :dayId AND meal_id = :mealId"
    )
    fun getCalorieIngredientsByDayAndMeal(dayId: Long, mealId: Long): List<CalorieEntryIngredients>?

    @Query(
        "SELECT calorie.id as calorieId, ingredient.name, " +
                "grams, calories_per_100g as caloriesPer100 " +
                "FROM calorie " +
                "JOIN ingredient ON calorie.ingredient_id = ingredient.id " +
                "WHERE calorie.day_id = :dayId AND meal_id = :mealId"
    )
    fun getCalorieIngredientsDetailed(dayId: Long, mealId: Long): List<CalorieEntryForEditing>?

    @Deprecated(WARNING_MESSAGE)
    @Insert
    fun createCaloriesEntry(instance: Calorie): Long

    @Deprecated(DEPRECATED_MESSAGE)
    @Query(
        "DELETE FROM calorie"
    )
    fun _DELETE_ALL()
}