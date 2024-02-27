package com.example.weightdojo.screens.charts.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.charts.LineChart
import com.example.weightdojo.screens.charts.WeightChartViewModel
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.WeightUnit
import com.example.weightdojo.utils.WeightUnits

@Composable
fun WeightChart(
    chartViewModel: WeightChartViewModel = viewModel(
        factory = VMFactory.build {
            WeightChartViewModel(MyApp.appModule.database)
        }
    ),
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    weightUnit: WeightUnits = config?.weightUnit ?: AppConfig.internalDefaultWeightUnit
) {
    LineChart(
        chartViewModel = chartViewModel,
        upperLowerBound = 10,
        title = "Weight",
        unit = weightUnit.name,
        convertValues = {
            WeightUnit.convert(to = weightUnit, value = it)
        })
}