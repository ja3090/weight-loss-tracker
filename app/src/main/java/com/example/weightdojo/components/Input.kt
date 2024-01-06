package com.example.weightdojo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.helper.widget.MotionPlaceholder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.addweight.validateInput
import com.example.weightdojo.ui.Sizing

@Composable
fun Input(
    inputValue: String,
    onValueChange: (new: String) -> Unit,
    trailingIcon: @Composable () -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier
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
            leadingIcon = {},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary,
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(Sizing.paddings.small)
                .wrapContentWidth()
                .align(Alignment.Center),
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            textStyle = TextStyle(textAlign = TextAlign.Center)
        )
    }
}