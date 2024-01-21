package com.example.weightdojo.screens.home.addmodal.addcalories.addnewmeal

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesState
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesStateHandler
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesVm
import com.example.weightdojo.components.inputs.IngredientAsInput
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.validateInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddNewMeal(
    addCaloriesVm: AddCaloriesVm = viewModel(),
    state: AddCaloriesState = addCaloriesVm.stateHandler.state,
    stateHandler: AddCaloriesStateHandler= addCaloriesVm.stateHandler,
    homeViewModel: HomeViewModel = viewModel()
) {
    suspend fun returnToHome() {
        withContext(Dispatchers.Main) {
            homeViewModel.showModal(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Input(
            InputArgs(
                inputValue = state.mealState?.name ?: "",
                onValueChange = {
                    addCaloriesVm.stateHandler.changeName(it)
                },
                keyboardOptions = KeyboardOptions.Default,
                placeholder = { TextDefault(text = "Meal Name") },
                modifier = Modifier.fillMaxWidth()
            )
        )

        Stats(ingredientList = state.ingredientList)
        Column(
            modifier = Modifier
                .weight(0.85f)
                .verticalScroll(rememberScrollState())
        ) {
            state.ingredientList.map { ingredientState ->
                IngredientAsInput(
                    ingredientState = ingredientState,
                    weightUnit = "KCAL",
                    onConfirmDelete = stateHandler::deleteIngredient,
                    onValueChange = {
                        val passes = validateInput(it)

                        if (!passes) return@IngredientAsInput

                        val grams = if (it.isEmpty()) 0F else it.toFloat()

                        stateHandler.changeIngredient(grams, ingredientState)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextDefault(text = "Add Ingredient",
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(horizontal = Sizing.paddings.small)
                        .clickable { stateHandler.moveToAddIngredient() })
                IconBuilder(
                    id = R.drawable.add,
                    contentDescription = "add ingredient",
                    testTag = "ADD_INGREDIENT"
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextDefault(text = "Create Template", modifier = Modifier.clickable {
                CoroutineScope(Dispatchers.IO).launch {
                    val success = addCaloriesVm.createTemplate()

                    if (success) returnToHome()
                }
            })
            CustomButton(buttonName = "Save") {

            }
        }
    }
}
