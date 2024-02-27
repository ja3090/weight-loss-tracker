package com.example.weightdojo.utils

import com.example.weightdojo.database.models.Ingredient

fun totalGrams(grams: Float, amountPer100: Float): Float {
    return (grams / 100) * amountPer100
}

data class Totals(
    var totalCals: Float = 0f
)

fun totals(ingredients: List<Ingredient>): Totals {

    val totals = Totals()

    for (ingredient in ingredients) {
        totals.totalCals += totalGrams(ingredient.grams, ingredient.caloriesPer100)
    }

    return totals
}