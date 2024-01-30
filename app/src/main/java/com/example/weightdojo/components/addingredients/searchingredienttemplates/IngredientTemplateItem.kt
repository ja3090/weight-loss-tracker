package com.example.weightdojo.components.addingredients.searchingredienttemplates

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
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesVm
import com.example.weightdojo.ui.Sizing

@Composable
fun IngredientTemplateItem(
    it: IngredientTemplate,
    useTemplate: (templ: IngredientTemplate) -> Unit,
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession()
) {
    val searchIngredientTemplatesVM: SearchIngredientTemplatesVM = viewModel()
    val state = searchIngredientTemplatesVM.state
    val calorieUnit = config?.calorieUnit?.name ?: "KCAL"

    Box(
        modifier = Modifier
            .clickable { searchIngredientTemplatesVM.makeActive(it) }
    ) {
        Column(
            modifier = Modifier
                .padding(Sizing.paddings.medium)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { searchIngredientTemplatesVM.makeActive(it) }
            ) {
                TextDefault(text = it.name, color = MaterialTheme.colors.primaryVariant)

                TextDefault(text = "${it.caloriesPer100.toInt()} $calorieUnit")
            }

            if (state.activeIngredientTemplate?.ingredientTemplateId == it.ingredientTemplateId) {
                SubListItem(title = "Protein", value = it.proteinPer100)
                SubListItem(title = "Fat", value = it.fatPer100)
                SubListItem(title = "Carbs", value = it.carbohydratesPer100)
                Row(
                    modifier = Modifier
                        .padding(top = Sizing.paddings.extraSmall)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(buttonName = "Use") {
//                        addCaloriesVm.stateHandler.addIngredientToMeal(it)
                        useTemplate(it)
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
        TextDefault(text = "${(value ?: 0f).toInt()} / 100G", fontSize = textSize)
    }
}
