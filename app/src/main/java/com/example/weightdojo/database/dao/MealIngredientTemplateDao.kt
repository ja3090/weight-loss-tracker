package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weightdojo.database.models.MealIngredientTemplate

@Dao
interface MealIngredientTemplateDao {

    @Deprecated(message)
    @Query("DELETE FROM meal_ingredient")
    fun _DELETE_ALL()
}