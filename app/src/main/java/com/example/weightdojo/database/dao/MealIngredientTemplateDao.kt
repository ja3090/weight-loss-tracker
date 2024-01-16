package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MealIngredientTemplateDao {


    @Deprecated(message)
    @Query("DELETE FROM meal_ingredient")
    fun _DELETE_ALL()
}