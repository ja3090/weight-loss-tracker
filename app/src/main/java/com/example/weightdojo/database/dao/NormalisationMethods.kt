package com.example.weightdojo.database.dao

import androidx.room.Query

interface NormalisationMethods {

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
                "total_fat = (SELECT totalFat FROM CalorieTotals), " +
                "total_calories = (SELECT totalCalories FROM CalorieTotals) " +
                "WHERE id = :dayId"
    )
    fun updateDay(dayId: Long)

    @Query(
        "UPDATE meal " +
                "SET total_carbohydrates = :totalCarbs, " +
                "total_protein = :totalProtein, " +
                "total_fat = :totalFat, " +
                "total_calories = :totalCals " +
                " WHERE id = :id"
    )
    fun updateMeal(
        totalCarbs: Float,
        totalFat: Float,
        totalCals: Float,
        totalProtein: Float,
        id: Long
    )

    @Query("DELETE FROM ingredient_template " +
            "WHERE ingredientTemplateId NOT IN (SELECT ingredientTemplateId FROM meal_ingredient)")
    fun deleteUnusedIngredientTemplates()
}