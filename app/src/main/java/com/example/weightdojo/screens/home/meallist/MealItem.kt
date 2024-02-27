package com.example.weightdojo.screens.home.meallist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.ConfirmModal
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.toTwoDecimalPlaces

@Composable
fun MealItem(
    index: Int,
    meal: MealWithNutrimentData,
    activeMeal: MealWithNutrimentData?,
    setter: (meal: MealWithNutrimentData) -> Unit,
    arrSize: Int,
    calorieUnit: String = MyApp.appModule.calorieUnit.name,
    onEditButtonClick: (Long) -> Unit
) {
    val isActive = activeMeal !== null && activeMeal.mealId == meal.mealId

    val confirmDelete = ConfirmModal(
        title = "Delete",
        text = "Are you sure you want to delete this?"
    ) {}

    confirmDelete.Modal()

    Column(modifier = Modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .clickable { setter(meal) }
                .padding(vertical = Sizing.paddings.medium, horizontal = Sizing.paddings.small)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isActive) TextDefault(
                text = meal.mealName,
                color = MaterialTheme.colors.primaryVariant
            )
            else TextDefault(text = meal.mealName)
            TextDefault(text = "${toTwoDecimalPlaces(meal.totalCalories)} $calorieUnit")
        }
        if (index != arrSize - 1) {
            CustomDivider(
                modifier = Modifier.padding(horizontal = Sizing.paddings.small)
            )
        }

        if (isActive) {
            NutrimentData(nutriments = meal.nutriments)

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
                        .clickable { onEditButtonClick(meal.mealId) }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
                IconBuilder(
                    id = R.drawable.delete,
                    contentDescription = "delete",
                    testTag = "DELETE_BUTTON",
                    modifier = Modifier
                        .clickable { confirmDelete.openOrClose(true) }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
                IconBuilder(
                    id = R.drawable.favorite,
                    contentDescription = "favorite",
                    testTag = "FAVORITE_BUTTON",
                    modifier = Modifier
                        .clickable { }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
            }
            CustomDivider(modifier = Modifier.padding(horizontal = Sizing.paddings.small))
        }
    }
}