package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_template")
data class MealTemplate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "meal_id") val mealId: Long,
    @ColumnInfo(name = "ingredient_id") val ingredientId: Long
)
