package com.example.weightdojo.datatransferobjects

interface NutrimentSummary {
    val nutrimentId: Long
    val grams: Float
    val nutrimentName: String
}

data class NutrimentBreakdownMeal(
    override val nutrimentId: Long,
    private val totalGrams: Float,
    override val nutrimentName: String
) : NutrimentSummary {
    override val grams: Float
        get() = totalGrams
}

data class NutrimentBreakdownIngredient(
    override val nutrimentId: Long,
    private val gPer100: Float,
    override val nutrimentName: String
) : NutrimentSummary {
    override val grams: Float
        get() = gPer100
}