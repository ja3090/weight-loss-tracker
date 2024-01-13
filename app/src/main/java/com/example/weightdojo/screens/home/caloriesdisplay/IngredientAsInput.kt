package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.ui.Sizing

@Composable
fun IngredientAsInput(
    ingredientsAsState: IngredientState,
    weightUnit: String,
    setter: (ingredientId: Long, newState: IngredientState) -> Unit
) {
    val fontSize = Sizing.font.default * 0.8

    var confirmDelete by remember {
        mutableStateOf(false)
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            onConfirmation = {
                val newState = if (ingredientsAsState.markedFor == Marked.EDIT) {
                    ingredientsAsState.copy(
                        markedFor = Marked.DELETE
                    )
                } else {
                    ingredientsAsState.copy(
                        markedFor = Marked.EDIT
                    )
                }

                setter(
                    ingredientsAsState.ingredientId,
                    newState
                )

                confirmDelete = false
            },
            dialogTitle = if (ingredientsAsState.markedFor == Marked.EDIT) {
                "Mark for Deletion"
            } else "Undo Mark for Deletion",
            dialogText = if (ingredientsAsState.markedFor == Marked.EDIT) {
                "Do you want to mark this for deletion?"
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
            totalCalories = (ingredientsAsState.grams / 100) * ingredientsAsState.caloriesPer100,
            name = ingredientsAsState.name,
            weightUnit = weightUnit,
            markedForDeletion = ingredientsAsState.markedFor == Marked.DELETE
        )
        Row(
            modifier = Modifier
                .padding(horizontal = Sizing.paddings.medium)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDefault(text = "KCAL per 100g", fontSize = fontSize)
            TextDefault(
                text = "${ingredientsAsState.caloriesPer100.toInt()}",
                fontSize = Sizing.font.default * 0.8
            )
        }

        if (ingredientsAsState.markedFor == Marked.DELETE) return

        Row(
            modifier = Modifier
                .padding(vertical = Sizing.paddings.extraSmall, horizontal = Sizing.paddings.medium)
                .heightIn(min = fontSize.value.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                modifier = Modifier.weight(0.75f),
                value = if (ingredientsAsState.grams == 0f) "" else {
                    "${ingredientsAsState.grams.toInt()}"
                },
                onValueChange = {
                    val passes = it.isEmpty() || it.isDigitsOnly()

                    if (passes) {
                        setter(
                            ingredientsAsState.ingredientId,
                            ingredientsAsState.copy(
                                grams = if (it.isEmpty()) 0f else it.toFloat()
                            )
                        )
                    }
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