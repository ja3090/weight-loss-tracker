package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.DayWithWeightAndMeals
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.WeightUnits

fun validateInput(newText: String): Boolean {
    if (newText.isEmpty()) return true
    if (newText.count() > 6) return false
    if (newText.startsWith("0")) return false
    if (newText.contains('.') && newText.startsWith(".")) return false
    if (newText.count { it == '.' } > 1) return false

    return newText.all { it.isDigit() || it == '.' }
}

fun validateSubmission(weight: String) {}

@Composable
fun SetWeight(
    configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    config: Config? = configSessionCache.getActiveSession(),
    dayData: DayWithWeightAndMeals?
) {
    val weightUnit = when (config?.weightUnit) {
        WeightUnits.LBS -> "LBS"
        else -> AppConfig.internalDefaultWeightUnit.name
    }

    var weight by remember { mutableStateOf("") }

    fun weightSetter(newWeight: String) {
        weight = newWeight
    }

    val extraOptions by remember {
        mutableStateOf(ExtraOptions(
            sex = config?.sex,
            age = if (config?.age == null) null else config?.age.toString(),
            height = config?.height,
        ))
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxSize()
    ) {
        Heading(text = "Set Weight")
        CustomDivider(tinted = false)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeightInput(weight = weight, weightUnit = weightUnit, weightSetter = ::weightSetter)

            ConfigOptions(extraOptions = extraOptions)
            }
        }
    }


data class ExtraOptions(
    private val config: Config?,
    private val dayData: DayWithWeightAndMeals?,
    var sex: Sex?,
    var age: String?,
    var weight: String?,
) {
    init {
        if (config == null) {
            sex = null
            age = null
            weight = null
        } else {
            sex = config.sex
            age = config.age
            weight = dayData.weight.weight
        }
    }
}