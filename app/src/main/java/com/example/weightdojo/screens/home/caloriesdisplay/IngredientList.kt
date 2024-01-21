package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.IngredientAsInput
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.totalGrams

@Composable
fun IngredientList(
    weightUnit: String,
    mealListVM: MealListVM = viewModel(),
    state: MealListState = mealListVM.state,
) {

    Column(
        modifier = Modifier
            .padding(bottom = Sizing.paddings.medium)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = Sizing.cornerRounding,
                    bottomStart = Sizing.cornerRounding
                )
            )
            .background(MaterialTheme.colors.secondary)
    ) {

        if (state.isEditing) {
            state.ingredientListAsState?.map { ingredientAsState ->
                IngredientAsInput(
                    ingredientState = ingredientAsState,
                    weightUnit = weightUnit,
                    onValueChange = {
                        val passes = it.isEmpty() || it.isDigitsOnly()

                        if (passes) {
                            mealListVM.changeGrams(
                                ingredientAsState.ingredientId,
                                if (it.isEmpty()) 0f else it.toFloat()
                            )
                        }
                    },
                    onConfirmDelete = mealListVM::deleteIngredient
                )
            }
        } else {
            state.ingredientList?.map {
                Ingredient(
                    name = it.name,
                    weightUnit = weightUnit,
                    totalCalories = totalGrams(it.grams, it.caloriesPer100)
                )
            }
        }

        if (!state.isEditing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconBuilder(
                    id = R.drawable.edit,
                    contentDescription = "edit",
                    testTag = "EDIT_BUTTON",
                    modifier = Modifier
                        .clickable { mealListVM.showIngredientListAsState() }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
                IconBuilder(
                    id = R.drawable.delete,
                    contentDescription = "delete",
                    testTag = "DELETE_BUTTON",
                    modifier = Modifier
                        .clickable { }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Sizing.paddings.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(buttonName = "Save") {
                    mealListVM.makeEdits()
                }
                TextDefault(text = "Cancel", modifier = Modifier
                    .padding(
                        vertical = Sizing.paddings.small,
                        horizontal = Sizing.paddings.medium
                    )
                    .clickable { mealListVM.removeActive() })
            }
        }
    }
}