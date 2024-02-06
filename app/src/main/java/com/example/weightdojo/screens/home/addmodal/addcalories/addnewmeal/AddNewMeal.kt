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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.addingredients.AddIngredient
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesState
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesStateHandler
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesVm
import com.example.weightdojo.components.inputs.IngredientAsInput
import com.example.weightdojo.components.successToast
import com.example.weightdojo.screens.main.MainViewModel
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
    homeViewModel: HomeViewModel = viewModel(),
) {
    val context = LocalContext.current

    fun submitHandler(
        dbFunction: suspend () -> Boolean,
        onSuccess: () -> Unit,
    ) {
        addCaloriesVm.viewModelScope.launch {
            val success = dbFunction()

            if (success) onSuccess()
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
                .weight(0.7f)
                .verticalScroll(rememberScrollState())
        ) {
            state.ingredientList.map { ingredientState ->
                IngredientAsInput(
                    ingredientState = ingredientState,
                    onConfirmDelete = stateHandler::deleteIngredient,
                    onValueChange = {
                        stateHandler.changeIngredient(it)
                    },
                    activeIngredientId = stateHandler.state.activeIngredientId,
                    setActiveIngredient = stateHandler::setActiveIngredient
                )
            }
            AddIngredient {
                stateHandler.moveToAddIngredient()
            }
        }
        Column(
            modifier = Modifier
                .weight(0.3f)
                .padding(vertical = Sizing.paddings.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.mealState?.mealTemplateId != 0L) {
                TextDefault(
                    text = "Overwrite",
                    modifier = Modifier.clickable {
                        confirmOverwrite = true
                    },
                    fontSize = Sizing.font.small
                )
            } else if (state.mealState.mealTemplateId == 0L) {
                TextDefault(
                    text = "Create Template",
                    modifier = Modifier
                        .clickable {
                            submitHandler(
                                addCaloriesVm::createTemplate,
                            ) {
                                successToast("Success", context)
                            }
                        },
                    fontSize = Sizing.font.small
                )
            }
            CustomButton(buttonName = "Save") {
                submitHandler(addCaloriesVm::submitMeal) {
                    addCaloriesVm.stateHandler.reset()
                    homeViewModel.refresh()
                    homeViewModel.showModal(false)
                    successToast("Success", context)
                }
            }
        }
    }

    if (confirmOverwrite) {
        AlertDialog(
            onDismissRequest = { confirmOverwrite = false },
            onReject = {
                submitHandler(addCaloriesVm::createTemplate) {
                    confirmOverwrite = false
                    successToast("Success", context)
                }
            },
            onConfirmation = {
                submitHandler(addCaloriesVm::overwriteTemplate) {
                    confirmOverwrite = false
                    successToast("Success", context)
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
