package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun WeightInput(weight: String, weightUnit: String, weightSetter: (newWeight: String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.background)
            .padding(Sizing.paddings.medium)
    ) {
        TextField(
            value = weight,
            onValueChange = {
                val passes = validateInput(it)

                if (passes) weightSetter(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary,
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(min = 10.dp),
            trailingIcon = { TextDefault(text = weightUnit) },
            placeholder = {
                TextDefault(
                    text = "Enter Weight",
                    color = MaterialTheme.colors.primary.copy(0.5f)
                )
            }
        )
    }
}