package com.example.weightdojo.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
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
        "WITH PriorTotal AS (" + "SELECT SUM((grams/100) * calories_per_100g) as totalCals FROM ingredient " + "WHERE meal_id = :mealId " + ")" + "" + "UPDATE meal " + "SET total_calories = (SELECT totalCals FROM PriorTotal)" + "WHERE mealId = :mealId"
    )
    fun updateCalorieTotals(mealId: Long)

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

    @Transaction
    fun updateMeal(singleMealDetailed: SingleMealDetailed) {
        val (calorieTotal, nutrimentTotals) = processIngredients(singleMealDetailed.ingredients)

        processNutrimentMeals(singleMealDetailed, nutrimentTotals)

        val meal = singleMealDetailed.toMeal().copy(totalCalories = calorieTotal)

        replaceMeal(meal)
    }

    @Transaction
    fun processIngredients(
        ingredients: List<SingleMealDetailedIngredient>
    ): NutrimentCalorieTotals {
        val nutrimentTotals = mutableMapOf<SingleMealDetailedNutriment, Float>()
        var calorieTotal = 0f

        for (ingredient in ingredients) {
            val toIngredient = ingredient.toIngredient()

            Log.d("ingredient", ingredient.toString())
            Log.d("toIngredient", toIngredient.toString())

            replaceIngredient(toIngredient)

            calorieTotal += totalGrams(ingredient.grams, ingredient.caloriesPer100)

            processNutrimentIngredients(ingredient.nutriments, ingredient, nutrimentTotals)
        }

        return Pair(calorieTotal, nutrimentTotals)
    }

    @Transaction
    fun processNutrimentMeals(
        singleMealDetailed: SingleMealDetailed,
        nutrimentTotals: MutableMap<SingleMealDetailedNutriment, Float>,
    ) {
        for (key in nutrimentTotals.keys) {
            val nutrimentMeal = NutrimentMeal(
                nutrimentId = key.nutrimentId,
                mealId = singleMealDetailed.mealId,
                totalGrams = nutrimentTotals[key] ?: 0f
            )

            replaceNutrimentMeal(nutrimentMeal)
        }
    }

    @Transaction
    fun processNutrimentIngredients(
        nutriments: List<SingleMealDetailedNutriment>,
        ingredient: SingleMealDetailedIngredient,
        totals: MutableMap<SingleMealDetailedNutriment, Float>
    ) {
        for (nutriment in nutriments) {
            val gPer100 = nutriment.gPer100AsString.toFloat()

            val nutrimentIngredient = NutrimentIngredient(
                ingredientId = ingredient.ingredientId,
                nutrimentId = nutriment.nutrimentId,
                gPer100 = gPer100
            )

            replaceNutrimentIngredient(nutrimentIngredient)

            val newValue = totals.getOrPut(nutriment) { 0f } + totalGrams(ingredient.grams, gPer100)

            totals[nutriment] = newValue
        }
    }
}