package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.screens.home.addmodal.addweight.validateInput

@Composable
fun Centimetres(
    extraOptions: ExtraOptions, heightInputOptions: HeightInputOptions
) {
    Input(
        InputArgs(
            inputValue = if (extraOptions.height == null) "" else extraOptions.height.toString(),
            onValueChange = {
                val passes = validateInput(it)

                if (passes) extraOptions.height = it
            },
            trailingIcon = {
                HeightOptions(heightInputOptions)
            },
            placeholder = {},
            leadingIcon = {},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
        )
    )
}