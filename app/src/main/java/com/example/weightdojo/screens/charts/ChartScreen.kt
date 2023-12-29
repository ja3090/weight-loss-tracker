package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weightdojo.screens.charts.screens.CaloriesChart
import com.example.weightdojo.screens.charts.screens.ChartMenu
import com.example.weightdojo.screens.charts.screens.WeightChart

enum class Charts { Calories, Weight, Menu }

@Composable
fun ChartScreen(
    navigator: NavHostController = rememberNavController()
) {

    fun navigateTo(chartScreens: Charts) {
        when (chartScreens) {
            Charts.Calories -> navigator.navigate(Charts.Calories.name)
            Charts.Weight -> navigator.navigate(Charts.Weight.name)
            Charts.Menu -> navigator.navigate(Charts.Menu.name)
        }
    }

    NavHost(
        navController = navigator,
        startDestination = Charts.Menu.name
    ) {

        composable(route = Charts.Menu.name) {
            ChartMenu(navigateTo = ::navigateTo)
        }
        composable(route = Charts.Weight.name) {
            WeightChart()
        }
        composable(route = Charts.Calories.name) {
            CaloriesChart()
        }
    }
}