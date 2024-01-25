package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.WeightUnits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun initialWeight(dayData: DayData?): String {
    return if (dayData == null) ""
    else if (dayData.day.weight == null) ""
    else dayData.day.weight.toString()
}

@Composable
fun AddWeight(
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    config: Config? = configSessionCache.getActiveSession(),
    dayData: DayData?,
    addWeightVM: AddWeightVM = viewModel(factory = VMFactory.build {
        AddWeightVM(database = MyApp.appModule.database, dayData = dayData)
    }),
    homeViewModel: HomeViewModel = viewModel()
) {
    val weightUnit = when (config?.weightUnit) {
        WeightUnits.LBS -> "LBS"
        else -> AppConfig.internalDefaultWeightUnit.name
    }

    ModalFrame(title = "Add Weight") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Sizing.paddings.medium),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeightInput(
                weight = addWeightVM.weight,
                weightUnit = weightUnit,
                weightSetter = addWeightVM::weightSetter
            )

            ConfigOptions(extraOptions = addWeightVM.extraOptions)
            CustomButton(buttonName = "Save") {
                addWeightVM.viewModelScope.launch(Dispatchers.IO) {
                    val success = addWeightVM.submit()

                    if (success) homeViewModel.showModal(false)
                }
            }
        }
    }
}