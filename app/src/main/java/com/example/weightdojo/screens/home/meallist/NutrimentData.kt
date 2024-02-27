package com.example.weightdojo.screens.home.meallist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.NutrimentBreakdown
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.toTwoDecimalPlaces

@Composable
fun NutrimentData(
    nutriments: List<NutrimentBreakdown>,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.small)
            .fillMaxWidth()
    ) {
        nutriments.map {
            val formattedNumber = toTwoDecimalPlaces(it.totalGrams)

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