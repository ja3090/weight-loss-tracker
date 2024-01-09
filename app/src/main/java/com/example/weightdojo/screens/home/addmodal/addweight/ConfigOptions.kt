package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.screens.home.addmodal.addweight.height.HeightInput


@Composable
fun ConfigOptions(extraOptions: ExtraOptions) {

    SexOptions(extraOptions = extraOptions)

    Column(
        modifier = Modifier
            .padding(bottom = Sizing.paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgeInput(extraOptions = extraOptions)

        HeightInput(extraOptions = extraOptions)
    }
}