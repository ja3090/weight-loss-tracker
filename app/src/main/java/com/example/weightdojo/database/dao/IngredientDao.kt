package com.example.weightdojo.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.NutritionTotals
import com.example.weightdojo.datatransferobjects.NutritionTotalsIngredients

@Dao
interface IngredientDao {
    @Transaction
    fun handleIngredientInsert(
        name: String,
        carbs: Float?,
        protein: Float?,
        fat: Float?,
        calories: Float,
        mealId: Long,
    ) {
        insertIngredient(
            name,
            carbs,
            protein,
            fat,
            calories,
            mealId
        )

        val mealTotals = getMealTotals(mealId)

        updateMealTotals(
            totalCarbohydrates = mealTotals.totalCarbohydrates,
            totalProtein = mealTotals.totalProtein,
            totalFat = mealTotals.totalFat,
            totalCalories = mealTotals.totalCalories,
            hasIngredients = true,
            id = mealId
        )

        val dayTotals = getDayTotals(mealTotals.dayId)

        updateDayTotals(
            dayId = mealTotals.dayId,
            totalCalories = dayTotals.totalCalories,
            totalFat = dayTotals.totalFat,
            totalProtein = dayTotals.totalProtein,
            totalCarbohydrates = dayTotals.totalCarbohydrates
        )
    }

    @Query(
        "INSERT INTO ingredient " +
                "('name', 'carbohydrates', 'protein', 'fat', 'calories', 'meal_id') " +
                "VALUES (:name, :carbs, :protein, :fat, :calories, :mealId) "
    )
    fun insertIngredient(
        name: String,
        carbs: Float?,
        protein: Float?,
        fat: Float?,
        calories: Float,
        mealId: Long,
    )

    @Query(
        "SELECT SUM(calories) as totalCalories, " +
                "SUM(carbohydrates) as totalCarbohydrates, " +
                "SUM(protein) as totalProtein, " +
                "SUM(fat) as totalFat, " +
                "meal.day_id as dayId " +
                "FROM ingredient " +
                "JOIN meal ON meal.uid = ingredient.meal_id " +
                "WHERE meal_id = :mealId "
    )
    fun getMealTotals(mealId: Long): NutritionTotalsIngredients

    @Query(
        "UPDATE meal " +
                "SET total_calories = :totalCalories, " +
                "total_fat = :totalFat, " +
                "total_calories = :totalCalories, " +
                "total_protein = :totalProtein, " +
                "total_carbohydrates = :totalCarbohydrates," +
                "has_ingredients = :hasIngredients " +
                "WHERE uid = :id "
    )
    fun updateMealTotals(
        totalCarbohydrates: Float?,
        totalProtein: Float?,
        totalFat: Float?,
        totalCalories: Float?,
        id: Long,
        hasIngredients: Boolean
    )

    @Query(
        "UPDATE day " +
                "SET total_calories = :totalCalories, " +
                "total_fat = :totalFat, " +
                "total_calories = :totalCalories, " +
                "total_protein = :totalProtein, " +
                "total_carbohydrates = :totalCarbohydrates " +
                "WHERE id = :dayId "
    )
    fun updateDayTotals(
        totalCarbohydrates: Float?,
        totalProtein: Float?,
        totalFat: Float?,
        totalCalories: Float?,
        dayId: Long,
    )

    @Query(
        "SELECT SUM(total_carbohydrates) as totalCarbs, " +
                "SUM(total_protein) as totalProtein, " +
                "SUM(total_fat) as totalFat, " +
                "SUM(total_calories) as totalCalories " +
                "FROM meal " +
                "WHERE day_id = :dayId "
    )
    fun getDayTotals(dayId: Long): NutritionTotals

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM ingredient")
    fun _DELETE_ALL()

    @Query(
        "SELECT * FROM ingredient " +
                "WHERE meal_id = :mealId "
    )
    fun getIngredients(mealId: Long): List<Ingredient>?
}