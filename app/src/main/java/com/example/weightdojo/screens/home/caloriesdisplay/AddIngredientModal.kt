package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.searchtemplates.SearchTemplate
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates.SearchIngredientTemplatesVM

@Composable
fun AddIngredientModal(mealListVM: MealListVM = viewModel()) {
    Dialog(
        onDismissRequest = { mealListVM.openAddIngModal(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ModalFrame(title = "Add To Meal", onClose = { mealListVM.openAddIngModal(false) }) {

            SearchTemplate(
                searchTemplatesVm = SearchIngredientTemplatesVM(),
                onDelete = mealListVM::deleteIngredient,
                onUseClick = mealListVM::addIngredient,
                viewModel = mealListVM,
                per100 = true
            )
        }
    }

}