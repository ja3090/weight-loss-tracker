package com.example.weightdojo.screens.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PeriodPicker(
    chartViewModel: BaseChartVM,
    chartState: ChartState = chartViewModel.chartState
) {

    Row(
        modifier = Modifier
            .padding(vertical = 50.dp)
            .width(200.dp)
            .height(60.dp)
            .clip(shape = RoundedCornerShape(Sizing.cornerRounding)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (entry in TimePeriods.entries) {
            if (entry.ordinal != 0) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primaryVariant)
                        .fillMaxHeight()
                        .width(1.dp),
                )
            }
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(
                    if (chartState.timePeriod == entry) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        MaterialTheme.colors.background
                    }
                )
                .clickable {
                    chartViewModel.viewModelScope.launch(Dispatchers.IO) {
                        try {
                            chartViewModel.getAndSetData(entry)

                        } catch (e: Exception) {
                            throw Error(e)
                        }
                    }
                },
                contentAlignment = Alignment.Center
            ) {
                TextDefault(
                    text = entry.toString(), color = if (chartState.timePeriod == entry) {
                        MaterialTheme.colors.background
                    } else {
                        MaterialTheme.colors.primary
                    }
                )
            }
        }
    }
}