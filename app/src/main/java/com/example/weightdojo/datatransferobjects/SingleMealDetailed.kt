package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import java.util.UUID

data class SingleMealDetailedDTO(
    val nutrimentName: String?,
    val mealName: String,
    val mealId: Long,
    val dayId: Long,
    val totalCalories: Float,
    val ingredientId: Long,
    val gPer100: Float?,
    val grams: Float,
    val ingredientName: String,
    val mealIsTemplate: Boolean,
    val ingredientIsTemplate: Boolean,
    val nutrimentId: Long?,
    val caloriesPer100: Float
) {
    fun toIngredient(): SingleMealDetailedIngredient {
        return SingleMealDetailedIngredient(
            grams = grams,
            caloriesPer100 = caloriesPer100,
            ingredientId = ingredientId,
            ingredientName = ingredientName,
            nutriments = mutableListOf(),
            mealId = mealId,
            ingredientIsTemplate = ingredientIsTemplate,
        )
    }

    fun toNutriment(): SingleMealDetailedNutriment? {
        val absent = gPer100 == null && nutrimentId == null && nutrimentName == null

        if (absent) return null

        gPer100 as Float
        nutrimentId as Long
        nutrimentName as String

        return SingleMealDetailedNutriment(
            gPer100 = gPer100,
            nutrimentId = nutrimentId,
            nutrimentName = nutrimentName,
        )
    }

    fun toMeal(): SingleMealDetailed {
        return SingleMealDetailed(
            mealId = mealId,
            mealName = mealName,
            dayId = dayId,
            ingredients = mutableListOf(),
            totalCalories = totalCalories,
            mealIsTemplate = mealIsTemplate
        )
    }
}

data class SingleMealDetailedIngredient(
    val internalId: UUID = UUID.randomUUID(),
    val grams: Float = 0f,
    val ingredientId: Long = 0,
    val ingredientName: String = "",
    val nutriments: MutableList<SingleMealDetailedNutriment> = mutableListOf(),
    val caloriesPer100: Float = 0f,
    val mealId: Long? = null,
    val ingredientIsTemplate: Boolean = false,
    val markedFor: Marked = Marked.EDIT
) {
    fun toIngredient(): Ingredient {
        return Ingredient(
            ingredientId = ingredientId,
            mealId = mealId,
            name = ingredientName,
            caloriesPer100 = caloriesPer100,
            isTemplate = ingredientIsTemplate,
            grams = grams
        )
    }
}

data class SingleMealDetailedNutriment(
    val nutrimentId: Long = 0,
    val nutrimentName: String = "",
    val gPer100: Float = 0F,
    val internalId: UUID = UUID.randomUUID(),
    val gPer100AsString: String = gPer100.toString(),
)

data class SingleMealDetailed(
    val mealName: String = "",
    val mealId: Long = 0,
    val dayId: Long? = 0,
    val totalCalories: Float = 0f,
    val ingredients: MutableList<SingleMealDetailedIngredient> = mutableListOf(),
    val mealIsTemplate: Boolean = false,
) {
    fun toMeal(): Meal {
        return Meal(
            mealId = mealId,
            name = mealName,
            totalCalories = totalCalories,
            dayId = dayId,
            isTemplate = mealIsTemplate
        )
    }
}

class SingleMealBuilder(
    private val rows: List<SingleMealDetailedDTO>
) {
    var data: SingleMealDetailed? = null
    private val nutrimentsByIngredient: MutableMap<Long, SingleMealDetailedIngredient> =
        mutableMapOf()

    init {
        format()
    }

    private fun format() {
        for (row in rows) {
            if (data == null) data = row.toMeal()

            val ingredient = row.toIngredient()

            nutrimentsByIngredient.putIfAbsent(
                ingredient.ingredientId,
                ingredient
            )

            val nutriment = row.toNutriment() ?: continue

            nutrimentsByIngredient[ingredient.ingredientId]?.nutriments?.add(nutriment)
        }

        for (key in nutrimentsByIngredient.keys) {
            val ingredient = nutrimentsByIngredient[key] ?: throw Exception("Data " +
                    "failed. Please check SingleMealDetailed Data Transfer Object")

            data?.ingredients?.add(ingredient)
        }
    }
}