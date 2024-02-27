package com.example.weightdojo.components.mealcreation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.dialogs.ErrorDialog
import com.example.weightdojo.components.inputs.Field
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.components.toast
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

fun getColour(isError: Boolean, color: Color): Color {
    return if (isError) CustomColors.Red.copy(0.5f)
    else color
}

@Composable
fun MealCreation(
    detailedMeal: SingleMealDetailed?,
    mealCreationOptions: MealCreationOptions = MealCreationOptions.CREATING,
    mealCreationVm: MealCreationVM = viewModel(
        factory = VMFactory.build {
            MealCreationVM(singleMealDetailed = detailedMeal)
        }
    ),
    state: MealCreationState = mealCreationVm.stateHandler.state,
    openModal: (Boolean) -> Unit,
    onSuccess: (() -> Unit)? = null
) {
    LaunchedEffect(detailedMeal) {
        mealCreationVm.stateHandler.reset(detailedMeal)
    }

    val context = LocalContext.current

    fun submit() {
        val valid = mealCreationVm.validator.validate(state)

        if (!valid.success) return mealCreationVm.stateHandler.setError(valid.message)

        mealCreationVm.viewModelScope.launch(Dispatchers.IO) {
            val res = when (mealCreationOptions) {
                MealCreationOptions.CREATING -> mealCreationVm.submitMeal()
                MealCreationOptions.EDITING -> mealCreationVm.submitEdit()
            }

            if (res.success) {
                toast("Success", context)
                if (onSuccess != null) onSuccess()
                openModal(false)
            }
            else withContext(Dispatchers.Main) {
                mealCreationVm.stateHandler.setError(res.errorMessage)
            }
        }
    }

    ErrorDialog(
        onConfirm = { mealCreationVm.stateHandler.setError(null) },
        title = "Error",
        text = mealCreationVm.stateHandler.state.errorMessage
    )

    if (detailedMeal != null) Dialog(
        onDismissRequest = { openModal(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ModalFrame(
            title = "Enter Meal",
            onClose = { openModal(false) }
        ) {
            Field(
                value = state.singleMealDetailed.mealName,
                onValueChange = { mealCreationVm.stateHandler.changeMealName(it) },
                placeholder = "Meal Name",
                trailingIcon = {},
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            CustomDivider()
            TextDefault(
                textAlign = TextAlign.Center,
                text = "Values per 100g*",
                color = MaterialTheme.colors.primary.copy(0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Sizing.paddings.small),
                fontSize = Sizing.font.small
            )
            state.singleMealDetailed.ingredients.map {
                IngredientInput(ingredient = it)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(buttonName = "Save") {
                    submit()
                }
                TextDefault(
                    text = "Cancel",
                    fontSize = Sizing.font.small,
                    modifier = Modifier
                        .padding(Sizing.paddings.small)
                        .clickable { openModal(false) }
                )
            }
        }
    }
}