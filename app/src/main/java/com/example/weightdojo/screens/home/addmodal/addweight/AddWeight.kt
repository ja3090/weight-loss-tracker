package com.example.weightdojo.screens.home.addmodal.addweight

import android.util.Log
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
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.DayWithMeals
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.WeightUnits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun validateInput(newText: String): Boolean {
    if (newText.isEmpty()) return true
    if (newText.count() > 6) return false
    if (newText.startsWith("0")) return false
    if (newText.contains('.') && newText.startsWith(".")) return false
    if (newText.count { it == '.' } > 1) return false

    return newText.all { it.isDigit() || it == '.' }
}

fun initialWeight(dayData: DayWithMeals?): String {
    return if (dayData == null) ""
    else if (dayData.day.weight == null) ""
    else dayData.day.weight.toString()
}

@Composable
fun AddWeight(
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    config: Config? = configSessionCache.getActiveSession(),
    dayData: DayWithMeals?,
    addWeightVM: AddWeightVM = viewModel(factory = VMFactory.build {
        AddWeightVM(database = MyApp.appModule.database, dayData = dayData)
    }),
    showModal: (show: Boolean) -> Unit
) {
    val weightUnit = when (config?.weightUnit) {
        WeightUnits.LBS -> "LBS"
        else -> AppConfig.internalDefaultWeightUnit.name
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.secondary)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Heading(
                text = "Set Weight",
                modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )
            CustomDivider(tinted = false)
        }

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

            Box(modifier = Modifier
                .clip(
                    RoundedCornerShape(Sizing.cornerRounding)
                )
                .background(MaterialTheme.colors.primaryVariant)
                .clickable {
                    addWeightVM.viewModelScope.launch(Dispatchers.IO) {
                        val success = addWeightVM.submit()

                        if (success) showModal(false)
                    }
                }) {
                TextDefault(
                    text = "Save",
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.padding(horizontal = 50.dp, vertical = 20.dp)
                )
            }
        }
    }
}