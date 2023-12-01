package com.example.weightdojo.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.LockFirstTime.LockFirstTime

enum class Screens {
    LockFirstTime,
    Lock,
    Home,
    Entries,
    Settings,
    Graphs
}

fun getStartDest(config: Config?): Enum<Screens> {
    return if (config == null) Screens.LockFirstTime
    else if (config.passcodeEnabled) Screens.Lock
    else Screens.Home
}

@Composable
fun MainScreen(
    config: Config?,
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = getStartDest(config).name
    ) {
        composable(route = Screens.LockFirstTime.name) {
            LockFirstTime(
                navHostController = navHostController
            )
        }
        composable(route = Screens.Lock.name) {
            Lock()
        }
        composable(route = Screens.Home.name) {
            Home()
        }
    }
}