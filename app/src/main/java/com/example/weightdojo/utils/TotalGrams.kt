package com.example.weightdojo.utils

fun totalGramsNullable(grams: Float, amountPer100: Float?): Float? {
    return if (amountPer100 == null) null else (grams / 100) * amountPer100
}

fun totalGramsNonNull(grams: Float, amountPer100: Float): Float {
    return (grams / 100) * amountPer100
}