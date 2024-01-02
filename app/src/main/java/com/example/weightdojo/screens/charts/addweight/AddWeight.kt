package com.example.weightdojo.screens.charts.addweight

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.weightdojo.components.text.TextDefault
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddWeight() {
    var number by remember { mutableStateOf("") }

    OutlinedTextField(
        value = number,
        onValueChange = { newNumber ->
            if (newNumber.all { it.isDigit() }) {
                number = newNumber
            }
        },
        label = { TextDefault("Enter number") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        maxLines = 1
    )
}