package com.example.weightdojo.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.AddModal
import com.example.weightdojo.screens.home.statsdisplay.StatsDisplay
import com.example.weightdojo.screens.main.Screens
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory
import kotlin.random.Random

@SuppressLint("NewApi")
@Composable
fun Home(
    navigateTo: (screen: Screens) -> Unit,
    homeViewModel: HomeViewModel = viewModel(
        factory = VMFactory.build {
            HomeViewModel(MyApp.appModule.database)
        }
    ),
    homeState: HomeState = homeViewModel.state
) {
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
                showModal = homeViewModel::showModal,
                navigateTo = navigateTo,
                dayData = homeState.day
            )
        }

        DayPicker(todayFullDate = homeState.currentDate, dateSetter = homeViewModel::getAndSetDay)

        StatsDisplay(day = homeState.day, mostRecentWeight = homeState.mostRecentWeight)
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        Sizing.cornerRounding,
                        Sizing.cornerRounding,
                        0.dp,
                        0.dp
                    )
                )
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Heading(text = "Meals")
                CustomDivider(
                    modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
                )
                repeat(3) {
                    val randomKcal = (it + 1) * Random.nextInt(500)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Sizing.paddings.medium),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextDefault(text = "Meal #$it")
                        TextDefault(text = randomKcal.toString() + "kcal")
                    }
                }
            }
        }
    }
}
