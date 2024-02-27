package com.example.weightdojo.components.inputs

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.example.weightdojo.components.mealcreation.getColour
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing

@Composable
fun Field(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (newName: String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable ((color: Color) -> Unit),
    inputTextColour: Color = MaterialTheme.colors.primary.copy(0.85f),
    fontSize: TextUnit = Sizing.font.default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    focusOptions: (FocusState) -> Unit = {},
    textAlign: TextAlign = TextAlign.Left
) {
    var hasFocused by remember { mutableStateOf(false) }
    var stillEmptyAfterFocusing by remember { mutableStateOf(false) }

    val raiseError = hasFocused && stillEmptyAfterFocusing && value.isEmpty()

    val colour = if (raiseError) {
        CustomColors.Red.copy(0.5f)
    } else {
        inputTextColour
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .padding(vertical = Sizing.paddings.extraSmall / 2)
        ) {
            if (value.isEmpty()) {
                TextDefault(
                    text = placeholder,
                    color = getColour(
                        raiseError,
                        MaterialTheme.colors.primary.copy(0.5f)
                    ),
                    fontSize = fontSize,
                    textAlign = textAlign
                )
            }
            BasicTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                keyboardOptions = keyboardOptions,
                textStyle = TextStyle(
                    fontSize = fontSize,
                    color = colour,
                    textAlign = textAlign
                ),
                cursorBrush = SolidColor(MaterialTheme.colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!hasFocused) hasFocused = it.isFocused

                        if (!it.hasFocus && hasFocused) stillEmptyAfterFocusing = true
                        focusOptions(it)
                    }
            )
        }
        trailingIcon(MaterialTheme.colors.primary.copy(0.5f))
    }
}