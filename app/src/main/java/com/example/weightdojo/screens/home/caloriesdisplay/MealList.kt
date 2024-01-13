package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory

@Composable
fun MealList(
    meals: List<MealData>?,
    weightUnit: String,
    mealListViewModel: MealListVM = viewModel(
        factory = VMFactory.build {
            MealListVM(MyApp.appModule.database)
        }
    )
) {

    val activeMeal = mealListViewModel.state.activeMeal

    LaunchedEffect(meals) {
        mealListViewModel.removeActive()
    }

    meals?.map {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { mealListViewModel.setActive(it) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(Sizing.paddings.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextDefault(
                    text = it.mealName,
                    color = MaterialTheme.colors.primaryVariant,
                    fontStyle = FontStyle.Italic
                )
            }
            TextDefault(
                modifier = Modifier.padding(Sizing.paddings.medium),
                text = "${(it.totalCalories ?: 0).toInt()} $weightUnit",
            )
        }

        if (activeMeal !== null && it.mealId == activeMeal.mealId) {
            IngredientList(
                ingredientList = mealListViewModel.state.ingredientList,
                weightUnit = weightUnit,
                isEditing = mealListViewModel.state.isEditing,
                activeMeal = activeMeal,
                ingredientListAsState = mealListViewModel.state.ingredientListAsState,
                showIngredientListAsState = mealListViewModel::showIngredientListAsState,
                ingredientListSetter = mealListViewModel::setIngredientsAsState,
                updateData = mealListViewModel::makeEdits,
                closeList = mealListViewModel::removeActive
            )
        }
    }
}
