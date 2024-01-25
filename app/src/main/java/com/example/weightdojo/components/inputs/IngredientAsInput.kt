package com.example.weightdojo.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.weightdojo.R
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
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
    onValueChange: (ingredientState: IngredientState) -> Unit,
) {
    var confirmDelete by remember {
        mutableStateOf(false)
    }

    var showDetailedList by remember {
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
                "Mark for delete?"
            } else "Do you want to keep this?",
        )
    }

    Column {
        NameField(
            value = ingredientState.name,
            onValueChange = {
                val newState = ingredientState.copy(name = it)

                onValueChange(newState)
            },
            modifier = Modifier
                .clickable { showDetailedList = !showDetailedList },
            showDetailedList = showDetailedList,
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        if (ingredientState.markedFor == Marked.DELETE) return

        CalorieBreakdown(
            calorieInfo = ingredientState.caloriesPer100,
            nutrimentName = "KCAL",
            setter = {
                val newState = ingredientState.copy(caloriesPer100 = it)

                onValueChange(newState)
            },
            per100 = true,
            placeholderText = "Calories"
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        if (showDetailedList) {
            CalorieBreakdown(
                calorieInfo = ingredientState.proteinPer100,
                nutrimentName = "Protein",
                setter = {
                    val newState = ingredientState.copy(proteinPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Protein"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )

            CalorieBreakdown(
                calorieInfo = ingredientState.fatPer100,
                nutrimentName = "Fat",
                setter = {
                    val newState = ingredientState.copy(fatPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Fat"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )

            CalorieBreakdown(
                calorieInfo = ingredientState.carbsPer100,
                nutrimentName = "Carbs",
                setter = {
                    val newState = ingredientState.copy(carbsPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Carbs"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )
        }

        CalorieBreakdown(
            calorieInfo = ingredientState.grams,
            setter = {
                val newState = ingredientState.copy(grams = it)

                onValueChange(newState)
            },
            nutrimentName = "G",
            placeholderText = "Grams"
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        if (showDetailedList) {
            IconBuilder(
                id = R.drawable.delete,
                contentDescription = "Delete ingredient",
                testTag = "DELETE_INGREDIENT",
                modifier = Modifier
                    .padding(vertical = Sizing.paddings.small, horizontal = Sizing.paddings.medium)
                    .fillMaxWidth()
                    .clickable { confirmDelete = true },
            )
        }
    }
}