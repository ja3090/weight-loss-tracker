package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.validateInput

@Composable
fun WeightInput(weight: String?, weightUnit: String, weightSetter: (newWeight: String) -> Unit) {
    Column(
        modifier = Modifier
//            .padding(bottom = Sizing.paddings.medium)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        TextDefault(
            text = "Weight",
            modifier = Modifier
                .padding(top = Sizing.paddings.small)
        )
        Input(
            InputArgs(
                inputValue = weight ?: "",
                onValueChange = {
                    val passes = validateInput(it)

                    if (passes) weightSetter(it)
                },
                trailingIcon = { TextDefault(text = weightUnit) },
                placeholder = {},
                leadingIcon = {},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )
        )
    }
}