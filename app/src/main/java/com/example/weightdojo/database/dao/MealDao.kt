package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.Meal

@Dao
interface MealDao {
    @Insert
    fun insertMealEntry(meal: Meal): Long

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()
}