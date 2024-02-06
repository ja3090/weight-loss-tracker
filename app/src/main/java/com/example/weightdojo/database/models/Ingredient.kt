package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "ingredient",
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["meal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "meal_id") val mealId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrates_per_100g") override val carbohydratesPer100: Float = 0f,
    @ColumnInfo(name = "protein_per_100g") override val proteinPer100: Float = 0f,
    @ColumnInfo(name = "fat_per_100g") override val fatPer100: Float = 0f,
    @ColumnInfo(name = "calories_per_100g") override val caloriesPer100: Float,
    @ColumnInfo(name = "grams") override val grams: Float,
) : NutritionalValues

interface NutritionalValues {
    val carbohydratesPer100: Float
    val proteinPer100: Float
    val fatPer100: Float
    val caloriesPer100: Float
    val grams: Float
}