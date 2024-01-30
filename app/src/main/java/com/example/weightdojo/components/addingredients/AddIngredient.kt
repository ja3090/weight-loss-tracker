package com.example.weightdojo.components.addingredients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun AddIngredient(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(Sizing.paddings.medium)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconBuilder(
            id = R.drawable.add,
            contentDescription = "add ingredient",
            testTag = "ADD_INGREDIENT",
            modifier = Modifier
                .padding(horizontal = Sizing.paddings.small)
                .clickable { onClick() }
        )
    }
}