package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
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
        var day = getDayByDate(date)

        if (day == null) {
            val id = insert(Day(date = date))
            day = getDayById(id)
        }

        day as Day

        val meals = getMeals(day.id)
        val totalCals = totalCalsForDay(day.id)
        val nutrimentTotalsByDay = getNutrimentTotals(day.id)

        return DayData(
            day = day.copy(totalCalories = totalCals),
            meals = formatMealData(meals),
            nutrimentTotals = nutrimentTotalsByDay
        )
    }

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
                "totalGrams,  " +
                "nutriment.nutrimentId  " +
                "FROM meal " +
                "JOIN nutriment_meal ON nutriment_meal.mealId = meal.mealId " +
                "JOIN nutriment ON nutriment.nutrimentId = nutriment_meal.nutrimentId " +
                "WHERE meal.day_id = :dayId " +
                "ORDER BY mealName ASC "
    )
    fun getMeals(dayId: Long): List<MealWithNutrimentDataDTO>

    @Query(
        "SELECT * FROM day " +
                "WHERE date = :date"
    )
    fun getDayByDate(date: LocalDate): Day?

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
        "WITH MealByDayId AS ( " +
                "    SELECT nutriment_meal.nutrimentId, " +
                "SUM(totalGrams) as totalGrams FROM meal " +
                "    JOIN nutriment_meal ON nutriment_meal.mealId = meal.mealId " +
                "    WHERE meal.day_id = :dayId " +
                "    GROUP BY nutriment_meal.nutrimentId " +
                ") " +
                " " +
                "SELECT COALESCE(totalGrams, 0) as totalGrams, nutriment.name as nutrimentName, " +
                "daily_intake_target as dailyIntakeTarget " +
                "FROM nutriment " +
                "LEFT JOIN MealByDayId ON nutriment.nutrimentId = MealByDayId.nutrimentId"
    )
    fun getNutrimentTotals(dayId: Long): List<NutrimentTotalsByDay>
}
