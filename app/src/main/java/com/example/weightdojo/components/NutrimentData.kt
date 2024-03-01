package com.example.weightdojo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.NutrimentSummary
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.toTwoDecimalPlaces

@Composable
fun <NutrimentSummaryType : NutrimentSummary> NutrimentData(
    nutriments: List<NutrimentSummaryType>,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.small)
            .fillMaxWidth()
    ) {
        nutriments.map {
            val formattedNumber = toTwoDecimalPlaces(it.grams)

            Row(
                modifier = Modifier
                    .padding(vertical = Sizing.paddings.extraSmall)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextDefault(text = it.nutrimentName)
                TextDefault(text = "$formattedNumber G")
            }
        }
    }
}