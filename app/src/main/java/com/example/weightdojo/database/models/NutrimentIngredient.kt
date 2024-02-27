package com.example.weightdojo.database.models

import androidx.room.Entity

@Entity(tableName = "nutriment_ingredient", primaryKeys = ["nutrimentId", "ingredientId"])
data class NutrimentIngredient(
    val nutrimentId: Long, val ingredientId: Long, val gPer100: Float
)
