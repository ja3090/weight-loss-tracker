package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

@Dao
interface MealDao {
    @Query(
        "INSERT INTO meal " +
                "('day_id', 'date', 'total_carbohydrates', 'total_fat', " +
                "'total_calories', 'total_protein', 'calorie_unit') " +
                "VALUES (:dayId, :date, :totalCarbohydrates, :totalFat, :totalCalories, " +
                ":totalProtein, :calorieUnit)"
    )
    fun insertMealEntry(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        calorieUnit: CalorieUnits
    )

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()

    @Query(
        "SELECT " +
                "SUM(total_carbohydrates) as carbAvg, SUM(total_fat) as fatAvg, " +
                "SUM(total_calories) as calAvg, SUM(total_protein) as proAvg " +
                "FROM meal WHERE day_id = :dayId"
    )
    fun getNutritionAverages(dayId: Long): NutritionAverageDTO
}

class NutritionAverageDTO(
    val carbAvg: Float,
    val fatAvg: Float,
    val calAvg: Float,
    val proAvg: Float
)