package com.example.weightdojo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0XFF000000).copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,

        ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.White,
        )
    }
}