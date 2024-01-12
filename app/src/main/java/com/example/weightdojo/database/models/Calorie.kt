package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calorie")
data class Calorie (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "grams") val grams: Float,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float?,
    @ColumnInfo(name = "total_protein") val totalProtein: Float?,
    @ColumnInfo(name = "total_fat") val totalFat: Float?,
    @ColumnInfo(name = "total_calories") val totalCalories: Float,
    @ColumnInfo(name = "meal_id") val mealId: Long,
    @ColumnInfo(name = "day_id") val dayId: Long,
    @ColumnInfo(name = "ingredient_id") val ingredientId: Long
)