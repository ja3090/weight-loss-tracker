package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.DayWithMeals
import com.example.weightdojo.datatransferobjects.NutritionTotals
import java.time.LocalDate

@Dao
interface DayDao {
    @Transaction
    fun getDay(date: LocalDate): DayWithMeals {
        var day = getDayByDate(date)

        if (day == null) {
            insert(date)
            day = getDayByDate(date)
        }

        day as DayWithMeals

        return day
    }

    @Query(
        "SELECT * FROM day " +
                "WHERE date = :date"
    )
    fun getDayByDate(date: LocalDate): DayWithMeals?

    @Query(
        "SELECT * FROM day " +
                "WHERE id = :id"
    )
    fun getDayById(id: Long): DayWithMeals

    @Query("INSERT INTO day ('date') VALUES (:date)")
    fun insert(date: LocalDate)

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
}