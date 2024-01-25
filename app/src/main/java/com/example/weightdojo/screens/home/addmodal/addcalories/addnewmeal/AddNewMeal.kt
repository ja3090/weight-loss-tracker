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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.AlertDialog
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
    stateHandler: AddCaloriesStateHandler = addCaloriesVm.stateHandler,
    homeViewModel: HomeViewModel = viewModel()
) {
    suspend fun returnToHome() {
        withContext(Dispatchers.Main) {
            homeViewModel.showModal(false)
        }
    }

    fun submitHandler() {
        addCaloriesVm.viewModelScope.launch {
            val success = addCaloriesVm.submitMeal()

            if (success) returnToHome()
        }
    }

    var confirmOverwrite by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Input(
            InputArgs(
                inputValue = state.mealState?.name ?: "",
                onValueChange = {
                    addCaloriesVm.stateHandler.changeName(it)
                },
                keyboardOptions = KeyboardOptions.Default,
                placeholder = {
                    TextDefault(
                        text = "Meal Name",
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default.copy(textAlign = TextAlign.Left)
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
                        stateHandler.changeIngredient(it)
                    },
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
            if (state.mealState?.mealTemplateId != 0L) {
                TextDefault(text = "Overwrite", modifier = Modifier.clickable {
                    confirmOverwrite = true

                    CoroutineScope(Dispatchers.IO).launch {
                        val success = addCaloriesVm.createTemplate()

                        if (success) Log.d("Success", "Successfully created template")
                    }
                })
            } else if (state.mealState.mealTemplateId == 0L) {
                TextDefault(text = "Create Template", modifier = Modifier.clickable {
                    CoroutineScope(Dispatchers.IO).launch {
                        val success = addCaloriesVm.createTemplate()

                        if (success) Log.d("Success", "Successfully created template")
                    }
                })
            }
            CustomButton(buttonName = "Save") {
                submitHandler()
            }
        }
    }

    if (confirmOverwrite) {
        AlertDialog(
            onDismissRequest = { confirmOverwrite = false },
            onReject = { submitHandler() },
            onConfirmation = {
                addCaloriesVm.viewModelScope.launch {
                    addCaloriesVm.overwriteTemplate()
                }
            },
            dialogTitle = "Overwrite Template",
            dialogText = "You've used a template to create this, " +
                    "do you want to overwrite it or create a new one?",
            confirmButtonText = "Overwrite",
            cancelButtonText = "Create New"
        )
    }
}
