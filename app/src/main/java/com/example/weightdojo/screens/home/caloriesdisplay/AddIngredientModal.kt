package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.addingredients.searchingredienttemplates.SearchIngredientTemplates
import com.example.weightdojo.screens.home.addmodal.ModalFrame

@Composable
fun AddIngredientModal(mealListVM: MealListVM = viewModel()) {
    Dialog(
        onDismissRequest = { mealListVM.openAddIngModal(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ModalFrame(title = "Add To Meal", onClose = { mealListVM.openAddIngModal(false) }) {

            SearchIngredientTemplates(
                onAddNew = {
                    mealListVM.addNewIngredient()
                    mealListVM.openAddIngModal(false)
                },
                useTemplate = {
                    mealListVM.addIngredient(it)
                    mealListVM.openAddIngModal(false)
                },
                isOpen = mealListVM.state.addIngredientModalOpen
            )
        }
    }

}