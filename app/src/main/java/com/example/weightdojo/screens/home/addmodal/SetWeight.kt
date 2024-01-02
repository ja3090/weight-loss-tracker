package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.components.text.Heading

@Composable
fun SetWeight() {

    var number by remember { mutableStateOf("") }

    fun validateInput(newText: String): Boolean {
        if (newText.isEmpty()) return true
        if (newText.count() > 6) return false
        if (newText.startsWith("0")) return false
        if (newText.contains('.') && newText.startsWith(".")) return false
        if (newText.count { it == '.' } > 1) return false

        return newText.all { it.isDigit() || it == '.' }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxSize()
    ) {
        Heading(text = "Set Weight")
        CustomDivider(tinted = false)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = number, onValueChange = {
                    val passes = validateInput(it)

                    if (passes) number = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )
        }
    }
}