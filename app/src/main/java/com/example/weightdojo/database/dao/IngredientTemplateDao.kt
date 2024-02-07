package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.dao.mealtemplate.message
import com.example.weightdojo.database.models.IngredientTemplate

@Dao
interface IngredientTemplateDao {
    @Delete
    fun deleteIngTemplate(ingredientTemplate: IngredientTemplate)

    @Query("DELETE FROM meal_ingredient WHERE ingredientTemplateId = :ingTemplateId")
    fun deleteMealIng(ingTemplateId: Long)

    @Transaction
    fun deleteHandler(ingredientTemplate: IngredientTemplate) {
        deleteIngTemplate(ingredientTemplate)
        deleteMealIng(ingredientTemplate.ingredientTemplateId)
    }

    @Query(
        "SELECT * FROM ingredient_template WHERE name LIKE '%' || :term || '%'"
    )
    fun searchIngredientTemplates(term: String): List<IngredientTemplate>

    @Deprecated(message)
    @Query("DELETE FROM ingredient_template")
    fun _DELETE_ALL()
}