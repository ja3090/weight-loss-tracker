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
import com.example.weightdojo.R
import com.example.weightdojo.components.SaveButton
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.CalorieEntryIngredients
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.ui.Sizing

@Composable
fun IngredientList(
    activeMeal: MealData,
    ingredientList: List<CalorieEntryIngredients>?,
    ingredientListAsState: List<IngredientState>?,
    weightUnit: String,
    isEditing: Boolean,
    showIngredientListAsState: (dayId: Long, mealId: Long) -> Unit,
    ingredientListSetter: (caloriesId: Long, newState: IngredientState) -> Unit,
    updateData: () -> Unit,
    closeList: () -> Unit
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

        if (isEditing) {
            ingredientListAsState?.map {
                IngredientAsInput(
                    ingredientsAsState = it,
                    weightUnit = weightUnit,
                    setter = ingredientListSetter
                )
            }
        } else {
            ingredientList?.map {
                Ingredient(
                    name = it.name,
                    weightUnit = weightUnit,
                    totalCalories = it.totalCalories
                )
            }
        }

        if (!isEditing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconBuilder(
                    id = R.drawable.edit,
                    contentDescription = "edit",
                    testTag = "EDIT_BUTTON",
                    modifier = Modifier
                        .clickable {
                            showIngredientListAsState(
                                activeMeal.dayId,
                                activeMeal.mealId
                            )
                        }
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
                SaveButton {
                    updateData()
                }
                TextDefault(
                    text = "Cancel",
                    modifier = Modifier
                    .padding(
                        vertical = Sizing.paddings.small,
                        horizontal = Sizing.paddings.medium
                    )
                    .clickable { closeList() }
                )
            }
        }
    }
}