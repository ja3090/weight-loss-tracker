package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentData
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.SingleIngredientDetailedDTO
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.utils.totals

@Dao
interface IngredientDao : NormalisationMethods {
    @Query(
        "SELECT * FROM ingredient " +
                "WHERE meal_id = :mealId"
    )
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>

    @Transaction
    fun handleIngredientInsert(ingredients: List<Ingredient>, dayId: Long, mealId: Long) {
        for (ingredient in ingredients) {
            insertIngredient(ingredient)
        }

        val total = totals(ingredients)

        updateMeal(
            id = mealId,
            totalCals = total.totalCals
        )

        updateDay(dayId)
    }

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM ingredient")
    fun _DELETE_ALL()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateIngredient(ingredient: Ingredient): Long

    @Query(
        "DELETE FROM ingredient " +
                "WHERE ingredientId = :id "
    )
    fun deleteIngredient(id: Long)

    @Query(
        "DELETE FROM meal " +
                "WHERE mealId = :id "
    )
    fun deleteMeal(id: Long)

    @Query(
        "SELECT nutriment.name as nutrimentName,  " +
                "ingredient.name as ingredientName,  " +
                "calories_per_100g as caloriesPer100,  " +
                "ingredient.ingredientId as ingredientId, " +
                "nutriment.nutrimentId, " +
                "nutriment_ingredient.gPer100 as gPer100 " +
                "FROM ingredient " +
                "JOIN nutriment_ingredient ON nutriment_ingredient.ingredientId = ingredient.ingredientId " +
                "JOIN nutriment ON nutriment.nutrimentId = nutriment_ingredient.nutrimentId " +
                "WHERE ingredient.name LIKE '%' || :term || '%' " +
                "AND is_template = 1 " +
                "AND is_soft_deleted = 0 " +
                "ORDER BY ingredientName ASC "
    )
    fun searchMealTemplates(term: String): List<IngredientWithNutrimentDataDTO>

    @Query(
        "SELECT " +
                "nutriment.name as nutrimentName, " +
                "meal_id as mealId, " +
                "ingredient.ingredientId, " +
                "gPer100, " +
                "grams, " +
                "ingredient.name as ingredientName, " +
                "ingredient.is_template as ingredientIsTemplate, " +
                "nutriment.nutrimentId, " +
                "calories_per_100g as caloriesPer100 " +
                "FROM ingredient " +
        "JOIN nutriment_ingredient ON nutriment_ingredient.ingredientId = ingredient.ingredientId " +
        "JOIN nutriment ON nutriment_ingredient.nutrimentId = nutriment.nutrimentId " +
        "WHERE ingredient.ingredientId = :id "
    )
    fun getDetailedIngredient(id: Long): List<SingleIngredientDetailedDTO>

    @Query(
        "SELECT " +
                "nutriment.name as nutrimentName, " +
                "0 as mealId, " +
                "0 as caloriesPer100, " +
                "0 as gPer100, " +
                "0 as grams, " +
                "0 as ingredientId, " +
                "'' as ingredientName, " +
                "0 as ingredientIsTemplate, " +
                "nutriment.nutrimentId, " +
                "0 as caloriesPer100 " +
                "FROM nutriment "
    )
    fun getDetailedIngredient(): List<SingleIngredientDetailedDTO>

    @Query(
            "WITH RedundantIngredients AS (" +
                "SELECT * " +
                "FROM ingredient " +
                "LEFT JOIN meal_ingredient ON ingredient.ingredientId = meal_ingredient.ingredientId " +
                "WHERE meal_ingredient.ingredientId IS NULL AND ingredient.is_template = 1 " +
            ")" +
                "DELETE FROM ingredient " +
                "WHERE ingredient.ingredientId IN ( " +
                "    SELECT RedundantIngredients.ingredientId " +
                "    FROM RedundantIngredients " +
                ")"
    )
    fun deleteRedundantIngredients()

    @Transaction
    fun deleteIngredientTemplate(ingredientId: Long) {
        softDeleteIngredientTemplate(ingredientId)
        deleteRedundantIngredients()
    }

    @Query(
        "UPDATE ingredient " +
        "SET is_soft_deleted = 1 " +
        "WHERE ingredientId = :ingredientId "
    )
    fun softDeleteIngredientTemplate(ingredientId: Long)
}