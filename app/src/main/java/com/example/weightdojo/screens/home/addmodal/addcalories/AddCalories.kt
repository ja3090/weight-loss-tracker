package com.example.weightdojo.screens.home.addmodal.addcalories

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.ErrorDialog
import com.example.weightdojo.components.searchtemplates.SearchIngredientTemplatesVM
import com.example.weightdojo.components.searchtemplates.SearchMealTemplatesVM
import com.example.weightdojo.screens.home.addmodal.AddModalVm
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.screens.home.addmodal.addcalories.addnewmeal.AddNewMeal
import com.example.weightdojo.components.searchtemplates.SearchTemplate
import com.example.weightdojo.utils.VMFactory

@Composable
fun AddCalories(
    dayId: Long?,
    addCaloriesVm: AddCaloriesVm = viewModel(factory = VMFactory.build {
        AddCaloriesVm(database = MyApp.appModule.database, dayId = dayId)
    }),
    state: AddCaloriesState = addCaloriesVm.stateHandler.state,
    addModalVm: AddModalVm = viewModel()
) {
    ErrorDialog(
        onConfirm = { addCaloriesVm.stateHandler.setErrorMessage(null) },
        title = "Error",
        text = state.error
    )

    ModalFrame(title = state.currentSubModal.toString(), onBack = {
        when (state.currentSubModal) {
            AddCalsSubModals.AddMealTemplate -> addModalVm.backToInitial()
            AddCalsSubModals.AddIngredientTemplate -> addCaloriesVm.stateHandler.moveToSubModal(
                AddCalsSubModals.MealCreation
            )

            AddCalsSubModals.MealCreation -> addCaloriesVm.stateHandler.moveToSubModal(
                AddCalsSubModals.AddMealTemplate
            )
        }
    }) {
        when (state.currentSubModal) {
            AddCalsSubModals.AddMealTemplate -> SearchTemplate(
                searchTemplatesVm = SearchMealTemplatesVM(),
                onUseClick = addCaloriesVm::moveToAddNewMeal,
                viewModel = addCaloriesVm,
                per100 = false
            )

            AddCalsSubModals.MealCreation -> AddNewMeal()
            AddCalsSubModals.AddIngredientTemplate -> SearchTemplate(
                searchTemplatesVm = SearchIngredientTemplatesVM(),
                onUseClick = addCaloriesVm::addIngredientToMeal,
                viewModel = addCaloriesVm,
                per100 = true
            )
        }
    }
}