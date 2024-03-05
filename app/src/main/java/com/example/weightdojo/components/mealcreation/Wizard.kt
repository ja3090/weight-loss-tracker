package com.example.weightdojo.components.mealcreation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.searchtemplates.SearchTemplates
import com.example.weightdojo.components.searchtemplates.Templates
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownIngredient
import com.example.weightdojo.datatransferobjects.NutrimentBreakdownMeal
import com.example.weightdojo.utils.VMFactory
import java.util.UUID

@Composable
fun Wizard(
    mealCreationVm: MealCreationVM = viewModel(),
    state: MealCreationState = mealCreationVm.stateHandler.state,
    openModal: (Boolean) -> Unit,
    submit: () -> Unit
) {
    when (state.subScreens) {
        SubScreens.CREATION -> CreationSubScreen(openModal = openModal, submit = submit)
        SubScreens.SEARCH_INGREDIENTS -> SearchTemplates<NutrimentBreakdownIngredient, IngredientWithNutrimentData>(
            template = Templates.INGREDIENT,
            onUseTemplate = { mealCreationVm.useIngredientTemplate(it.id) },
            onAddNew = { mealCreationVm.addNewIngredientToMeal() },
            key = UUID.randomUUID().toString()
        )
        SubScreens.SEARCH_MEALS -> SearchTemplates<NutrimentBreakdownMeal, MealWithNutrimentData>(
            template = Templates.MEAL,
            onUseTemplate = { mealCreationVm.useMealTemplate(it.id) },
            onAddNew = { mealCreationVm.stateHandler.addNewMeal() },
            key = UUID.randomUUID().toString()
        )
    }
}