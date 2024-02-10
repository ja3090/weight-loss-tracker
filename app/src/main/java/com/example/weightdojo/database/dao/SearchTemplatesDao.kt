package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate

@Dao
interface SearchTemplatesDao {

    @Query("SELECT * FROM ingredient_template " +
            "WHERE soft_delete = 0 AND name LIKE '%' || :term || '%'")
    fun searchIngredientTemplates(term: String): List<IngredientTemplate>


    @Query("SELECT * FROM meal_template " +
            "WHERE name LIKE '%' || :term || '%'")
    fun searchMealTemplates(term: String): List<MealTemplate>
}