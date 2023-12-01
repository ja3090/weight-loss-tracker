package com.example.weightdojo.components.keypad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeypadButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {

        ElevatedButton(
            onClick = onClick,
            modifier = Modifier.size(100.dp)
        ) {
            Text(text = text, fontSize = 25.sp)
        }
    }
}