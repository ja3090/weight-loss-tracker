package com.example.weightdojo.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing

@Composable
fun BottomBarNav(navigateTo: (screen: Screens) -> Unit) {

    Column {

        CustomDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colors.secondary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        navigateTo(Screens.Home)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconBuilder(
                    id = R.drawable.home,
                    contentDescription = "Home",
                    testTag = "HOME"
                )
                TextDefault(
                    text = "Home",
                    modifier = Modifier.padding(top = Sizing.paddings.small)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        navigateTo(Screens.Charts)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconBuilder(
                    id = R.drawable.charts,
                    contentDescription = "Charts",
                    testTag = "CHARTS",
                    color = CustomColors.Red
                )
                TextDefault(
                    text = "Charts",
                    modifier = Modifier.padding(top = Sizing.paddings.small)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        navigateTo(Screens.Settings)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconBuilder(
                    id = R.drawable.settings,
                    contentDescription = "Settings",
                    testTag = "SETTINGS",
                    color = CustomColors.Blue
                )
                TextDefault(
                    text = "Settings",
                    modifier = Modifier.padding(top = Sizing.paddings.small)
                )
            }
        }
    }
}