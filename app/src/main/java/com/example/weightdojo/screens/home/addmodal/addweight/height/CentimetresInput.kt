package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.runtime.Composable
import com.example.weightdojo.components.Input
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.screens.home.addmodal.addweight.validateInput

@Composable
fun Centimetres(
    extraOptions: ExtraOptions, heightInputOptions: HeightInputOptions
) {
    Input(
        inputValue = if (extraOptions.height == null) "" else extraOptions.height.toString(),
        onValueChange = {
            val passes = validateInput(it)

            if (passes) extraOptions.height = it
        },
        trailingIcon = {
            HeightOptions(heightInputOptions)
        }, placeholder = {})
}