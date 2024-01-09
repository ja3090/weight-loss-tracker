package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import androidx.compose.runtime.*
import com.example.weightdojo.components.inputs.NumberInput
import com.example.weightdojo.components.text.TextDefault

@Composable
fun Inches(
    extraOptions: ExtraOptions, heightInputOptions: HeightInputOptions
) {
    var feet by remember { mutableStateOf("") }
    var inches by remember { mutableStateOf("") }

    fun validateFeet(new: String): Boolean {
        if (new.startsWith("0")) return false
        if (new.count() > 1) return false

        return new.isEmpty() || new.all { it.isDigit() }
    }

    fun validateInches(new: String): Boolean {
        if (new.startsWith("0")) return false
        if (!new.all { it.isDigit() }) return false
        return new.isEmpty() || new.toInt() <= 11
    }

    val setExtraOptions = {
        if (inches.isEmpty() || feet.isEmpty()) extraOptions.height = null
        else {
            val heightInInches = (feet.toFloat() * 12) + inches.toFloat()

            extraOptions.height = heightInInches.toString()
        }
    }

    setExtraOptions()

    Row {
        NumberInput(
            inputValue = feet,
            onValueChange = {
                val passes = validateFeet(it)

                if (passes) {
                    feet = it
                }
            },
            trailingIcon = {
                TextDefault(text = "FT")
            },
            placeholder = {},
            modifier = Modifier.weight(1f)
        )
        NumberInput(
            inputValue = inches,
            onValueChange = {
                val passes = validateInches(it)

                if (passes) {
                    inches = it
                }
            },
            trailingIcon = {
                HeightOptions(heightInputOptions)
            },
            placeholder = {},
            modifier = Modifier.weight(1f)
        )
    }
}