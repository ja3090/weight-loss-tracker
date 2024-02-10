package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weightdojo.utils.totalGrams

@Entity(tableName = "ingredient_template")
data class IngredientTemplate(
    @PrimaryKey(autoGenerate = true) val ingredientTemplateId: Long = 0,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "carbohydrates_per_100g") val carbohydratesPer100: Float = 0f,
    @ColumnInfo(name = "protein_per_100g") val proteinPer100: Float = 0f,
    @ColumnInfo(name = "fat_per_100g") val fatPer100: Float = 0f,
    @ColumnInfo(name = "calories_per_100g") val caloriesPer100: Float,
    @ColumnInfo(name = "grams") val grams: Float,
    @ColumnInfo(name = "soft_delete") val softDelete: Boolean = false,
    override val protein: Float = proteinPer100,
    override val carbs: Float = carbohydratesPer100,
    override val cals: Float = totalGrams(grams, caloriesPer100),
    override val fat: Float = fatPer100,
) : Searchable
