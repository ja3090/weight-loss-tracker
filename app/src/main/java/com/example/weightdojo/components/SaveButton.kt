package com.example.weightdojo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SaveButton(onClick: () -> Unit) {
    Box(modifier = Modifier
        .clip(
            RoundedCornerShape(Sizing.cornerRounding)
        )
        .background(MaterialTheme.colors.primaryVariant)
        .clickable(onClick = onClick)
    ) {
        TextDefault(
            text = "Save",
            color = MaterialTheme.colors.background,
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 20.dp)
        )
    }
}