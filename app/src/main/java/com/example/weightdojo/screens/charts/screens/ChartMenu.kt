package com.example.weightdojo.screens.charts.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.charts.Charts
import com.example.weightdojo.ui.Sizing

@Composable
fun ChartMenu(navigateTo: (chartScreen: Charts) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .clip(shape = RoundedCornerShape(Sizing.cornerRounding))
                    .background(MaterialTheme.colors.background)
                    .weight(1f)
                    .clickable { navigateTo(Charts.Weight) }
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconBuilder(
                    id = R.drawable.weight,
                    contentDescription = "Weight chart",
                    testTag = "WEIGHT_CHART",
                    color = Color(0xffF13030)
                )
                TextDefault(text = Charts.Weight.name)
            }
            Column(
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .clip(shape = RoundedCornerShape(Sizing.cornerRounding))
                    .background(MaterialTheme.colors.background)
                    .weight(1f)
                    .clickable { navigateTo(Charts.Calories) }
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconBuilder(
                    id = R.drawable.food,
                    contentDescription = "Calories chart",
                    testTag = "CALORIES_CHART",
                    color = Color(0xff008DD5)
                )
                TextDefault(text = Charts.Calories.name)
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
        }
    }
}