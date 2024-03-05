package com.example.weightdojo.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.weightdojo.ui.Sizing

@Composable
fun TextDefault(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = 0.8.sp,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary.copy(0.85f),
    modifier: Modifier = Modifier,
    fontSize: TextUnit = Sizing.font.default,
    textAlign: TextAlign = TextAlign.Center,
    fontStyle: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        color = color,
        fontSize = fontSize,
        textAlign = textAlign,
        fontStyle = fontStyle,
        textDecoration = textDecoration,
    )
}