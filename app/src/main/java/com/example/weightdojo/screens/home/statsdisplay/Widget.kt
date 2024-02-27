package com.example.weightdojo.screens.home.statsdisplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun Widget(modifier: Modifier = Modifier, stat: String, statTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextDefault(
            text = statTitle,
            modifier = Modifier.padding(bottom = Sizing.paddings.small),
            color = MaterialTheme.colors.primary.copy(0.5f)
        )
        TextDefault(text = stat)
    }
}