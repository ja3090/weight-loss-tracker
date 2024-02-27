package com.example.weightdojo.utils

import android.util.Log
import com.example.weightdojo.AppConfig
import com.example.weightdojo.AppModule
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.datatransferobjects.DayData

data class Stats(
    val calorieUnit: String,
    val weightUnit: String,
    val goalWeight: String,
    val weight: String,
    val calories: String
)


fun statsDisplayHelper(config: Config?, dayData: DayData?): Stats {
    val calorieUnit = MyApp.appModule.calorieUnit
    val weightUnit = config?.weightUnit ?: AppConfig.internalDefaultWeightUnit
    val weight = MyApp.appModule.weightFormatter(dayData?.day?.weight)
    val goalWeight = MyApp.appModule.weightFormatter(config?.goalWeight)
    val totalCalories = MyApp.appModule.calorieFormatter(dayData?.day?.totalCalories)

    return Stats(
        calorieUnit = MyApp.appModule.calorieUnit.name,
        weightUnit = MyApp.appModule.weightUnit.name,
        goalWeight = "${toTwoDecimalPlaces(goalWeight)} $weightUnit",
        weight = "${toTwoDecimalPlaces(weight)} $weightUnit",
        calories = "${toTwoDecimalPlaces(totalCalories)} $calorieUnit"
    )
}