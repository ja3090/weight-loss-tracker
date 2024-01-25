package com.example.weightdojo.database.dao.test

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.dao.mealtemplate.MealTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.MealTemplateWithIngredients

@Dao
interface TestMealTemplateDao {
    @Query(
        "SELECT * FROM meal_template " +
                "WHERE mealTemplateId = :id"
    )
    fun getMealTemplateById(id: Long): MealTemplate?

    @Query(
        "SELECT * FROM ingredient_template " +
                "WHERE ingredientTemplateId = :id"
    )
    fun getIngTemplateById(id: Long): IngredientTemplate?

    @Query(
        "SELECT * FROM meal_ingredient " +
                "WHERE mealTemplateId = :mealTemplateId " +
                "AND ingredientTemplateId = :ingredientTemplateId "
    )
    fun getMealIngById(mealTemplateId: Long, ingredientTemplateId: Long): MealIngredientTemplate?

    @Transaction
    @Query("SELECT * FROM meal_template")
    fun getAllMealIngredient(): List<MealTemplateWithIngredients>
}