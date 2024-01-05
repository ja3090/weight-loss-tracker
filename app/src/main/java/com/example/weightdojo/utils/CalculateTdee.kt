package com.example.weightdojo.utils

import com.example.weightdojo.database.models.Sex

fun calculateTdee(
    age: Int?, sex: Sex?, height: Float?, weight: Float?, weightUnit: WeightUnits?
): String? {
    if (age == null || sex == null || height == null || weight == null || weightUnit == null) {
        return null
    }

    if (sex == Sex.Male) {
        val bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)

        val tdee = bmr * 1.2

        return tdee.toInt().toString() + " " + weightUnit.name
    }

    if (sex == Sex.Female) {
        val bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)

        val tdee = bmr * 1.2

        return tdee.toInt().toString() + " " + weightUnit.name
    }

    return null
}