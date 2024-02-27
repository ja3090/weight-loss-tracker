package com.example.weightdojo.components.mealcreation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.validateInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

data class MealCreationState(
    val singleMealDetailed: SingleMealDetailed,
    val activeIngredient: SingleMealDetailedIngredient? = null,
    val errorMessage: String? = null
)

class MealCreationStateHandler(
    singleMealDetailed: SingleMealDetailed?
) {
    var state by mutableStateOf(
        MealCreationState(
            singleMealDetailed = singleMealDetailed ?: SingleMealDetailed()
        )
    )

    fun setError(message: String?) {
        state = state.copy(errorMessage = message)
    }

    fun reset(singleMealDetailed: SingleMealDetailed? = null) {
        state = state.copy(
            singleMealDetailed = singleMealDetailed ?: SingleMealDetailed(),
            activeIngredient = null
        )
    }

    fun changeMealName(newName: String) {
        val updated = state.singleMealDetailed.copy(mealName = newName)

        state = state.copy(singleMealDetailed = updated)
    }

    fun changeIngredientName(newName: String, uuid: UUID) {
        changeIngredients(uuid) {
            if (uuid == it.internalId) {
                it.copy(ingredientName = newName)
            } else it
        }
    }

    fun setActiveIngredient(newActive: SingleMealDetailedIngredient) {
        val active = if (newActive == state.activeIngredient) null else newActive

        state = state.copy(activeIngredient = active)
    }

    private fun changeIngredients(
        uuid: UUID,
        mapper: (SingleMealDetailedIngredient) -> SingleMealDetailedIngredient,
    ) {
        val updatedList = state.singleMealDetailed.ingredients.map {
            if (it.internalId == uuid) mapper(it)
            else it
        }.toMutableList()

        val updated = state.singleMealDetailed.copy(ingredients = updatedList)

        state = state.copy(singleMealDetailed = updated)
    }

    fun changeCaloriesPer100(newValue: String, uuid: UUID) {
        val valid = newValue.isDigitsOnly() || newValue.isEmpty()

        if (!valid) return

        val convertedValue = if (newValue == "") 0f else newValue.toFloat()

        changeIngredients(uuid) {
            it.copy(caloriesPer100 = convertedValue)
        }
    }

    fun changeNutriment(newValue: String, nutrimentUuid: UUID, ingredientUuid: UUID) {
        val valid = validateInput(newValue)

        if (!valid) return

        changeIngredients(ingredientUuid) {
            it.copy(nutriments = updateNutriments(
                it, nutrimentUuid
            ) { nut -> nut.copy(gPer100AsString = newValue) })
        }
    }

    fun changeIngredientGrams(newValue: String, ingredientUuid: UUID) {
        val valid = newValue.isDigitsOnly() || newValue.isEmpty()

        if (!valid) return

        val convertedValue = if (newValue == "") 0f else newValue.toFloat()

        changeIngredients(ingredientUuid) {
            it.copy(grams = convertedValue)
        }
    }

    fun removeDecimalIfIncorrectlyPlaced(
        currentValue: String,
        nutrimentUuid: UUID,
        ingredientUuid: UUID
    ) {
        val incorrectlyPlaced = currentValue.isNotEmpty() && currentValue.last() == '.'

        if (incorrectlyPlaced) {
            val finalValue = currentValue.dropLast(1)

            changeIngredients(ingredientUuid) {
                val updatedNutriments = updateNutriments(it, nutrimentUuid) { nutr ->
                    nutr.copy(gPer100AsString = finalValue)
                }

                it.copy(nutriments = updatedNutriments)
            }
        }
    }

    private fun updateNutriments(
        ingredient: SingleMealDetailedIngredient,
        nutrimentUuid: UUID,
        newState: (SingleMealDetailedNutriment) -> SingleMealDetailedNutriment
    ): MutableList<SingleMealDetailedNutriment> {
        return ingredient.nutriments.map {
            if (it.internalId == nutrimentUuid) {
                newState(it)
            } else it
        }.toMutableList()
    }
}