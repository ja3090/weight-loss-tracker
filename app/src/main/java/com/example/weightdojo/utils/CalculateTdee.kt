package com.example.weightdojo.utils

import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Sex

fun calculateTdee(
    age: Int?,
    sex: Sex?,
    height: Float?,
    weight: Float?,
    calorieUnit: CalorieUnits?,
    weightUnit: WeightUnits?
): String? {
    if (age == null || sex == null || height == null || weight == null || calorieUnit == null || weightUnit == null) {
        return null
    }

    val weightToKg = WeightUnit.convert(
        from = weightUnit,
        to = WeightUnits.KG,
        value = weight
    )

    if (sex == Sex.Male) {
        val bmr = 88.362 + (13.397 * weightToKg) + (4.799 * height) - (5.677 * age)

        val tdee = CalorieUnit.convert(
            from = AppConfig.internalDefaultCalorieUnit,
            to = calorieUnit,
            value = (bmr * 1.2).toFloat()
        )

        return tdee.toInt().toString() + " " + calorieUnit.name
    }

    if (sex == Sex.Female) {
        val bmr = 447.593 + (9.247 * weightToKg) + (3.098 * height) - (4.330 * age)

        val tdee = CalorieUnit.convert(
            from = AppConfig.internalDefaultCalorieUnit,
            to = calorieUnit,
            value = (bmr * 1.2).toFloat()
        )

        return tdee.toInt().toString() + " " + calorieUnit.name
    }

    return null
}