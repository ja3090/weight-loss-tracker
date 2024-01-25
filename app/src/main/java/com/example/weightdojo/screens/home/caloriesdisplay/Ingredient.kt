package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun Ingredient(
    modifier: Modifier = Modifier,
    totalCalories: Float,
    name: String,
    weightUnit: String,
    markedForDeletion: Boolean = false,
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(
                horizontal = Sizing.paddings.medium,
                vertical = Sizing.paddings.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDefault(
            text = name,
            color = MaterialTheme.colors.primaryVariant,
            fontStyle = FontStyle.Italic,
            fontSize = Sizing.font.default * 0.9,
            textDecoration = if (markedForDeletion) {
                TextDecoration.LineThrough
            } else TextDecoration.None
        )
        TextDefault(
            text = "${if (markedForDeletion) 0 else totalCalories.toInt()} $weightUnit",
            fontSize = Sizing.font.default * 0.9
        )
    }
}