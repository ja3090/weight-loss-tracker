package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.weightdojo.components.inputs.NumberInput
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

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
        NumberInput(
            inputValue = weight ?: "",
            onValueChange = {
                val passes = validateInput(it)

                if (passes) weightSetter(it)
            },
            trailingIcon = { TextDefault(text = weightUnit) },
            placeholder = {},
            leadingIcon = {}
        )
    }
}