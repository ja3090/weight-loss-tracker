package com.example.weightdojo.database.dao

import androidx.room.Query

interface NormalisationMethods {

    @Query(
        "WITH CalorieTotals AS ( " +
                "    SELECT SUM(total_calories) as totalCalories " +
                "    FROM meal " +
                "    WHERE meal.day_id = :dayId " +
                ") " +
                " " +
                "UPDATE day " +
                "SET total_calories = (SELECT totalCalories FROM CalorieTotals) " +
                "WHERE id = :dayId"
    )
    fun updateDay(dayId: Long)

    @Query(
        "UPDATE meal " +
                "SET total_calories = :totalCals " +
                " WHERE mealId = :id"
    )
    fun updateMeal(
        totalCals: Float,
        id: Long
    )
}