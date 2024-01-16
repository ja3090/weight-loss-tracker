package com.example.weightdojo.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "meal_ingredient", primaryKeys = ["mealTemplateId", "ingredientTemplateId"])
data class MealIngredientTemplate(
    val mealTemplateId: Long,
    val ingredientTemplateId: Long
)

data class MealTemplateWithIngredients(
    @Embedded val ingredients: List<IngredientTemplate>,
    @Relation(
        parentColumn = "mealTemplateId",
        entityColumn = "ingredientTemplateId",
        associateBy = Junction(MealIngredientTemplate::class)
    )
    val mealTemplate: MealTemplate
)