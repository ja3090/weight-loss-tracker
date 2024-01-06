package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.ui.Sizing

@Composable
fun SexOptions(extraOptions: ExtraOptions) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Button(sex = Sex.Male, extraOptions = extraOptions, modifier = Modifier.weight(1f))
        Button(sex = Sex.Female, extraOptions = extraOptions, modifier = Modifier.weight(1f))
    }
}

@Composable
fun Button(sex: Sex, extraOptions: ExtraOptions, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(Sizing.paddings.small)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(
                if (extraOptions.sex == sex) {
                    MaterialTheme.colors.primaryVariant
                } else {
                    MaterialTheme.colors.background
                }
            )
            .border(
                width = 2.dp,
                color = if (extraOptions.sex == sex) {
                    MaterialTheme.colors.primaryVariant
                } else {
                    Color.Transparent
                }, shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                if (extraOptions.sex == sex) {
                    extraOptions.sex = null
                } else {
                    extraOptions.sex = sex
                }
            },
        contentAlignment = Alignment.Center
    ) {

        TextDefault(
            text = sex.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(Sizing.paddings.medium),
            color = if (extraOptions.sex == sex) {
                MaterialTheme.colors.background
            } else {
                MaterialTheme.colors.primary
            },
            textAlign = TextAlign.Center
        )
    }
}