package com.example.weightdojo.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.WeightUnit
import com.example.weightdojo.utils.WeightUnits

data class SettingsState(
    val config: Config?, val weightUnitOpen: Boolean = false, val calorieUnitOpen: Boolean = false
)

class SettingsVm(
    private val configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    private val config: Config? = configSessionCache.getActiveSession()
) : ViewModel() {

    var state by mutableStateOf(SettingsState(config = config))

    fun weightUnitDropDownSetter(boolean: Boolean) {
        state = state.copy(weightUnitOpen = boolean)
    }

    fun calorieUnitDropDownSetter(boolean: Boolean) {
        state = state.copy(calorieUnitOpen = boolean)
    }

    fun setGoalWeight(string: String) {
        val newConfig = state.config?.copy(goalWeight = if (string.isEmpty()) {
            null
        } else {
            config?.weightUnit?.let { WeightUnit.convert(from = it, value = string.toFloat()) }
        })

        configSetter(newConfig)
    }

    fun setWeightUnit(weightUnit: WeightUnits) {
        val newConfig = state.config?.copy(weightUnit = weightUnit)

        configSetter(newConfig)
    }

    fun setCalorieUnit(calorieUnit: CalorieUnits) {
        val newConfig = state.config?.copy(calorieUnit = calorieUnit)

        configSetter(newConfig)
    }

    private fun configSetter(newConfig: Config?) {
        state = state.copy(
            config = newConfig
        )

        configSessionCache.saveSession(newConfig)
    }
}