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

enum class SubModals {
    Initial, AddWeight, AddCalories
}

@Composable
fun AddModal(
    showModal: (show: Boolean) -> Unit,
    navigateTo: (screen: Screens) -> Unit,
    dayData: DayData?
) {
    var currentSubModal by remember {
        mutableStateOf(SubModals.Initial)
    }

    val homeViewModel: HomeViewModel = viewModel()
    val homeState = homeViewModel.state

    fun setCurrentSubModal(subModal: SubModals) {
        currentSubModal = subModal
    }

    Dialog(
        onDismissRequest = { showModal(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        when (currentSubModal) {
            SubModals.Initial -> Initial(::setCurrentSubModal)
            SubModals.AddWeight -> AddWeight(dayData = dayData, showModal = showModal)
            SubModals.AddCalories -> AddCalories(dayId = homeState.dayData?.day?.id)
        }
    }
}