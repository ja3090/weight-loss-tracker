package com.example.weightdojo.screens.home.addmodal.addcalories

class AddCaloriesValidation {
    val errorMessages = ErrorMessages()
    fun validateSubmission(addCaloriesState: AddCaloriesState): ValidationResponse {
        when (addCaloriesState.mealState?.name) {
            null -> return ValidationResponse(errorMessage = errorMessages.mealStateNull, success = false)
            "" -> return ValidationResponse(errorMessage = errorMessages.noMealNameProvided, success = false)
        }

        if (addCaloriesState.ingredientList.isEmpty()) {
            return ValidationResponse(
                errorMessage = errorMessages.emptyIngredientList,
                success = false
            )
        }

        for (ingredient in addCaloriesState.ingredientList) {

            if (ingredient.name.isEmpty()) {
                return ValidationResponse(
                    errorMessage = errorMessages.noIngredientNameGiven,
                    success = false
                )
            }

            if (ingredient.grams == 0f) {
                return ValidationResponse(
                    errorMessage = errorMessages.gramsFieldEmpty,
                    success = false
                )
            }

            if (ingredient.caloriesPer100 == 0f) {
                return ValidationResponse(
                    errorMessage = errorMessages.caloriesFieldEmpty,
                    success = false
                )
            }
        }

        return ValidationResponse(success = true)
    }
}

data class ErrorMessages(
    val mealStateNull: String = "mealState is null",
    val noMealNameProvided: String = "Please provide a meal name",
    val emptyIngredientList: String = "Please add ingredients for this meal",
    val noIngredientNameGiven: String = "One of your ingredient name fields is empty",
    val gramsFieldEmpty: String = "One of your ingredient grams fields is empty or zero. " +
            "Either delete or fill this in",
    val caloriesFieldEmpty: String = "One of your ingredient calories fields is empty or zero. " +
            "Either delete or fill this in"
)

data class ValidationResponse(
    val errorMessage: String? = null,
    val success: Boolean
)