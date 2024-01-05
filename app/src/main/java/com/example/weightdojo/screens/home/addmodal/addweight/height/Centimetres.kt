package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.screens.home.addmodal.addweight.validateInput
import com.example.weightdojo.ui.Sizing

@Composable
fun Centimetres(
    extraOptions: ExtraOptions, heightInputOptions: HeightInputOptions
) {
    TextField(value = if (extraOptions.height == null) "" else extraOptions.height.toString(),
        onValueChange = {
            val passes = validateInput(it)

            if (passes) extraOptions.height = it
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.primary, backgroundColor = Color.Transparent
        ),
        modifier = Modifier
            .wrapContentWidth()
            .widthIn(min = 10.dp)
            .padding(
                Sizing.paddings.small
            ),
        textStyle = TextStyle(textAlign = TextAlign.Center),
        trailingIcon = {
            HeightOptions(heightInputOptions)
        })
}