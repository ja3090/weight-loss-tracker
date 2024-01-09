package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "day_id") val dayId: Long,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float?,
    @ColumnInfo(name = "total_protein") val totalProtein: Float?,
    @ColumnInfo(name = "total_fat") val totalFat: Float?,
    @ColumnInfo(name = "total_calories") val totalCalories: Float,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(
        name = "has_ingredients",
        defaultValue = "false"
    ) val hasIngredients: Boolean = false,
)