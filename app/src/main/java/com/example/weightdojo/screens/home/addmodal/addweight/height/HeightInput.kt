package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.ui.Sizing

enum class HeightUnits {
    CM, IN
}

class HeightInputOptions {
    var menuOpen by mutableStateOf(false)
    var heightUnit by mutableStateOf(HeightUnits.CM)

    fun update(newMenuOpen: Boolean = menuOpen, newHeightUnit: HeightUnits = heightUnit) {
        menuOpen = newMenuOpen
        heightUnit = newHeightUnit
    }
}

@Composable
fun HeightInput(modifier: Modifier = Modifier, extraOptions: ExtraOptions) {

    val heightInputOptions by remember {
        mutableStateOf(HeightInputOptions())
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextDefault(
            text = "Height", modifier = Modifier.padding(top = Sizing.paddings.small)
        )

        when (heightInputOptions.heightUnit) {
            HeightUnits.CM -> Centimetres(
                extraOptions = extraOptions,
                heightInputOptions = heightInputOptions
            )

            HeightUnits.IN -> Inches(
                extraOptions = extraOptions,
                heightInputOptions = heightInputOptions
            )
        }
    }
}