package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.ui.Sizing
import androidx.compose.runtime.*
import com.example.weightdojo.components.Input
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
        Input(
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
        Input(
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