package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.DayWithMeals
import java.time.LocalDate

@Dao
interface DayDao {
    @Transaction
    @Query(
        "SELECT * FROM day " +
                "WHERE date = :date"
    )
    fun getDays(date: LocalDate): DayWithMeals?

    @Transaction
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
                "SET total_calories = :calAvg, total_protein = :proAvg, " +
                "total_fat = :fatAvg, total_carbohydrates = :carbAvg " +
                "WHERE id = :dayId"
    )
    fun setCalorieStats(calAvg: Float, proAvg: Float, fatAvg: Float, carbAvg: Float, dayId: Long)

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