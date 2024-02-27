package com.example.weightdojo.screens.home.addmodal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class AddMenuSubModals {
    Initial, AddWeight, AddCalories
}

class AddModalVm : ViewModel() {
    var currentSubModal by mutableStateOf(AddMenuSubModals.Initial)

    fun setCurrentAddMenuSubModals(subModal: AddMenuSubModals) {
        currentSubModal = subModal
    }

    fun backToInitial() {
        currentSubModal = AddMenuSubModals.Initial
    }
}