package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "ingredient",
//    foreignKeys = [
//        ForeignKey(
//            entity = Meal::class,
//            parentColumns = ["mealId"],
//            childColumns = ["meal_id"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0,
    @ColumnInfo(name = "meal_id") val mealId: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "calories_per_100g") val caloriesPer100: Float,
    @ColumnInfo(name = "is_template", defaultValue = "0") val isTemplate: Boolean = false,
    @ColumnInfo(name = "grams") val grams: Float,
    @ColumnInfo(name = "is_soft_deleted") val isSoftDeleted: Boolean = false,
)