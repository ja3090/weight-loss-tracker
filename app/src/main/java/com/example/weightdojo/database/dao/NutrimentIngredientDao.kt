package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.NutrimentIngredient

@Dao
interface NutrimentIngredientDao {

    @Insert
    fun insertNutrimentIngredient(nutrimentIngredient: NutrimentIngredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM nutriment_ingredient")
    fun _DELETE_ALL()

    @Query("DELETE FROM nutriment_ingredient WHERE ingredientId = :ingredientId")
    fun deleteNutrimentIngredient(ingredientId: Long)
}