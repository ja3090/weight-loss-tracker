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
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.VMFactory

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

    meals?.map {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (mealListViewModel.state.isEditing) {
                        return@clickable
                    } else {
                        mealListViewModel.setActive(it)
                    }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextDefault(
                text = it.mealName,
                color = MaterialTheme.colors.primaryVariant,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(Sizing.paddings.medium),
            )
            TextDefault(
                modifier = Modifier.padding(Sizing.paddings.medium),
                text = "${
                    CalorieUnit.convert(value = it.totalCalories ?: 0f, to = calorieUnit).toInt()
                } $calorieUnit",
            )
        }

        if (activeMeal !== null && it.mealId == activeMeal.mealId) {
            IngredientList(calorieUnit = calorieUnit)
        }
    }
}
