package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.MealTemplateWithIngredients
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.MealWithIngredients

const val message = "Internal use only"

@Dao
interface MealTemplateDao {

    @Transaction
    fun handleMealTemplateInsertion(
        mealState: MealState,
        ingredientList: List<IngredientState>,
        overwrite: Boolean
    ) {

        val templateConverter = ConvertTemplates()

        val mealTemplate = templateConverter.toMealTemplate(mealState, overwrite)

        val mealTemplateId = insertMealTemplate(mealTemplate)

        for (ingredient in ingredientList) {
            if (ingredient.markedFor == Marked.DELETE) {
                deleteIngTemplate(
                    ingredient.ingredientTemplateId,
                    mealTemplateId
                )
                continue
            }

            val ingredientTemplate = templateConverter.toIngredientTemplate(ingredient)

            val ingredientTemplateId = insertIngredientTemplate(ingredientTemplate)

            val mealIngredient = MealIngredientTemplate(
                mealTemplateId = mealTemplateId,
                ingredientTemplateId = ingredientTemplateId
            )

            insertMealIngredientJunction(mealIngredient)
        }
    }

    @Query(
        "DELETE FROM meal_ingredient " +
                "WHERE ingredientTemplateId = :ingredientTemplateId " +
                "AND mealTemplateId = :mealTemplateId "
    )
    fun deleteIngTemplate(ingredientTemplateId: Long, mealTemplateId: Long)

    @Transaction
    @Query("SELECT * FROM meal_template")
    fun returnMealTemplateWithIngredients(): List<MealTemplateWithIngredients>

    @Transaction
    @Query(
        "SELECT * FROM meal_template " +
                "WHERE mealTemplateId = :mealTemplateId "
    )
    fun getMealTemplateWithIngredientsById(mealTemplateId: Long): MealTemplateWithIngredients

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealIngredientJunction(mealIngredientTemplate: MealIngredientTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredientTemplate(ingredientTemplate: IngredientTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealTemplate(mealTemplate: MealTemplate): Long

    @Query(
        "SELECT * FROM meal_template"
    )
    fun getMealTemplates(): List<MealTemplate>

    @Query(
        "SELECT * FROM meal_template WHERE name LIKE '%' || :term || '%'"
    )
    fun searchMealTemplates(term: String): List<MealTemplate>

    @Insert
    fun createTemplate(mealTemplate: MealTemplate): Long

    @Deprecated(message)
    @Query("DELETE FROM meal_template")
    fun _DELETE_ALL()
}