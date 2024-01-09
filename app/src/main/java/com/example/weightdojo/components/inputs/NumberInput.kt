package com.example.weightdojo.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.ui.Sizing

@Composable
fun NumberInput(
    inputValue: String,
    onValueChange: (new: String) -> Unit,
    trailingIcon: @Composable () -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.background)
            .then(modifier),
    ) {
        TextField(
            value = inputValue,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary,
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(Sizing.paddings.small)
//                .wrapContentWidth()
                .align(Alignment.Center),
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            textStyle = TextStyle(textAlign = TextAlign.Center)
        )
    }
}