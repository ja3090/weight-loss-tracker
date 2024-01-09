package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Ingredient (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrates") val carbohydrates: Float?,
    @ColumnInfo(name = "protein") val protein: Float?,
    @ColumnInfo(name = "fat") val fat: Float?,
    @ColumnInfo(name = "calories") val calories: Float,
    @ColumnInfo(name = "meal_id") val mealId: Long
)