package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weightdojo.database.dao.mealtemplate.message
import com.example.weightdojo.database.models.IngredientTemplate

@Dao
interface IngredientTemplateDao {
    @Query(
        "SELECT * FROM ingredient_template WHERE name LIKE '%' || :term || '%'"
    )
    fun searchIngredientTemplates(term: String): List<IngredientTemplate>

    @Deprecated(message)
    @Query("DELETE FROM ingredient_template")
    fun _DELETE_ALL()
}