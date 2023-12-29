package com.example.weightdojo.screens.charts.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.screens.charts.LineChart
import com.example.weightdojo.screens.charts.WeightChartViewModel
import com.example.weightdojo.utils.VMFactory

@Composable
fun WeightChart(
    chartViewModel: WeightChartViewModel = viewModel(
        factory = VMFactory.build {
            WeightChartViewModel(MyApp.appModule.database)
        }
    ),
) {
    LineChart(chartViewModel = chartViewModel)
}