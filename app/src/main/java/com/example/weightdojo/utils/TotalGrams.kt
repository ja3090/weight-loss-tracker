package com.example.weightdojo.utils

import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked

fun totalGrams(grams: Float, amountPer100: Float): Float {
    return (grams / 100) * amountPer100
}

data class Totals(
    var carbs: Float = 0f,
    var fat: Float = 0f,
    var protein: Float = 0f,
    var totalCals: Float = 0f
)

fun totals(ingredients: List<IngredientState>): Totals {

    val totals = Totals()

    for (ingredient in ingredients) {
        if (ingredient.markedFor == Marked.DELETE) continue
        totals.carbs += totalGrams(ingredient.grams, ingredient.carbsPer100)
        totals.protein += totalGrams(ingredient.grams, ingredient.proteinPer100)
        totals.fat += totalGrams(ingredient.grams, ingredient.fatPer100)
        totals.totalCals += totalGrams(ingredient.grams, ingredient.caloriesPer100)
    }

    return totals
}