package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.NutrimentIngredient

@Dao
interface NutrimentIngredientDao {
    @Query(
        "WITH RedundantIngredients AS (" +
                "SELECT nutriment_ingredient.ingredientId " +
                "FROM nutriment_ingredient " +
                "LEFT JOIN ingredient ON ingredient.ingredientId = nutriment_ingredient.ingredientId " +
                "WHERE nutriment_ingredient.ingredientId IS NULL " +
                ")" +
                "DELETE FROM nutriment_ingredient " +
                "WHERE nutriment_ingredient.ingredientId IN ( " +
                "    SELECT RedundantIngredients.ingredientId " +
                "    FROM RedundantIngredients " +
                ")"
    )
    fun deleteRedundantNutrimentIngredient()

    @Insert
    fun insertNutrimentIngredient(nutrimentIngredient: NutrimentIngredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM nutriment_ingredient")
    fun _DELETE_ALL()

    @Query("DELETE FROM nutriment_ingredient WHERE ingredientId = :ingredientId")
    fun deleteNutrimentIngredient(ingredientId: Long)
}