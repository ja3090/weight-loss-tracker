package com.example.weightdojo.components.mealcreation

import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment

data class ValidationResponse(
    val success: Boolean,
    val message: String
)

class MealCreationValidation {
    fun validate(state: MealCreationState): ValidationResponse {
        if (state.singleMealDetailed.mealName.isEmpty()) {
            return ValidationResponse(
                success = false,
                message = "Please enter a meal name"
            )
        }

        val nutriments = mutableListOf<SingleMealDetailedNutriment>()

        if (state.singleMealDetailed.ingredients.isEmpty()) {
            return ValidationResponse(
                success = false,
                message = "Cannot submit a meal without ingredients"
            )
        }

        for (ingredient in state.singleMealDetailed.ingredients) {
            if (ingredient.markedFor == Marked.DELETE) continue

            if (ingredient.caloriesPer100 == 0f) {
                return ValidationResponse(
                    success = false,
                    message = "One of your ingredients is missing a calories per 100g value"
                )
            }
            if (ingredient.grams == 0f) {
                return ValidationResponse(
                    success = false,
                    message = "One of your ingredients is missing a grams value"
                )
            }
            if (ingredient.ingredientName.isEmpty()) {
                return ValidationResponse(
                    success = false,
                    message = "One of your ingredients is missing a name"
                )
            }
            nutriments.addAll(nutriments.size, ingredient.nutriments)
        }

        for (nutriment in nutriments) {
            if (nutriment.gPer100AsString == "") {
                return ValidationResponse(
                    success = false,
                    message = "One of your nutriment fields is missing a per 100 grams value"
                )
            }
        }

        return ValidationResponse(success = true, message = "Success")
    }
}