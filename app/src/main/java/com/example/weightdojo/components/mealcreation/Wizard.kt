package com.example.weightdojo.components.mealcreation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.searchtemplates.SearchTemplates
import com.example.weightdojo.components.searchtemplates.Templates
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentData
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownIngredient
import com.example.weightdojo.utils.VMFactory

@Composable
fun Wizard(
    mealCreationVm: MealCreationVM = viewModel(),
    state: MealCreationState = mealCreationVm.stateHandler.state,
    openModal: (Boolean) -> Unit,
    mealCreationOptions: MealCreationOptions,
    submit: () -> Unit
) {
    when (state.subScreens) {
        SubScreens.CREATION -> CreationSubScreen(openModal = openModal, submit = submit)
        SubScreens.SEARCH_INGREDIENTS -> SearchTemplates<NutrimentBreakdownIngredient, IngredientWithNutrimentData>(
            template = Templates.INGREDIENT,
            onUseTemplate = { mealCreationVm.useTemplate(it.id) },
            onAddNew = { mealCreationVm.stateHandler.addNewIngredientToMeal() }
        )

        SubScreens.SEARCH_MEALS -> if (mealCreationOptions == MealCreationOptions.EDITING) {
            null
        } else {
            TextDefault(text = "Select Meal")
        }
    }
}