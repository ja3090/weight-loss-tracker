package com.example.weightdojo.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.ConfirmModal
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.screens.main.Screens
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.VMFactory
import com.example.weightdojo.utils.WeightUnit
import com.example.weightdojo.utils.WeightUnits
import com.example.weightdojo.utils.validateInput
import faker.com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic

fun getGoalWeight(config: Config): Float? {
    return if (config.goalWeight == null) null else {
        WeightUnit.convert(
            from = AppConfig.internalDefaultWeightUnit,
            to = config.weightUnit,
            value = config.goalWeight
        )
    }
}

@Composable
fun Settings(
    settingsVm: SettingsVm = viewModel(factory = VMFactory.build {
        SettingsVm(MyApp.appModule.configSessionCache)
    }),
    moveToLockFirstTimeScreen: () -> Unit,
    navigateTo: (screen: Screens) -> Unit
) {
    val config = settingsVm.state.config ?: return

    val goalWeight = getGoalWeight(config)

    var goalWeightInput by remember(config) {
        mutableStateOf(
            if (goalWeight == null || goalWeight == 0f) "" else goalWeight.toString()
        )
    }

    val confirmRemovePasscode = ConfirmModal(
        title = "Remove Passcode",
        text = "Do you want to remove the passcode from this app?"
    ) {
        settingsVm.removePasscode()
        navigateTo(Screens.Home)
    }

    confirmRemovePasscode.Modal()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Heading(text = "Settings", modifier = Modifier.padding(horizontal = Sizing.paddings.small))
        CustomDivider(tinted = false)
        TextDefault(
            text = "General", fontWeight = FontWeight.Bold, modifier = Modifier.padding(
                vertical = Sizing.paddings.medium, horizontal = Sizing.paddings.small
            )
        )
        SelectUnit(
            setter = {
                settingsVm.setWeightUnit(
                    WeightUnits.valueOf(it.name)
                )
            },
            options = WeightUnits.entries,
            title = "Weight Unit",
            open = settingsVm::weightUnitDropDownSetter,
            isOpen = settingsVm.state.weightUnitOpen,
            currentSelected = settingsVm.state.config?.weightUnit
                ?: AppConfig.internalDefaultWeightUnit,
            modifier = Modifier.padding(
                horizontal = Sizing.paddings.small
            )
        )
        CustomDivider()
        SelectUnit(
            setter = {
                settingsVm.setCalorieUnit(
                    CalorieUnits.valueOf(it.name)
                )
            },
            options = CalorieUnits.entries,
            title = "Calorie Unit",
            open = settingsVm::calorieUnitDropDownSetter,
            isOpen = settingsVm.state.calorieUnitOpen,
            currentSelected = settingsVm.state.config?.calorieUnit
                ?: AppConfig.internalDefaultCalorieUnit,
            modifier = Modifier.padding(
                horizontal = Sizing.paddings.small
            )
        )
        CustomDivider()
        Input(
            InputArgs(inputValue = goalWeightInput,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    val passes = validateInput(it)

                    if (passes) {
                        goalWeightInput = it
                        settingsVm.setGoalWeight(goalWeightInput)
                    }
                },
                placeholder = {
                    TextDefault(
                        text = "Goal Weight", color = MaterialTheme.colors.primaryVariant.copy(0.5f)
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                modifier = Modifier.fillMaxSize(),
                trailingIcon = {
                    TextDefault(
                        text = settingsVm.state.config?.weightUnit?.name
                            ?: AppConfig.internalDefaultWeightUnit.name
                    )
                })
        )
        CustomDivider()
        if (config.passcodeEnabled) {
            SettingsButton(text = "Remove Passcode") {
                confirmRemovePasscode.openOrClose(true)
            }
        } else {
            SettingsButton(text = "Set Up Passcode") {
                moveToLockFirstTimeScreen()
            }
        }
    }
}
