package com.example.weightdojo.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.screens.home.caloriesdisplay.Ingredient
import com.example.weightdojo.ui.Sizing


@Composable
fun IngredientAsInput(
    ingredientState: IngredientState,
    weightUnit: String,
    onConfirmDelete: (newState: IngredientState) -> Unit,
    onValueChange: (new: String) -> Unit
) {
    val fontSize = Sizing.font.default * 0.8

    var confirmDelete by remember {
        mutableStateOf(false)
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            onConfirmation = {
                onConfirmDelete(ingredientState)
                confirmDelete = false
            },
            dialogTitle = if (ingredientState.markedFor == Marked.EDIT) {
                "Delete"
            } else "Undo Delete",
            dialogText = if (ingredientState.markedFor == Marked.EDIT) {
                "Do you want to delete this?"
            } else "Do you want to keep this?"
        )
    }

    Column(
        modifier = Modifier
            .clickable { }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        confirmDelete = true
                    }
                )
            }
    ) {

        Ingredient(
            totalCalories = (ingredientState.grams / 100) * ingredientState.caloriesPer100,
            name = ingredientState.name,
            weightUnit = weightUnit,
            markedForDeletion = ingredientState.markedFor == Marked.DELETE
        )

        if (ingredientState.markedFor == Marked.DELETE) return

        Row(
            modifier = Modifier
                .padding(horizontal = Sizing.paddings.medium)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDefault(text = "KCAL per 100g", fontSize = fontSize)
            TextDefault(
                text = "${ingredientState.caloriesPer100.toInt()}",
                fontSize = Sizing.font.default * 0.8
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = Sizing.paddings.extraSmall, horizontal = Sizing.paddings.medium)
                .heightIn(min = fontSize.value.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                modifier = Modifier.weight(0.75f),
                value = if (ingredientState.grams == 0f) "" else {
                    "${ingredientState.grams}"
                },
                onValueChange = {
                    onValueChange(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = fontSize,
                    color = MaterialTheme.colors.primary
                ),
                cursorBrush = SolidColor(MaterialTheme.colors.primary)
            )
            TextDefault(
                text = "G",
                modifier = Modifier.weight(0.25f),
                textAlign = TextAlign.Right
            )
        }
        CustomDivider(
            tinted = true,
            modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )
    }
}