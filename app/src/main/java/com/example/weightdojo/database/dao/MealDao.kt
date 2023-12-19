package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.CalorieUnit
import com.example.weightdojo.database.models.Weight
import com.example.weightdojo.database.models.WeightUnit
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface MealDao {
    @Query("INSERT INTO meal " +
            "('day_id', 'date', 'total_carbohydrates', 'total_fat', " +
            "'total_calories', 'total_protein', 'calorie_unit') " +
            "VALUES (:dayId, :date, :totalCarbohydrates, :totalFat, :totalCalories, " +
            ":totalProtein, :calorieUnit)")
    fun insertMealEntry(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        calorieUnit: CalorieUnit
    )

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()
}