package com.example.weightdojo.database.dao.mealtemplate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.MealTemplateWithIngredients
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.utils.totals

const val message = "Internal use only"

@Dao
interface MealTemplateDao : UpdateTemplate {

    @Transaction
    fun updateMealTemplate(
        mealTemplate: MealTemplate,
        ingredientTemplates: List<IngredientState>,
    ) {
        val mealTemplateId = overwriteMealTemplate(mealTemplate)

        if (mealTemplateId != mealTemplate.mealTemplateId) {
            throw Exception("Update unsuccessful, an existing row isn't being edited")
        }

        val deleteCounter = processIngredients(ingredientTemplates, mealTemplateId)

        if (deleteCounter == ingredientTemplates.size) {
            throw Exception("All ingredients are being deleted, this should be validated against")
        }

        val totals = totals(ingredientTemplates.filter { it.markedFor !== Marked.DELETE })

        updateMeal(
            totalCals = totals.totalCals,
            totalProtein = totals.protein,
            totalFat = totals.fat,
            totalCarbs = totals.carbs,
            id = mealTemplateId
        )
    }

    @Transaction
    fun createMealTemplate(
        mealTemplate: MealTemplate,
        ingredientTemplates: List<IngredientTemplate>
    ) {
        val mealTemplateId = insertMealTemplate(mealTemplate)
        overwriteOrCreateIngredients(ingredientTemplates)

        for (templ in ingredientTemplates) {
            val mealIng =
                MealIngredientTemplate(mealTemplateId, templ.ingredientTemplateId)
            overwriteMealIngredientJunction(mealIng)
        }
    }

    @Insert
    fun insertMealTemplate(mealTemplate: MealTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun overwriteOrCreateIngredients(ingredientTemplates: List<IngredientTemplate>)

    @Transaction
    @Query(
        "SELECT * FROM meal_template " +
                "WHERE mealTemplateId = :mealTemplateId "
    )
    fun getMealTemplateWithIngredientsById(mealTemplateId: Long): MealTemplateWithIngredients

    @Query(
        "SELECT * FROM meal_template WHERE name LIKE '%' || :term || '%'"
    )
    fun searchMealTemplates(term: String): List<MealTemplate>

    @Deprecated(message)
    @Query("DELETE FROM meal_template")
    fun _DELETE_ALL()
}