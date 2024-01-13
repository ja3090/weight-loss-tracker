package com.example.weightdojo.screens.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun BottomBarNav(navigateTo: (screen: Screens?) -> Unit, currentScreen: String?) {
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
            NavButton(
                navigateTo = navigateTo,
                screen = Screens.Charts,
                currentScreen = currentScreen,
                modifier = Modifier.weight(1f)
            ) {
                IconBuilder(
                    id = R.drawable.charts,
                    contentDescription = "Charts",
                    testTag = "CHARTS",
                    color = it
                )
                TextDefault(
                    text = "Charts",
                    modifier = Modifier.padding(top = Sizing.paddings.small),
                )
            }
            NavButton(
                navigateTo = navigateTo,
                screen = Screens.Home,
                currentScreen = currentScreen,
                modifier = Modifier.weight(1f)
            ) {
                IconBuilder(
                    id = R.drawable.home,
                    contentDescription = "Home",
                    testTag = "HOME",
                    color = it
                )
                TextDefault(
                    text = "Home",
                    modifier = Modifier.padding(top = Sizing.paddings.small),
                )
            }
            NavButton(
                navigateTo = navigateTo,
                screen = Screens.Settings,
                currentScreen = currentScreen,
                modifier = Modifier.weight(1f)
            ) {
                IconBuilder(
                    id = R.drawable.settings,
                    contentDescription = "Settings",
                    testTag = "SETTINGS",
                    color = it
                )
                TextDefault(
                    text = "Settings",
                    modifier = Modifier.padding(top = Sizing.paddings.small),
                )
            }
        }
    }
}

@Composable
fun NavButton(
    screen: Screens,
    navigateTo: (screen: Screens) -> Unit,
    modifier: Modifier = Modifier,
    currentScreen: String?,
    content: @Composable (color: Color) -> Unit,
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .clickable {
                navigateTo(screen)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content(
            if (screen.name == currentScreen) {
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.primary.copy(0.5f)
            }
        )
    }
}