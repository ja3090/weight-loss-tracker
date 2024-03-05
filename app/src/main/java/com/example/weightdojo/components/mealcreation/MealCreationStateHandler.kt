package com.example.weightdojo.components.mealcreation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.validateInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID


enum class SubScreens {
    CREATION,
    SEARCH_INGREDIENTS,
    SEARCH_MEALS,
}

data class MealCreationState(
    val singleMealDetailed: SingleMealDetailed,
    val activeIngredient: SingleMealDetailedIngredient? = null,
    val errorMessage: String? = null,
    val subScreens: SubScreens = SubScreens.CREATION
)

class MealCreationStateHandler(
    singleMealDetailed: SingleMealDetailed?,
    private val mealCreationOptions: MealCreationOptions
) {
    var state by mutableStateOf(
        MealCreationState(
            singleMealDetailed = singleMealDetailed ?: SingleMealDetailed(),
            subScreens = if (mealCreationOptions == MealCreationOptions.CREATING) {
                SubScreens.SEARCH_MEALS
            }  else {
                SubScreens.CREATION
            }
        )
    )

    fun addIngredientToMeal(ingredient: SingleMealDetailedIngredient) {
        val updatedList = state.singleMealDetailed.ingredients.plus(ingredient).toMutableList()
        val updatedMeal = state.singleMealDetailed.copy(ingredients = updatedList)

        state = state.copy(singleMealDetailed = updatedMeal, subScreens = SubScreens.CREATION)
    }

    fun removeIngredientFromList(ingredient: SingleMealDetailedIngredient) {
        changeIngredients(ingredient.internalId) {
            it.copy(markedFor = Marked.DELETE)
        }
    }

    fun setSubScreen(screen: SubScreens) {
        state = state.copy(subScreens = screen)
    }

    fun setError(message: String?) {
        state = state.copy(errorMessage = message)
    }

    fun reset(singleMealDetailed: SingleMealDetailed? = null, subScreen: SubScreens? = null) {
        state = state.copy(
            singleMealDetailed = singleMealDetailed ?: SingleMealDetailed(),
            activeIngredient = null,
            subScreens = subScreen ?: SubScreens.SEARCH_MEALS
        )
    }

    fun useTemplate(singleMealDetailed: SingleMealDetailed) {
        state = state.copy(
            singleMealDetailed = singleMealDetailed.copy(),
            activeIngredient = null,
            subScreens = SubScreens.CREATION,
        )
    }

    fun addNewMeal() {
        state = state.copy(
            singleMealDetailed = SingleMealDetailed(),
            activeIngredient = null,
            subScreens = SubScreens.CREATION
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

    /**
     * Provide a function to change a single ingredient specified with the uuid param.
     * Also handles the updating of the state
     */
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