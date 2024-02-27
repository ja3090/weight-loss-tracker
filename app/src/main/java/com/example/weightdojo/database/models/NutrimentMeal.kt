package com.example.weightdojo.database.models

import androidx.room.Entity

@Entity(tableName = "nutriment_meal", primaryKeys = ["mealId", "nutrimentId"])
data class NutrimentMeal(
    val mealId: Long,
    val nutrimentId: Long,
    val totalGrams: Float
)
