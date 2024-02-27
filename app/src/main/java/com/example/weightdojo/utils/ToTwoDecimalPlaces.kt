package com.example.weightdojo.utils

fun toTwoDecimalPlaces(number: Float?): String {
    if (number == null) return "-"

    if (number - number.toInt() == 0f) return "${number.toInt()}"

    return String.format("%.2f", number)
}