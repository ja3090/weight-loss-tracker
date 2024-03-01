package com.example.weightdojo.datatransferobjects

interface NutritionBreakdown<T : NutrimentSummary> {
    val calories: Float
    val name: String
    val id: Long
    val nutriments: MutableList<T>
}