package com.example.weightdojo.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.charts.Chart
import com.example.weightdojo.screens.charts.ChartScreen
import com.example.weightdojo.screens.home.Home
import com.example.weightdojo.screens.lock.Lock
import com.example.weightdojo.screens.lockfirsttime.LockFirstTime
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel(
        factory = VMFactory.build {
            MainViewModel(MyApp.appModule.database)
        }
    ),
    config: Config? = mainViewModel.state.config,
) {

    val context = LocalContext.current as FragmentActivity

    NavHost(
        navController = navHostController,
        startDestination = Screens.Charts.name
    ) {
        composable(route = Screens.LockFirstTime.name) {
            LockFirstTime(
                onSubmitRedirect = {
                    mainViewModel.viewModelScope.launch {
//                        mainViewModel.setAuthenticated(true)
                        navHostController.navigate(Screens.Home.name)
                    }
                },
                context = context
            )
        }
        composable(route = Screens.Lock.name) {
            Lock(
                config = config!!,
                context = context,
                redirectToHome = {
                    mainViewModel.viewModelScope.launch {
                        mainViewModel.setAuthenticated(true)
                        navHostController.navigate(Screens.Home.name)
                    }
                },
                isAuthenticated = mainViewModel.state.authenticated,
            )
        }
        composable(route = Screens.Home.name) {
            Home(
                currentDate = mainViewModel.state.currentDate,
                dateSetter = mainViewModel::setDate
            )
        }
        composable(route = Screens.Charts.name) {
            ChartScreen()
        }
    }

}