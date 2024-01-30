package com.example.weightdojo.screens.home.addmodal.addcalories

class AddCaloriesValidation {
    fun validateSubmission(addCaloriesState: AddCaloriesState): ValidationResponse {
        when (addCaloriesState.mealState?.name) {
            null -> return ValidationResponse(errorMessage = "Name is null", success = false)
            "" -> return ValidationResponse(errorMessage = "Please provide a meal name", success = false)
        }

        if (addCaloriesState.ingredientList.isEmpty()) {
            return ValidationResponse(
                errorMessage = "Please add ingredients for this meal",
                success = false
            )
        }

        for (ingredient in addCaloriesState.ingredientList) {

            if (ingredient.name.isEmpty()) {
                return ValidationResponse(
                    errorMessage = "One of your ingredient name fields is empty",
                    success = false
                )
            }

            if (ingredient.grams == 0f) {
                return ValidationResponse(
                    errorMessage = "One of your ingredient grams fields is empty or zero. " +
                            "Either delete or fill this in",
                    success = false
                )
            }

            if (ingredient.caloriesPer100 == 0f) {
                return ValidationResponse(
                    errorMessage = "One of your ingredient calories fields is empty or zero. " +
                            "Either delete or fill this in",
                    success = false
                )
            }
        }

        return ValidationResponse(success = true)
    }
}

data class ValidationResponse(
    val errorMessage: String? = null,
    val success: Boolean
)