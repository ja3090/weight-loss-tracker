package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IngredientTemplateDao {

    @Deprecated(message)
    @Query("DELETE FROM ingredient_template")
    fun _DELETE_ALL()
}