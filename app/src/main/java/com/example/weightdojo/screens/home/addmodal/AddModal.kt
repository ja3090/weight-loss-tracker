package com.example.weightdojo.screens.home.addmodal

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCalories
import com.example.weightdojo.screens.home.addmodal.addweight.AddWeight
import com.example.weightdojo.screens.main.Screens

@Composable
fun AddModal(
    dayData: DayData?,
    homeViewModel: HomeViewModel = viewModel(),
    addModalVm: AddModalVm = viewModel()
) {

    val homeState = homeViewModel.state

    Dialog(
        onDismissRequest = { homeViewModel.showModal(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        when (addModalVm.currentSubModal) {
            AddMenuSubModals.Initial -> Initial()
            AddMenuSubModals.AddWeight -> AddWeight(dayData = dayData)
            AddMenuSubModals.AddCalories -> AddCalories(dayId = homeState.dayData?.day?.id)
        }
    }
}