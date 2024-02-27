package com.example.weightdojo.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing


@Composable
fun CalorieBreakdown(
    calorieInfo: Float,
    setter: (str: Float) -> Unit,
    nutrimentName: String,
    per100: Boolean = false,
    placeholderText: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.medium)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalorieInput(
            value = calorieInfo,
            setter = setter,
            modifier = Modifier.weight(0.5f),
            placeholderText = placeholderText
        )
        Row {
            TextDefault(text = nutrimentName, fontSize = Sizing.font.small)
            if (per100) {
                TextDefault(
                    text = " per 100g",
                    fontSize = Sizing.font.small,
                    color = MaterialTheme.colors.primary.copy(0.35f),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}
