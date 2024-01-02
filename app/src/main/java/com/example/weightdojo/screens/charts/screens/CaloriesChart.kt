package com.example.weightdojo.screens.charts.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.screens.charts.CalorieChartViewModel
import com.example.weightdojo.screens.charts.LineChart
import com.example.weightdojo.utils.VMFactory


@Composable
fun CaloriesChart(
    chartViewModel: CalorieChartViewModel = viewModel(
        factory = VMFactory.build {
            CalorieChartViewModel(MyApp.appModule.database)
        }
    ),
) {
    LineChart(
        chartViewModel = chartViewModel,
        upperLowerBound = 100,
        title = "Calories",
        unit = "kcal"
    )
}