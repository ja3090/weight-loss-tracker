package com.example.weightdojo.datatransferobjects

data class SingleIngredientDetailedDTO(
    val nutrimentName: String,
    val mealId: Long?,
    val ingredientId: Long,
    val gPer100: Float,
    val grams: Float,
    val ingredientName: String,
    val ingredientIsTemplate: Boolean,
    val nutrimentId: Long,
    val caloriesPer100: Float
) {
    fun toIngredient(): SingleMealDetailedIngredient {
        return SingleMealDetailedIngredient(
            grams = grams,
            ingredientId = ingredientId,
            ingredientName = ingredientName,
            ingredientIsTemplate = ingredientIsTemplate,
            caloriesPer100 = caloriesPer100,
            mealId = mealId
        )
    }

    fun toNutriment(): SingleMealDetailedNutriment {
        return SingleMealDetailedNutriment(
            nutrimentId = nutrimentId,
            nutrimentName = nutrimentName,
            gPer100 = gPer100
        )
    }
}

class SingleIngredientDetailedBuilder(
    private val rows: List<SingleIngredientDetailedDTO>
) {
    var data: SingleMealDetailedIngredient? = null

    init {
        format()
    }

    private fun format() {
        for (row in rows) {
            if (data == null) data = row.toIngredient()

            val nutriment = row.toNutriment()

            data?.nutriments?.add(nutriment)
        }
    }
}