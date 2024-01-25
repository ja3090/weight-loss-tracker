package com.example.weightdojo.screens.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.home.addmodal.AddModal
import com.example.weightdojo.screens.home.caloriesdisplay.CaloriesDisplay
import com.example.weightdojo.screens.home.statsdisplay.StatsDisplay
import com.example.weightdojo.screens.main.Screens
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.VMFactory

@SuppressLint("NewApi")
@Composable
fun Home(
    navigateTo: (screen: Screens?) -> Unit,
    homeViewModel: HomeViewModel = viewModel(
        factory = VMFactory.build {
            HomeViewModel(MyApp.appModule.database)
        }
    ),
    homeState: HomeState = homeViewModel.state,
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    config: Config? = configSessionCache.getActiveSession()
) {
    val weightUnit = config?.calorieUnit?.name ?: AppConfig.internalDefaultWeightUnit.name

    BackHandler {
        navigateTo(null)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Sizing.paddings.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Heading(text = "Home")

            Row(
                modifier = Modifier.clickable { homeViewModel.showModal(true) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextDefault(text = "Add", modifier = Modifier.padding(end = 5.dp))
                IconBuilder(id = R.drawable.add, contentDescription = "Add", testTag = "ADD_BUTTON")
            }
        }

        if (homeState.showAddModal) {
            AddModal(
                dayData = homeState.dayData
            )
        }

        DayPicker(todayFullDate = homeState.currentDate, dateSetter = homeViewModel::getAndSetDay)

        StatsDisplay(dayData = homeState.dayData, mostRecentWeight = homeState.mostRecentWeight)

        CaloriesDisplay(meals = homeState.dayData?.meals, weightUnit = weightUnit)
    }
}
