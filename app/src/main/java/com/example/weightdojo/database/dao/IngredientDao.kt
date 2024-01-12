package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.Ingredient

@Dao
interface IngredientDao {
    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM ingredient")
    fun _DELETE_ALL()
}