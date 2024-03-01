package com.example.weightdojo.datatransferobjects

data class IngredientWithNutrimentDataDTO(
    val nutrimentName: String,
    val ingredientName: String,
    val caloriesPer100: Float,
    val gPer100: Float,
    val nutrimentId: Long,
    val ingredientId: Long
) {
    fun toIngredientWithNutrimentData(): IngredientWithNutrimentData {
        return IngredientWithNutrimentData(
            caloriesPer100 = caloriesPer100,
            ingredientName = ingredientName,
            ingredientId = ingredientId,
            nutriments = mutableListOf()
        )
    }

    fun toNutrimentBreakdownIngredient(): NutrimentBreakdownIngredient {
        return NutrimentBreakdownIngredient(
            gPer100 = gPer100,
            nutrimentId = nutrimentId,
            nutrimentName = nutrimentName
        )
    }
}

data class IngredientWithNutrimentData(
    private val caloriesPer100: Float,
    private val ingredientName: String,
    private val ingredientId: Long,
    override var nutriments: MutableList<NutrimentBreakdownIngredient>
) : NutritionBreakdown<NutrimentBreakdownIngredient> {
    override val name: String
        get() = ingredientName
    override val calories: Float
        get() = caloriesPer100
    override val id: Long
        get() = ingredientId
}

class IngredientWithNutrimentDataBuilder(
    private val rows: List<IngredientWithNutrimentDataDTO>
) {
    var data: MutableList<IngredientWithNutrimentData> = mutableListOf()
    private val mapByMealId: MutableMap<Long, IngredientWithNutrimentData> = mutableMapOf()

    init {
        format()
    }

    private fun format() {
        for (row in rows) {
            mapByMealId.putIfAbsent(row.ingredientId, row.toIngredientWithNutrimentData())

            mapByMealId[row.ingredientId]?.nutriments?.add(row.toNutrimentBreakdownIngredient())
        }

        data = mapByMealId.values.toMutableList()
    }
}