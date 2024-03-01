package com.example.weightdojo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.NutrimentSummary
import com.example.weightdojo.datatransferobjects.NutritionBreakdown
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.toTwoDecimalPlaces
import kotlin.jvm.Throws

@Composable
fun <K : NutrimentSummary, T: NutritionBreakdown<K>> NutritionBreakdown(
    data: T,
    setter: (T) -> Unit,
    isActive: Boolean,
    calorieUnit: CalorieUnits = MyApp.appModule.calorieUnit
) {
    Row(
        modifier = Modifier
            .clickable { setter(data) }
            .padding(vertical = Sizing.paddings.medium, horizontal = Sizing.paddings.small)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isActive) TextDefault(
            text = data.name,
            color = MaterialTheme.colors.primaryVariant
        )
        else TextDefault(text = data.name)
        TextDefault(text = "${toTwoDecimalPlaces(data.calories)} $calorieUnit")
    }
    if (isActive) NutrimentData(nutriments = data.nutriments)
}