package com.example.weightdojo.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xffFEF9FF),
    primaryVariant = Color(0xff0FFF95),
    secondary = Color(0xff0B5351),
    secondaryVariant = Color(0xff06BA63),
    background = Color(0xff191516)
)

private val LightColorPalette = lightColors(
    primary = Color(0xff191516),
    primaryVariant = Color(0xff06BA63),
    secondary = Color(0xfff0f0f0),
    secondaryVariant = Color(0xff06BA63),
    background = Color(0xffFEF9FF)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}