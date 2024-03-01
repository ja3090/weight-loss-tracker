package com.example.weightdojo.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataDTO
import com.example.weightdojo.datatransferobjects.SingleMealBuilder
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedDTO
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.totalGrams

typealias NutrimentCalorieTotals = Pair<Float, MutableMap<SingleMealDetailedNutriment, Float>>

@Dao
interface MealDao : NormalisationMethods, Inserters {
    @Query(
        "UPDATE meal " +
        "SET total_calories = :totalCalories " +
        "WHERE mealId = :mealId "
    )
    fun updateCalorieTotals(mealId: Long, totalCalories: Float)

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM meal")
    fun _DELETE_ALL()

    @Query("DELETE FROM meal WHERE mealId = :id ")
    fun deleteMeal(id: Long)

    @Query(
        "SELECT nutriment.name as nutrimentName,  " +
                "meal.name as mealName, " +
                "mealId, " +
                "total_calories as totalCalories, " +
                "day_id as dayId,  " +
                "ingredient.ingredientId, " +
                "gPer100, " +
                "grams, " +
                "ingredient.name as ingredientName, " +
                "meal.is_template as mealIsTemplate, " +
                "ingredient.is_template as ingredientIsTemplate, " +
                "nutriment.nutrimentId, " +
                "calories_per_100g as caloriesPer100 " +
                "FROM meal " +
                "JOIN ingredient ON ingredient.meal_id = meal.mealId " +
                "LEFT JOIN nutriment_ingredient ON nutriment_ingredient.ingredientId = ingredient.ingredientId " +
                "JOIN nutriment ON nutriment.nutrimentId = nutriment_ingredient.nutrimentId " +
                "WHERE mealId = :mealId "
    )
    fun mealWithIngredientsDetailed(mealId: Long): List<SingleMealDetailedDTO>
//    @Query(
//        "SELECT nutriment.name as nutrimentName, " +
//                "                meal.name as mealName," +
//                "                mealId," +
//                "                total_calories as totalCalories," +
//                "                day_id as dayId," +
//                "                ingredient.ingredientId," +
//                "                grams," +
//                "                COALESCE(gPer100, 0) as gPer100," +
//                "                ingredient.name as ingredientName," +
//                "                meal.is_template as mealIsTemplate," +
//                "                ingredient.is_template as ingredientIsTemplate," +
//                "                nutriment.nutrimentId," +
//                "                calories_per_100g as caloriesPer100" +
//                "                FROM meal" +
//                "                JOIN ingredient ON ingredient.meal_id = meal.mealId" +
//                "                CROSS JOIN nutriment" +
//                "                LEFT JOIN nutriment_ingredient ON nutriment_ingredient.nutrimentId = nutriment.nutrimentId AND nutriment_ingredient.ingredientId = ingredient.ingredientId" +
//                "                WHERE mealId = 1"
//    )
//    fun mealWithIngredientsDetailed(mealId: Long): List<SingleMealDetailedDTO>

    @Transaction
    fun handleDelete(id: Long, dayId: Long) {
        deleteMeal(id)
        updateDay(dayId)
    }

    fun getMealWithIngredientsDetailed(mealId: Long): SingleMealDetailed? {
        val rows = mealWithIngredientsDetailed(mealId)

        val singleMealDetailed = SingleMealBuilder(rows)

        return singleMealDetailed.data
    }

    @Query(
        "SELECT nutriment.name as nutrimentName,  " +
                "meal.name as mealName,  " +
                "total_calories as totalCalories,  " +
                "meal.mealId as mealId, " +
                "totalGrams,  " +
                "nutriment.nutrimentId  " +
                "FROM meal " +
                "JOIN nutriment_meal ON nutriment_meal.mealId = meal.mealId " +
                "JOIN nutriment ON nutriment.nutrimentId = nutriment_meal.nutrimentId " +
                "WHERE meal.name LIKE '%' || :term || '%' AND is_template = 1 " +
                "ORDER BY mealName ASC "
    )
    fun searchMealTemplates(term: String): List<MealWithNutrimentDataDTO>
}