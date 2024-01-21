package com.example.weightdojo.screens.home.addmodal.addcalories

import android.net.wifi.WifiManager.AddNetworkResult
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.screens.home.addmodal.addcalories.addnewmeal.AddNewMeal
import com.example.weightdojo.screens.home.addmodal.addcalories.searchingredienttemplates.SearchIngredientTemplates
import com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates.SearchMealTemplates
import com.example.weightdojo.utils.VMFactory

@Composable
fun AddCalories(
    dayId: Long?,
    addCaloriesVm: AddCaloriesVm = viewModel(factory = VMFactory.build {
        AddCaloriesVm(database = MyApp.appModule.database, dayId = dayId)
    }),
    state: AddCaloriesState = addCaloriesVm.stateHandler.state
) {

    ModalFrame(title = state.currentSubModal.toString()) {
        when (state.currentSubModal) {
            SubModals.AddMealTemplate -> SearchMealTemplates()
            SubModals.MealCreation -> AddNewMeal()
            SubModals.AddIngredientTemplate -> SearchIngredientTemplates()
        }
    }
}