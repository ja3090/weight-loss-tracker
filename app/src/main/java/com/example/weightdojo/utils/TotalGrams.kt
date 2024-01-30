package com.example.weightdojo.utils

import com.example.weightdojo.database.models.NutritionalValues

fun totalGrams(grams: Float, amountPer100: Float): Float {
    return (grams / 100) * amountPer100
}

data class Totals(
    var carbs: Float = 0f,
    var fat: Float = 0f,
    var protein: Float = 0f,
    var totalCals: Float = 0f
)

fun totals(ingredients: List<NutritionalValues>): Totals {

    val totals = Totals()

    for (ingredient in ingredients) {
        totals.carbs += totalGrams(ingredient.grams, ingredient.carbohydratesPer100)
        totals.protein += totalGrams(ingredient.grams, ingredient.proteinPer100)
        totals.fat += totalGrams(ingredient.grams, ingredient.fatPer100)
        totals.totalCals += totalGrams(ingredient.grams, ingredient.caloriesPer100)
    }

    return totals
}