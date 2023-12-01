package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDateTime

@Entity
data class Meal (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float,
    @ColumnInfo(name = "total_protein") val totalProtein: Float,
    @ColumnInfo(name = "total_fat") val totalFat: Float,
    @ColumnInfo(name = "total_calories") val totalCalories: Float,
)