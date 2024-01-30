package com.example.weightdojo.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.ui.Sizing

data class InputArgs(
    val inputValue: String,
    val onValueChange: (new: String) -> Unit,
    val trailingIcon: (@Composable () -> Unit)? = null,
    val placeholder: @Composable () -> Unit,
    val modifier: Modifier = Modifier,
    val leadingIcon: (@Composable () -> Unit)? = null,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle = TextStyle(textAlign = TextAlign.Center)
)

@Composable
fun Input(
    inputArgs: InputArgs
) {
    val (inputValue,
        onValueChange, trailingIcon, placeholder, modifier, leadingIcon, keyboardOptions, textStyle) = inputArgs

        TextField(
            value = inputValue,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary, backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .then(modifier),
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            textStyle = textStyle
        )
}