package com.example.weightdojo.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.weightdojo.ui.Sizing

@Composable

fun Heading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary.copy(0.85f)
) {
    TextDefault(
        text = text,
        fontSize = Sizing.font.heading,
        modifier = Modifier
            .padding(vertical = Sizing.paddings.medium)
            .then(modifier),
        fontWeight = FontWeight.Bold,
        color = color
    )
}