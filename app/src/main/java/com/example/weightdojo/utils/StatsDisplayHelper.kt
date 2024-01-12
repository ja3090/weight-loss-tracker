package com.example.weightdojo.utils

import com.example.weightdojo.AppConfig
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
    val calorieUnit = config?.calorieUnit ?: AppConfig.internalDefaultCalorieUnit
    val weightUnit = config?.weightUnit?.name ?: "KG"
    val weight = dayData?.day?.weight
    val goalWeight = config?.goalWeight
    val totalCalories = dayData?.day?.totalCalories

    val goalWeightAsString = if (goalWeight !== null) "$goalWeight $weightUnit" else "-"
    val weightAsString = if (weight !== null) "$weight $weightUnit" else "-"
    val caloriesAsString =
        if (totalCalories !== null) "${totalCalories.toInt()} $calorieUnit" else "-"

    return Stats(
        calorieUnit = calorieUnit.name,
        weightUnit = weightUnit,
        goalWeight = goalWeightAsString,
        weight = weightAsString,
        calories = caloriesAsString
    )
}