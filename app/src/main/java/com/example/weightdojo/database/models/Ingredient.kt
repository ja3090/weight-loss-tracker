package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ingredient")
data class Ingredient (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrates_per_100g") val carbohydratesPer100: Float?,
    @ColumnInfo(name = "protein_per_100g") val proteinPer100: Float?,
    @ColumnInfo(name = "fat_per_100g") val fatPer100: Float?,
    @ColumnInfo(name = "calories_per_100g") val caloriesPer100: Float,
)