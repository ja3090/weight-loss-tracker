package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weightdojo.database.models.NutrimentMeal

@Dao
interface NutrimentMealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNutrimentMeal(nutrimentMeal: NutrimentMeal)

    @Query(
        "WITH PriorTotal AS (" +
                    "SELECT SUM(totalGrams) as totalGrams FROM nutriment_meal " +
                    "WHERE nutrimentId = :nutrimentId " +
                    "AND mealId = :mealId " +
                ")" +
                "" +
                "UPDATE nutriment_meal " +
                "SET totalGrams = (SELECT totalGrams FROM PriorTotal) + :totalGrams " +
                "WHERE nutrimentId = :nutrimentId AND mealId = :mealId"
    )
    fun updateNutrimentTotals(nutrimentId: Long, mealId: Long, totalGrams: Float)

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM nutriment_meal")
    fun _DELETE_ALL()
}