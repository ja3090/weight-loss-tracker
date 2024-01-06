package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.Input
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Sex
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