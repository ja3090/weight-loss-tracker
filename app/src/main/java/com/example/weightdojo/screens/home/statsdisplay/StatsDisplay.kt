package com.example.weightdojo.screens.home.statsdisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.calculateTdee
import com.example.weightdojo.utils.statsDisplayHelper

@Composable
fun StatsDisplay(
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    dayData: DayData?,
    mostRecentWeight: Float?
) {
    val config = configSessionCache.getActiveSession()

    val stats = statsDisplayHelper(config, dayData)

    Box(
        modifier = Modifier
            .padding(
                top = 0.dp,
                start = Sizing.paddings.small,
                end = Sizing.paddings.small,
                bottom = Sizing.paddings.small
            )
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        Sizing.cornerRounding
                    )
                )
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth(),
        ) {
            Column {
                Widget(
                    stat = stats.calories,
                    statTitle = "Total Calories",
                    modifier = Modifier.weight(1f)
                )
                CustomDivider(
                    modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .aspectRatio(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Widget(
                        stat = calculateTdee(
                            config?.age,
                            config?.sex,
                            config?.height,
                            mostRecentWeight,
                            config?.calorieUnit,
                            config?.weightUnit
                        ) ?: "-", statTitle = "TDEE", modifier = Modifier.weight(1f)
                    )
                    Widget(
                        stat = stats.weight,
                        statTitle = "Weight",
                        modifier = Modifier.weight(1f)
                    )
                    Widget(
                        stat = stats.goalWeight,
                        statTitle = "Goal",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}