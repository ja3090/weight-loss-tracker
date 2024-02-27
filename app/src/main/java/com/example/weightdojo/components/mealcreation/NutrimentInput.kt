package com.example.weightdojo.components.mealcreation

import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.inputs.Field
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing
import java.util.UUID

@Composable
fun NutrimentInput(
    ingredientUUID: UUID,
    nutriment: SingleMealDetailedNutriment,
    mealCreationVM: MealCreationVM = viewModel()
) {
    var isFocused by remember { mutableStateOf(false) }

    Field(
        focusOptions = { focusState ->
            isFocused = focusState.isFocused

            if (!focusState.isFocused) {
                mealCreationVM.stateHandler.removeDecimalIfIncorrectlyPlaced(
                    currentValue = nutriment.gPer100AsString,
                    nutrimentUuid = nutriment.internalId,
                    ingredientUuid = ingredientUUID
                )
            }
        },
        value = nutriment.gPer100AsString,
        onValueChange = {
            mealCreationVM.stateHandler.changeNutriment(
                newValue = it,
                nutrimentUuid = nutriment.internalId,
                ingredientUuid = ingredientUUID
            )
        },
        placeholder = "Grams*",
        trailingIcon = {
            TextDefault(
                text = "${nutriment.nutrimentName} g",
                fontSize = Sizing.font.small,
                color = it
            )
        },
        fontSize = Sizing.font.small,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}