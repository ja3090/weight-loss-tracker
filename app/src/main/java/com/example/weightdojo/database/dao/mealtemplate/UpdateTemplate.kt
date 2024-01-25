package com.example.weightdojo.database.dao.mealtemplate

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.utils.totals


interface UpdateTemplate {
    @Query(
        "UPDATE meal_template " +
                "SET total_carbohydrates = :totalCarbs, " +
                "total_protein = :totalProtein, " +
                "total_fat = :totalFat, " +
                "total_calories = :totalCals " +
                " WHERE mealTemplateId = :id"
    )
    fun updateMeal(
        totalCarbs: Float,
        totalFat: Float,
        totalCals: Float,
        totalProtein: Float,
        id: Long
    )

    fun processIngredients(ingredientList: List<IngredientState>, mealTemplateId: Long): Int {
        var deleteCounter = 0

        for (ingredient in ingredientList) {
            if (ingredient.markedFor == Marked.DELETE) {
                deleteIngTemplate(
                    ingredient.ingredientTemplateId,
                    mealTemplateId
                )
                deleteCounter++
            } else {
                updateOrAddIngredient(ingredient, mealTemplateId)
            }
        }

        return deleteCounter
    }

    fun updateOrAddIngredient(ingredient: IngredientState, mealTemplateId: Long) {
        val templateConverter = ConvertTemplates()

        val ingTemplate = templateConverter.toIngredientTemplate(ingredient)

        val ingredientTemplateId = overwriteIngredientTemplate(ingTemplate)

        val mealIngredient = MealIngredientTemplate(
            mealTemplateId = mealTemplateId,
            ingredientTemplateId = ingredientTemplateId
        )

        overwriteMealIngredientJunction(mealIngredient)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun overwriteMealTemplate(mealTemplate: MealTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun overwriteMealIngredientJunction(mealIngredientTemplate: MealIngredientTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun overwriteIngredientTemplate(ingredientTemplate: IngredientTemplate): Long

    @Query(
        "DELETE FROM meal_ingredient " +
                "WHERE ingredientTemplateId = :ingredientTemplateId " +
                "AND mealTemplateId = :mealTemplateId "
    )
    fun deleteIngTemplate(ingredientTemplateId: Long, mealTemplateId: Long)
}