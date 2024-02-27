package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredient
import com.example.weightdojo.database.models.Nutriment
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
import com.example.weightdojo.utils.totalGrams

@Dao
interface SeedDatabase : Inserters {

    @Transaction
    fun makeTemplate(mealId: Long) {
        val mealWithNutriments =
            getMeal(mealId) ?: throw Exception("Can't find meal with provided ID")

        val (meal) = mealWithNutriments.keys.filter { it.mealId == mealId }
        val nutriments = mealWithNutriments[meal]

        val mealTemplate = meal.copy(mealId = 0, isTemplate = true, dayId = null)
        val ingredients = getIngredientsByMeal(mealId)

        val newMealId = insertMealEntry(mealTemplate)

        processNutrimentMeal(newMealId, nutriments!!)

        for (ingredient in ingredients) {
            val ingTemplate = ingredient.copy(ingredientId = 0, isTemplate = true, mealId = null)

            val ingId = insertIngredient(ingTemplate)
            val mealIng = MealIngredient(mealId = newMealId, ingredientId = ingId)
            makeMealIngredient(mealIng)

            processNutrimentIngredients(
                oldIngId = ingredient.ingredientId,
                newIngId = ingId,
            )
        }
    }

    @Query(
        "SELECT * FROM meal " +
                "JOIN nutriment_meal ON nutriment_meal.mealId = meal.mealId " +
                "WHERE meal.mealId = :mealId"
    )
    fun getMeal(mealId: Long): Map<Meal, List<NutrimentMeal>>?

    @Query("SELECT * FROM ingredient WHERE meal_id = :mealId")
    fun getIngredientsByMeal(mealId: Long): List<Ingredient>

    @Transaction
    fun processNutrimentIngredients(
        oldIngId: Long,
        newIngId: Long,
    ) {
        val nutrIngs = getNutrimentIngredient(oldIngId)

        for (nutrIng in nutrIngs) {
            insertNutrimentIngredient(
                NutrimentIngredient(
                    ingredientId = newIngId,
                    nutrimentId = nutrIng.nutrimentId,
                    gPer100 = nutrIng.gPer100
                )
            )
        }
    }
    @Transaction
    fun processNutrimentMeal(
        newMealId: Long,
        nutriments: List<NutrimentMeal>
    ) {
        for (nutriment in nutriments) {
            insertNutrimentMeal(
                NutrimentMeal(
                    nutrimentId = nutriment.nutrimentId,
                    mealId = newMealId,
                    totalGrams = nutriment.totalGrams
                )
            )
        }
    }

    @Query("SELECT * FROM nutriment_ingredient WHERE ingredientId = :ingredientId")
    fun getNutrimentIngredient(ingredientId: Long): List<NutrimentIngredient>
}