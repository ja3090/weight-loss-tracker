package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.ui.Sizing

@Composable
fun Initial(
    addModalVm: AddModalVm = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    ModalFrame(
        title = "Add",
        onBack = { homeViewModel.showModal(false) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { addModalVm.setCurrentAddMenuSubModals(AddMenuSubModals.AddWeight) }
        ) {
            TextDefault(
                text = "Weight",
                modifier = Modifier
                    .padding(Sizing.paddings.medium),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { addModalVm.setCurrentAddMenuSubModals(AddMenuSubModals.AddCalories) }
        ) {
            TextDefault(
                text = "Calories",
                modifier = Modifier
                    .padding(Sizing.paddings.medium),
            )
        }
    }
}
