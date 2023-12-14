package com.example.weightdojo.components.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.weightdojo.components.TextDefault

@Composable
fun KeypadButton(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        TextDefault(text = text, fontWeight = fontWeight, color = color)
    }
}