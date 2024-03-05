package com.example.weightdojo.database.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "meal_ingredient",
    primaryKeys = ["mealId", "ingredientId"],
//    foreignKeys = [
//        ForeignKey(
//            entity = Meal::class,
//            parentColumns = ["mealId"],
//            childColumns = ["mealId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class MealIngredient(
    val mealId: Long,
    val ingredientId: Long
)
