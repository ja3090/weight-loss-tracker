package com.example.weightdojo.screens.charts.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.charts.CalorieChartViewModel
import com.example.weightdojo.screens.charts.LineChart
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.VMFactory


@Composable
fun CaloriesChart(
    chartViewModel: CalorieChartViewModel = viewModel(
        factory = VMFactory.build {
            CalorieChartViewModel(MyApp.appModule.database)
        }
    ),
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    calorieUnit: CalorieUnits = config?.calorieUnit ?: AppConfig.internalDefaultCalorieUnit
) {

    LineChart(
        chartViewModel = chartViewModel,
        upperLowerBound = 100,
        title = "Calories",
        unit = calorieUnit.name,
        convertValues = {
            CalorieUnit.convert(to = calorieUnit, value = it)
        }
    )
}