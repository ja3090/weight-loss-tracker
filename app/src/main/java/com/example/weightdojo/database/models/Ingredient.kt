package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ingredient")
data class Ingredient (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "meal_id") val mealId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrates_per_100g") val carbohydratesPer100: Float = 0f,
    @ColumnInfo(name = "protein_per_100g") val proteinPer100: Float = 0f,
    @ColumnInfo(name = "fat_per_100g") val fatPer100: Float = 0f,
    @ColumnInfo(name = "calories_per_100g") val caloriesPer100: Float,
    @ColumnInfo(name = "grams") val grams: Float,
)