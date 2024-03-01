package com.example.weightdojo.datatransferobjects


data class MealWithNutrimentDataDTO(
    val nutrimentName: String,
    val mealName: String,
    val totalCalories: Float,
    val totalGrams: Float,
    val nutrimentId: Long,
    val mealId: Long
) {
    fun toMealWithNutrimentData(): MealWithNutrimentData {
        return MealWithNutrimentData(
            totalCalories = totalCalories,
            mealName = mealName,
            mealId = mealId,
            nutriments = mutableListOf()
        )
    }

    fun toNutrimentBreakdownMeal(): NutrimentBreakdownMeal {
        return NutrimentBreakdownMeal(
            nutrimentId, totalGrams, nutrimentName
        )
    }
}

class MealWithNutrimentDataBuilder(
    private val rows: List<MealWithNutrimentDataDTO>
) {
    var data: MutableList<MealWithNutrimentData> = mutableListOf()
    private val mapByMealId: MutableMap<Long, MealWithNutrimentData> = mutableMapOf()

    init {
        format()
    }

    private fun format() {
        for (row in rows) {
            mapByMealId.putIfAbsent(row.mealId, row.toMealWithNutrimentData())

            mapByMealId[row.mealId]?.nutriments?.add(row.toNutrimentBreakdownMeal())
        }

        data = mapByMealId.values.toMutableList()
    }
}

data class MealWithNutrimentData(
    private val totalCalories: Float,
    private val mealName: String,
    private val mealId: Long,
    override var nutriments: MutableList<NutrimentBreakdownMeal>,
) : NutritionBreakdown<NutrimentBreakdownMeal> {
    override val calories: Float
        get() = totalCalories
    override val id: Long
        get() = mealId
    override val name: String
        get() = mealName
}