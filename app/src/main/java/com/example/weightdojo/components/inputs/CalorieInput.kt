package com.example.weightdojo.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing


@Composable
fun CalorieInput(
    modifier: Modifier = Modifier,
    value: Float,
    setter: (str: Float) -> Unit,
    textAlign: TextAlign = TextAlign.Left,
    placeholderText: String
) {
    Box(
        modifier = Modifier
            .padding(vertical = Sizing.paddings.extraSmall / 2)
    ) {
        if (value == 0f) {
            TextDefault(
                text = placeholderText,
                color = MaterialTheme.colors.primary.copy(0.5f),
                fontSize = Sizing.font.small
            )
        }
        BasicTextField(
            value = if (value == 0f) "" else "${value.toInt()}",
            onValueChange = {
                val passes = it.isEmpty() || it.isDigitsOnly()

                if (!passes) return@BasicTextField

                setter(if (it.isEmpty()) 0f else it.toFloat())
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = Sizing.font.small,
                color = MaterialTheme.colors.primary,
                textAlign = textAlign
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            modifier = Modifier
                .then(modifier)
        )
    }
}
