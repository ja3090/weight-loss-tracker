package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.utils.VMFactory

@Composable
fun ChartScreen(
    chartViewModel: CalorieChartViewModel = viewModel(
        factory = VMFactory.build {
            CalorieChartViewModel(MyApp.appModule.database)
        }
    ),
) {
    if (chartViewModel.chartState.data == null) return

    WeightChart(chartViewModel = chartViewModel)
}