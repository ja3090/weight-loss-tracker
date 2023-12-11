package com.example.weightdojo.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextDefault(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = 0.8.sp,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        color = color
    )
}