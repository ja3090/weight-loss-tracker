package com.example.weightdojo.screens.home.addmodal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import com.example.weightdojo.database.models.DayWithMeals
import com.example.weightdojo.screens.home.addmodal.addweight.AddWeight
import com.example.weightdojo.screens.main.Screens

enum class SubModals {
    Initial, SetWeight
}

@Composable
fun AddModal(
    showModal: (show: Boolean) -> Unit,
    navigateTo: (screen: Screens) -> Unit,
    dayData: DayWithMeals?
) {
    var currentSubModal by remember {
        mutableStateOf(SubModals.Initial)
    }

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
            SubModals.SetWeight -> AddWeight(dayData = dayData, showModal = showModal)
        }
    }
}