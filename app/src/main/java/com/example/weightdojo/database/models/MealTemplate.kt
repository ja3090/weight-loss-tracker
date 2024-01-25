package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_template")
data class MealTemplate(
    @PrimaryKey(autoGenerate = true) val mealTemplateId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float = 0f,
    @ColumnInfo(name = "total_protein") val totalProtein: Float = 0f,
    @ColumnInfo(name = "total_fat") val totalFat: Float = 0f,
    @ColumnInfo(name = "total_calories") val totalCalories: Float = 0f,
)
