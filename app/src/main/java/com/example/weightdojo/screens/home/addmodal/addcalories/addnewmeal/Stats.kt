package com.example.weightdojo.screens.home.addmodal.addcalories.addnewmeal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.totalGrams


@Composable
fun Stats(ingredientList: List<IngredientState>) {
    val fontSize = Sizing.font.default * 0.85

    val totals = object {
        var carbs = 0f
        var fat = 0f
        var protein = 0f
        var totalCals = 0f
    }

    for (ingredient in ingredientList) {
        if (ingredient.markedFor == Marked.DELETE) continue
        totals.carbs += totalGrams(ingredient.grams, ingredient.carbsPer100)
        totals.protein += totalGrams(ingredient.grams, ingredient.proteinPer100)
        totals.fat += totalGrams(ingredient.grams, ingredient.fatPer100)
        totals.totalCals += totalGrams(ingredient.grams, ingredient.caloriesPer100)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        TextDefault(
            modifier = Modifier
                .padding(Sizing.paddings.small)
                .fillMaxWidth(),
            text = "Totals G",
            fontSize = fontSize
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextDefault(
                    text = "Pro",
                    fontSize = fontSize
                )
                TextDefault(
                    text = "${totals.protein.toInt()}",
                    fontSize = fontSize
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextDefault(
                    text = "Fat",
                    fontSize = fontSize
                )
                TextDefault(
                    text = "${totals.fat.toInt()}",
                    fontSize = fontSize
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextDefault(
                    text = "Car",
                    fontSize = fontSize
                )
                TextDefault(
                    text = "${totals.carbs.toInt()}",
                    fontSize = fontSize
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextDefault(
                    text = "KCAL",
                    fontSize = fontSize
                )
                TextDefault(
                    text = "${totals.totalCals.toInt()}",
                    fontSize = fontSize
                )
            }
        }
    }
}