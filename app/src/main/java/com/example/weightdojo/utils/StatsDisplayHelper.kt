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
    val weightUnit = config?.weightUnit ?: AppConfig.internalDefaultWeightUnit
    val weight = dayData?.day?.weight
    val goalWeight = config?.goalWeight
    val totalCalories = dayData?.day?.totalCalories

    val goalWeightAsString = if (goalWeight !== null) "${
        WeightUnit.convert(
            from = AppConfig.internalDefaultWeightUnit,
            to = weightUnit,
            value = goalWeight
        )
    } $weightUnit" else "-"
    
    val weightAsString = if (weight !== null) "${
        WeightUnit.convert(
            to = weightUnit,
            from = AppConfig.internalDefaultWeightUnit,
            value = weight
        )
    } $weightUnit" else "-"

    val caloriesAsString =
        if (totalCalories !== null) "${
            CalorieUnit.convert(
                to = calorieUnit,
                value = totalCalories,
                from = AppConfig.internalDefaultCalorieUnit
            ).toInt()
        } $calorieUnit" else "-"

    return Stats(
        calorieUnit = calorieUnit.name,
        weightUnit = weightUnit.name,
        goalWeight = goalWeightAsString,
        weight = weightAsString,
        calories = caloriesAsString
    )
}