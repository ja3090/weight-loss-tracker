package com.example.weightdojo.screens.home.addmodal.addcalories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import java.util.UUID

data class AddCaloriesState(
    val mealState: MealState? = null,
    val ingredientList: List<IngredientState> = listOf(),
    val dayId: Long? = null,
    val currentSubModal: AddCalsSubModals = AddCalsSubModals.AddMealTemplate,
    val error: String? = null,
    val activeIngredientId: UUID? = null
)

enum class AddCalsSubModals {
    AddMealTemplate {
        override fun toString(): String {
            return "Choose Meal"
        }
    },
    MealCreation {
        override fun toString(): String {
            return "Create Meal"
        }
    },
    AddIngredientTemplate {
        override fun toString(): String {
            return "Add"
        }
    }
}

class AddCaloriesStateHandler(
    private val dayId: Long?
) {
    val templateConverter = ConvertTemplates()

    var state by mutableStateOf(AddCaloriesState(dayId = dayId))

    fun setErrorMessage(error: String?) {
        state = state.copy(error = error)
    }

    fun setActiveIngredient(ingredient: IngredientState) {
        state = if (ingredient.internalId == state.activeIngredientId) {
            state.copy(activeIngredientId = null)
        } else {
            state.copy(activeIngredientId = ingredient.internalId)
        }
    }

    fun addNewIngredient() {
        val updatedList = state.ingredientList + IngredientState()

        state = state.copy(ingredientList = updatedList, currentSubModal = AddCalsSubModals.MealCreation)
    }
    fun changeName(string: String) {
        val mealState = state.mealState ?: return

        state = state.copy(mealState = mealState.copy(name = string))
    }

    fun deleteIngredient(toDelete: IngredientState) {
        val markedFor = if (toDelete.markedFor == Marked.DELETE) Marked.EDIT else Marked.DELETE

        val updatedList = state.ingredientList.map {
            if (it === toDelete) {
                it.copy(markedFor = markedFor)
            } else it
        }

        state = state.copy(ingredientList = updatedList)
    }

    fun addIngredientToMeal(ingredientTemplate: IngredientTemplate) {
        state = state.copy(
            ingredientList = state.ingredientList + templateConverter.toIngredientState(
                ingredientTemplate
            ), currentSubModal = AddCalsSubModals.MealCreation
        )
    }

    fun moveToAddIngredient() {
        state = state.copy(currentSubModal = AddCalsSubModals.AddIngredientTemplate)
    }

    fun changeIngredient(ingredientState: IngredientState) {
        val updatedList = state.ingredientList.map {
            if (it.internalId == ingredientState.internalId) {
                ingredientState
            } else it
        }

        state = state.copy(ingredientList = updatedList)
    }

    fun initMealCreation(
        dayId: Long?,
    ) {
        val (meal, ingredientStateList) = templateConverter.makeMealStateAndIngredientList(
            dayId = dayId
        )

        state = state.copy(
            mealState = meal,
            ingredientList = ingredientStateList,
            currentSubModal = AddCalsSubModals.MealCreation
        )
    }

    fun moveToSubModal(subModal: AddCalsSubModals) {
        state = state.copy(currentSubModal = subModal)
    }

    fun setMealTemplate(mealState: MealState, ingredientList: List<IngredientState>) {
        state = state.copy(
            mealState = mealState,
            ingredientList = ingredientList,
            currentSubModal = AddCalsSubModals.MealCreation
        )
    }

    fun reset() {
        state = AddCaloriesState(dayId = dayId)
    }
}