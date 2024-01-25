package com.example.weightdojo.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun NameField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (newName: String) -> Unit,
    showDetailedList: Boolean,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.medium)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .padding(vertical = Sizing.paddings.extraSmall / 2)
        ) {
            if (value.isEmpty()) {
                TextDefault(
                    text = "Name",
                    color = MaterialTheme.colors.primary.copy(0.5f)
                )
            }
            BasicTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(
                    fontSize = Sizing.font.default,
                    color = MaterialTheme.colors.primary,
                ),
                cursorBrush = SolidColor(MaterialTheme.colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        IconBuilder(
            id = R.drawable.arrow_down,
            contentDescription = "Close detailed list",
            testTag = "DETAILED_LIST_CLOSE",
            modifier = Modifier
                .graphicsLayer(rotationZ = if (showDetailedList) 180f else 0f)
                .offset(x = if (showDetailedList) (-6).dp else 6.dp)
                .then(modifier)
        )
    }
}