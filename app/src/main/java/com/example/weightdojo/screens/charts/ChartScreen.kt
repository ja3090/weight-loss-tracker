package com.example.weightdojo.screens.charts

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weightdojo.screens.charts.screens.CaloriesChart
import com.example.weightdojo.screens.charts.screens.ChartMenu
import com.example.weightdojo.screens.charts.screens.WeightChart
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

enum class Charts { Calories, Weight, Menu }

@Composable
fun ChartScreen(
    navigator: NavHostController = rememberNavController(),
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
        startDestination = Charts.Menu.name,
//        modifier = Modifier.padding(it),
    ) {

        composable(route = Charts.Menu.name) {
            if (it.destination.route == Charts.Menu.name) {
                ChartMenu(navigateTo = ::navigateTo)
            }
        }
        composable(route = Charts.Weight.name) {
            if (it.destination.route == Charts.Weight.name) {
                WeightChart()
            }
        }
        composable(route = Charts.Calories.name) {
            if (it.destination.route == Charts.Calories.name) {
                CaloriesChart()
            }
        }
//        composable(route = Charts.Menu.name) {
//            ChartMenu(navigateTo = ::navigateTo)
//        }
//        composable(route = Charts.Weight.name) {
//            WeightChart()
//        }
//        composable(route = Charts.Calories.name) {
//            CaloriesChart()
//        }
    }
}