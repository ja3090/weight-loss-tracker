package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.utils.VMFactory

@Composable
fun ChartScreen(
    chartViewModel: ChartViewModel = viewModel(
        factory = VMFactory.build {
            ChartViewModel(MyApp.appModule.database)
        }
    ),
) {
    if (chartViewModel.chartState.data == null) return

    Chart(chartState = chartViewModel.chartState)
}