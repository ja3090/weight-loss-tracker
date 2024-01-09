package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory

@Composable
fun MealList(
    meals: List<Meal>?, weightUnit: String,
    mealListViewModel: MealListVM = viewModel(
        factory = VMFactory.build {
            MealListVM(MyApp.appModule.database)
        }
    )
) {

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
                    text = it.name,
                    color = MaterialTheme.colors.primaryVariant,
                    fontStyle = FontStyle.Italic
                )
                if (it.hasIngredients) {
                    IconBuilder(
                        id = R.drawable.menu_book,
                        contentDescription = "expandable menu",
                        testTag = "EXPANDABLE_MENU",
                        modifier = Modifier.padding(horizontal = Sizing.paddings.small / 2)
                    )
                }
            }
            TextDefault(
                modifier = Modifier.padding(Sizing.paddings.medium),
                text = "${it.totalCalories.toInt()} $weightUnit",
            )
        }
        if (it.uid == mealListViewModel.state.activeMeal?.uid) {
            MealItemOptions(
                ingredientList = mealListViewModel.state.ingredientList,
                weightUnit = weightUnit
            )
        }
    }
}
