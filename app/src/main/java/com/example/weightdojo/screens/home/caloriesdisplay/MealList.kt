package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.Totals
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.totals

@Composable
fun MealList(
    meals: List<MealData>?,
    mealListViewModel: MealListVM = viewModel(
        factory = VMFactory.build {
            MealListVM(MyApp.appModule.database)
        }
    ),
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    calorieUnit: CalorieUnits = config?.calorieUnit ?: AppConfig.internalDefaultCalorieUnit
) {

    val activeMeal = mealListViewModel.state.activeMeal

    LaunchedEffect(meals) {
        mealListViewModel.removeActive()
    }

    val totals = totals(mealListViewModel.state.ingredientList ?: listOf())

    meals?.map {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (mealListViewModel.state.isEditing) {
                        return@clickable
                    } else {
                        mealListViewModel.setActive(it)
                    }
                }
                .padding(Sizing.paddings.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextDefault(
                    text = it.mealName,
                    color = MaterialTheme.colors.primaryVariant,
                    fontStyle = FontStyle.Italic,
//                    modifier = Modifier.padding(Sizing.paddings.medium),
                )
                TextDefault(
//                    modifier = Modifier.padding(Sizing.paddings.medium),
                    text = "${
                        CalorieUnit.convert(
                            value = if (mealListViewModel.state.activeMeal == it) {
                                totals.totalCals
                            } else {
                                it.totalCalories ?: 0f
                            }, 
                            to = calorieUnit
                        ).toInt()
                    } $calorieUnit",
                )
            }

            if (activeMeal !== null && it.mealId == activeMeal.mealId) {
                CalorieTotalsBreakdown(totals = totals)
            }
        }

        if (activeMeal !== null && it.mealId == activeMeal.mealId) {
            IngredientList(calorieUnit = calorieUnit)
        }
    }
}

@Composable
fun CalorieTotalsBreakdown(totals: Totals) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Sizing.paddings.extraSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDefault(text = "Carbohydrates", fontSize = Sizing.font.small)
            TextDefault(text = "${totals.carbs.toInt()} G", fontSize = Sizing.font.small)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Sizing.paddings.extraSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDefault(text = "Fat", fontSize = Sizing.font.small)
            TextDefault(text = "${totals.fat.toInt()} G", fontSize = Sizing.font.small)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Sizing.paddings.extraSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDefault(text = "Protein", fontSize = Sizing.font.small)
            TextDefault(text = "${totals.protein.toInt()} G", fontSize = Sizing.font.small)
        }
    }
}
