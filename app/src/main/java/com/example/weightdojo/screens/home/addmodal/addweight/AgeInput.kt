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
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.Input
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun AgeInput(extraOptions: ExtraOptions) {
    Column(
        modifier = Modifier
            .padding(bottom = Sizing.paddings.medium)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        TextDefault(
            text = "Age",
            modifier = Modifier
                .padding(top = Sizing.paddings.small)
        )
        Input(
            inputValue = if (extraOptions.age == null) "" else extraOptions.age.toString(),
            onValueChange = {
                if ((it.isDigitsOnly() && it.count() <= 2) || it.isEmpty()) {
                    extraOptions.age = it
                }
            },
            trailingIcon = {},
            placeholder = {}
        )

    }
}