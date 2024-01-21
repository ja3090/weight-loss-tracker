package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(
            sex = Sex.Male,
            extraOptions = extraOptions,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.5f)
                .padding(end = Sizing.paddings.medium)
        )
        Button(
            sex = Sex.Female,
            extraOptions = extraOptions,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.5f)
                .padding(start = Sizing.paddings.medium)
        )
    }
}

@Composable
fun Button(sex: Sex, extraOptions: ExtraOptions, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = Sizing.paddings.medium)
            .then(modifier)
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
    ) {

        TextDefault(
            text = sex.name,
            modifier = Modifier
                .align(Alignment.Center),
            color = if (extraOptions.sex == sex) {
                MaterialTheme.colors.background
            } else {
                MaterialTheme.colors.primary
            },
            textAlign = TextAlign.Center
        )
    }
}