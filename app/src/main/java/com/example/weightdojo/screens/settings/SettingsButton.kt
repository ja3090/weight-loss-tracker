package com.example.weightdojo.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun SettingsButton(text: String, onClick: () -> Unit) {
    TextDefault(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = Sizing.paddings.medium,
                horizontal = Sizing.paddings.small
            )
            .clickable { onClick() },
        textAlign = TextAlign.Left
    )
}