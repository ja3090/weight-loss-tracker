package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.dialogs.ErrorDialog
import com.example.weightdojo.components.successToast
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.AddModalVm
import com.example.weightdojo.screens.home.addmodal.ModalFrame
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.WeightUnit
import com.example.weightdojo.utils.WeightUnits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun initialWeight(
    dayData: DayData?,
    to: WeightUnits?
): String {
    val toWeightUnit = to ?: AppConfig.internalDefaultWeightUnit

    return if (dayData == null) ""
    else if (dayData.day.weight == null) ""
    else WeightUnit.convert(to = toWeightUnit, value = dayData.day.weight).toString()
}

@Composable
fun AddWeight(
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    config: Config? = configSessionCache.getActiveSession(),
    dayData: DayData?,
    addWeightVM: AddWeightVM = viewModel(factory = VMFactory.build {
        AddWeightVM(database = MyApp.appModule.database, dayData = dayData)
    }),
    homeViewModel: HomeViewModel = viewModel(),
    addModalVm: AddModalVm = viewModel(),
) {
    val weightUnit = when (config?.weightUnit) {
        WeightUnits.LBS -> "LBS"
        else -> AppConfig.internalDefaultWeightUnit.name
    }

    val context = LocalContext.current

    suspend fun onSuccess() {
        withContext(Dispatchers.Main) {
            homeViewModel.showModal(false)
            homeViewModel.refresh()
            addModalVm.backToInitial()
            successToast(message = "Success", context = context)
        }
    }

    ErrorDialog(
        onConfirm = { addWeightVM.error = null },
        title = "Error",
        text = addWeightVM.error
    )

    ModalFrame(
        title = "Add Weight",
        onClose = { homeViewModel.showModal(false) },
        onBack = {
        addModalVm.backToInitial()
    }) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(Sizing.paddings.medium),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeightInput(
                weight = addWeightVM.weight,
                weightUnit = weightUnit,
                weightSetter = addWeightVM::weightSetter
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = addWeightVM.showExtraOptions,
                    onCheckedChange = {
                        addWeightVM.showExtraOptions()
                    }
                )
                TextDefault(text = "Show Extra Options")
            }
            if (addWeightVM.showExtraOptions) {
                ConfigOptions(extraOptions = addWeightVM.extraOptions)
            }
            CustomButton(buttonName = "Save") {
                addWeightVM.viewModelScope.launch(Dispatchers.IO) {
                    val success = addWeightVM.submit()

                    if (success) onSuccess()
                }
            }
        }
    }
}