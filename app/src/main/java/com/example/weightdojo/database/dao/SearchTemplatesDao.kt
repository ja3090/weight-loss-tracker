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

    @RawQuery
    fun <T> searchTemplates(query: SimpleSQLiteQuery): List<T>

    fun searchIngredientTemplates(term: String): List<IngredientTemplate> {
        val query = SimpleSQLiteQuery(
            "SELECT * FROM ingredient_template " +
                "WHERE name LIKE '%' || ? || '%'", arrayOf(term)
        )

        return searchTemplates(query)
    }

    fun searchMealTemplates(term: String): List<MealTemplate> {
        val query = SimpleSQLiteQuery(
            "SELECT * FROM meal_template " +
                "WHERE name LIKE '%' || ? || '%'", arrayOf(term)
        )

        return searchTemplates(query)
    }
}