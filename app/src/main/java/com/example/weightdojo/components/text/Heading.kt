package com.example.weightdojo.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.weightdojo.ui.Sizing

@Composable
fun Heading(text: String, modifier: Modifier = Modifier) {
    TextDefault(
        text = text,
        fontSize = Sizing.font.heading,
        modifier = Modifier
            .padding(vertical = Sizing.paddings.medium)
            .then(modifier),
        fontWeight = FontWeight.Bold
    )
}