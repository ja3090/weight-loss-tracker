package com.example.weightdojo.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.NutrimentTotalsByDay
import com.example.weightdojo.utils.FormatMealData
import java.time.LocalDate

@Dao
interface DayDao : NormalisationMethods, FormatMealData {

    @Transaction
    fun getDay(date: LocalDate): DayData {
        var day = getDayByDate(date) ?: Day(date = date)

        if (day.id == 0L) {
            val id = insert(day)
            day = day.copy(id = id)
        }

        val meals = getMeals(day.id)

        val formattedData = formatMealData(meals)

        return DayData(
            day = day.copy(totalCalories = formattedData.totalCalories),
            meals = formattedData.mealWithNutrimentData,
            nutrimentTotals = formattedData.nutrimentTotalsByDay.ifEmpty {
                getNutrimentTotals()
            }
        )
    }

    @Query(
        "SELECT * FROM day " +
                "WHERE day.date = :date "
    )
    fun getDayByDate(date: LocalDate): Day?

    @Query(
        "SELECT SUM(total_calories) as total_calories FROM meal " +
                "WHERE day_id = :dayId "
    )
    fun totalCalsForDay(dayId: Long): Float?

    @Query(
        "SELECT nutriment.name as nutrimentName,  " +
                "meal.name as mealName,  " +
                "total_calories as totalCalories,  " +
                "meal.mealId as mealId, " +
                "COALESCE(totalGrams, 0) as totalGrams,  " +
                "nutriment.nutrimentId," +
                "nutriment.daily_intake_target as dailyIntakeTarget " +
                "FROM meal " +
                "CROSS JOIN nutriment " +
                "LEFT JOIN nutriment_meal ON nutriment_meal.mealId = meal.mealId " +
                "AND nutriment.nutrimentId = nutriment_meal.nutrimentId " +
                "WHERE meal.day_id = :dayId " +
                "ORDER BY mealName ASC "
    )
    fun getMeals(dayId: Long): List<MealWithNutrimentDataDTO>

    @Query(
        "SELECT * FROM day " +
                "WHERE id = :id "
    )
    fun getDayById(id: Long): Day?

    @Insert
    fun insert(day: Day): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM day")
    fun _DELETE_ALL()

    @Query(
        "UPDATE day " +
                "SET weight = :weight " +
                "WHERE id = :dayId"
    )
    fun setWeight(weight: Float, dayId: Long)

    @Query(
        "SELECT * FROM day " +
                "WHERE weight IS NOT NULL " +
                "ORDER BY date DESC " +
                "LIMIT 1 "
    )
    fun getMostRecentWeight(): Day?

    @Query(
        "SELECT 0 as totalGrams, " +
                "daily_intake_target as dailyIntakeTarget, " +
                "name as nutrimentName FROM nutriment"
    )
    fun getNutrimentTotals(): List<NutrimentTotalsByDay>
}
