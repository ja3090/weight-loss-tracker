package com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesVm
import com.example.weightdojo.ui.Sizing

@Composable
fun MealTemplateItem(
    it: MealTemplate,
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    calorieUnit: String = config?.calorieUnit?.name ?: "KCAL",
) {
    val addCaloriesVm: AddCaloriesVm = viewModel()
    val searchMealTemplatesVM: SearchMealTemplatesVM = viewModel()
    val state = searchMealTemplatesVM.state

    Box(
        modifier = Modifier
            .clickable { searchMealTemplatesVM.makeActive(it) }
    ) {
        Column(
            modifier = Modifier
                .padding(Sizing.paddings.medium)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { searchMealTemplatesVM.makeActive(it) }
            ) {
                TextDefault(text = it.name, color = MaterialTheme.colors.primaryVariant)

                TextDefault(text = "${it.totalCalories.toInt()} $calorieUnit")
            }

            if (state.activeMealTemplate?.mealTemplateId == it.mealTemplateId) {
                SubListItem(title = "Protein", value = it.totalProtein)
                SubListItem(title = "Fat", value = it.totalFat)
                SubListItem(title = "Carbs", value = it.totalCarbohydrates)
                Row(
                    modifier = Modifier
                        .padding(top = Sizing.paddings.extraSmall)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(buttonName = "Use") {
                        addCaloriesVm.moveToAddNewMeal(it.mealTemplateId)
                    }
                }
            }
        }
    }
}


@Composable
fun SubListItem(title: String, value: Float?) {
    val textSize = Sizing.font.default * 0.85

    Row(
        modifier = Modifier
            .padding(vertical = Sizing.paddings.extraSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDefault(text = title, fontSize = textSize)
        TextDefault(text = "${(value ?: 0f).toInt()} G", fontSize = textSize)
    }
}
