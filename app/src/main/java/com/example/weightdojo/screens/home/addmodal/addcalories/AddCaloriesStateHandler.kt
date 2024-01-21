package com.example.weightdojo.screens.home.addmodal.addcalories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState

data class AddCaloriesState(
    val mealState: MealState? = null,
    val ingredientList: List<IngredientState> = listOf(),
    val dayId: Long? = null,
    val currentSubModal: SubModals = SubModals.AddMealTemplate,
    val overwriteTemplate: Boolean? = null
)

enum class SubModals {
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

    //    AddNewIngredient,
    AddIngredientTemplate {
        override fun toString(): String {
            return "Add Ingredient"
        }
    }
}

class AddCaloriesStateHandler(
    private val dayId: Long?
) {
    private val templateConverter = ConvertTemplates()

    var state by mutableStateOf(AddCaloriesState(dayId = dayId))

    fun addNewIngredient() {
        val updatedList = state.ingredientList + IngredientState()

        state = state.copy(ingredientList = updatedList, currentSubModal = SubModals.MealCreation)
    }

    fun changeName(string: String) {
        val mealState = state.mealState

        if (mealState == null) {
            Log.e(
                "createTemplateError",
                "overwriteTemplate is required and is null. " +
                        "User should be prompted to choose whether this template should be overwritten"
            )
            return
        }

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
            ), currentSubModal = SubModals.MealCreation
        )
    }

    fun moveToAddIngredient() {
        state = state.copy(currentSubModal = SubModals.AddIngredientTemplate)
    }

    fun changeIngredient(grams: Float, ingredientState: IngredientState) {
        val updatedList = state.ingredientList.map {
            if (it == ingredientState) {
                it.copy(grams = grams)
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
            currentSubModal = SubModals.MealCreation
        )
    }

    fun moveToSubModal(subModal: SubModals) {
        state = state.copy(currentSubModal = subModal)
    }

    fun setMealTemplate(mealState: MealState, ingredientList: List<IngredientState>) {
        state = state.copy(
            mealState = mealState,
            ingredientList = ingredientList,
            currentSubModal = SubModals.MealCreation
        )
    }
}