package com.example.weightdojo.utils

import com.example.weightdojo.AppConfig
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.DayWithWeightAndMeals

data class Stats(
    val calorieUnit: String,
    val weightUnit: String,
    val goalWeight: String,
    val weight: String,
    val tdee: String,
    val calories: String
)

fun statsDisplayHelper(config: Config?, day: DayWithWeightAndMeals?): Stats {
    val calorieUnit = config?.calorieUnit ?: AppConfig.internalDefaultCalorieUnit

    val weightUnit = config?.weightUnit?.name ?: "Kg"

    val goalWeight = if (config?.goalWeight !== null) {
        config.goalWeight.toInt().toString() + " " + weightUnit
    } else "-"

    val weight = if (day?.weight?.weight !== null) {
        day.weight.weight.toInt().toString() + " " + weightUnit
    } else "-"

    val tdee = if (config?.tdee !== null) {
        config.tdee.toString() + calorieUnit.name
    } else "-"

    val calories = if (day?.day?.totalCalories !== null) {
        day?.day?.totalCalories.toInt().toString() + " " + calorieUnit
    } else "-"

    return Stats(
        calorieUnit = calorieUnit.name,
        weightUnit = weightUnit,
        goalWeight = goalWeight,
        weight = weight,
        tdee = tdee,
        calories = calories
    )
}