package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.DayWithWeightAndMeals
import java.time.LocalDate

@Dao
interface DayDao {
    @Transaction
    @Query("SELECT * FROM day " +
            "WHERE date = :date")
    fun getDays(date: LocalDate): DayWithWeightAndMeals?

    @Transaction
    @Query("SELECT * FROM day " +
            "WHERE id = :id")
    fun getDayById(id: Long): DayWithWeightAndMeals

    @Query("INSERT INTO day ('date') VALUES (:date)")
    fun insert(date: LocalDate)

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM day")
    fun _DELETE_ALL()
}