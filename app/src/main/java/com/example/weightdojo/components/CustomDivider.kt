package com.example.weightdojo.components

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(modifier: Modifier = Modifier, tinted: Boolean = true) {
    Divider(
        color = if (tinted) {
            MaterialTheme.colors.secondaryVariant.copy(0.3f)
        } else {
            MaterialTheme.colors.secondaryVariant
        },
        thickness = 1.dp,
        modifier = modifier
    )
}