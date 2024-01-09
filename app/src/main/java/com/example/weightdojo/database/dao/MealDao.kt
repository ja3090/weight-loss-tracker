package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.datatransferobjects.NutritionTotals
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

@Dao
interface MealDao {
    @Transaction
    fun handleMealInsert(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        name: String
    ): Long {
        insertMealEntry(
            dayId,
            date,
            totalCarbohydrates,
            totalFat,
            totalCalories,
            totalProtein,
            name
        )

        val dayTotals = getDayTotals(dayId)

        updateDayTotals(
            dayId,
            dayTotals.totalCarbohydrates,
            dayTotals.totalFat,
            dayTotals.totalCalories,
            dayTotals.totalProtein
        )

        return lastInsertId()
    }

    @Query(
        "SELECT uid FROM meal " +
        "ORDER BY uid DESC " +
        "LIMIT 1 "
    )
    fun lastInsertId(): Long

    @Query(
        "SELECT SUM(total_calories) as totalCalories, SUM(total_carbohydrates) as totalCarbohydrates, " +
                "SUM(total_protein) as totalProtein, SUM(total_fat) as totalFat " +
                "FROM meal " +
                "WHERE day_id = :dayId "
    )
    fun getDayTotals(dayId: Long): NutritionTotals

    @Query(
        "UPDATE day " +
        "SET total_carbohydrates = :totalCarbohydrates, total_protein = :totalProtein, " +
        "total_fat = :totalFat, total_calories = :totalCalories " +
        "WHERE id = :dayId "
    )
    fun updateDayTotals(
        dayId: Long,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float?,
        totalProtein: Float?,
    )

    @Query(
        "INSERT INTO meal " +
                "('day_id', 'date', 'total_carbohydrates', 'total_fat', " +
                "'total_calories', 'total_protein', 'name') " +
                "VALUES (:dayId, :date, :totalCarbohydrates, :totalFat, :totalCalories, " +
                ":totalProtein, :name)"
    )
    fun insertMealEntry(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        name: String
    )

    @Query(
        "SELECT * FROM meal " +
                "WHERE day_id = :dayId"
    )
    fun getMealByDayId(dayId: Long): Meal

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()
}