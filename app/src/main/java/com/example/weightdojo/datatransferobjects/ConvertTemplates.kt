package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate

typealias MealStateAndIngredientList = Pair<MealState, List<IngredientState>>

class ConvertTemplates {
    fun makeMealStateAndIngredientList(
        mealTemplate: MealTemplate? = null,
        ingredientTemplateList: List<IngredientTemplate> = listOf(),
        dayId: Long?
    ): MealStateAndIngredientList {
        val meal = MealState(
            mealTemplateId = mealTemplate?.mealTemplateId ?: 0,
            name = mealTemplate?.name ?: "",
            totalProtein = mealTemplate?.totalProtein ?: 0f,
            totalFat = mealTemplate?.totalFat ?: 0f,
            totalCarbohydrates = mealTemplate?.totalCarbohydrates ?: 0f,
            totalCalories = mealTemplate?.totalCalories ?: 0f,
            dayId = dayId
        )

        val ingredientList = convertToIngredientList(ingredientTemplateList)

        return Pair(meal, ingredientList)
    }

    fun convertToIngredientList(
        ingredients: List<IngredientTemplate>
    ): List<IngredientState> {
        val ingredientList = mutableListOf<IngredientState>()

        for (ingredient in ingredients) {
            val toIngredient = toIngredientState(ingredient)

            ingredientList.add(toIngredient)
        }

        return ingredientList
    }

    fun toIngredientState(ingTemplate: IngredientTemplate): IngredientState {
        return IngredientState(
            caloriesPer100 = ingTemplate.caloriesPer100,
            grams = ingTemplate.grams,
            name = ingTemplate.name,
            ingredientId = 0,
            carbohydratesPer100 = ingTemplate.carbohydratesPer100,
            fatPer100 = ingTemplate.fatPer100,
            proteinPer100 = ingTemplate.proteinPer100,
            ingredientTemplateId = ingTemplate.ingredientTemplateId
        )
    }

    fun toIngredientTemplate(ingredientState: IngredientState): IngredientTemplate {
        return IngredientTemplate(
            ingredientTemplateId = ingredientState.ingredientTemplateId,
            grams = ingredientState.grams,
            carbohydratesPer100 = ingredientState.carbohydratesPer100,
            name = ingredientState.name,
            fatPer100 = ingredientState.fatPer100,
            caloriesPer100 = ingredientState.caloriesPer100,
            proteinPer100 = ingredientState.proteinPer100
        )
    }

    fun toMealTemplate(mealState: MealState): MealTemplate {
        return MealTemplate(
            mealTemplateId = mealState.mealTemplateId,
            name = mealState.name,
            totalCarbohydrates = mealState.totalCarbohydrates,
            totalProtein = mealState.totalProtein,
            totalFat = mealState.totalFat,
            totalCalories = mealState.totalCalories
        )
    }

    fun toIngredient(ingredientState: IngredientState, mealId: Long): Ingredient {
        return Ingredient(
            grams = ingredientState.grams,
            caloriesPer100 = ingredientState.caloriesPer100,
            carbohydratesPer100 = ingredientState.carbohydratesPer100,
            fatPer100 = ingredientState.fatPer100,
            proteinPer100 = ingredientState.proteinPer100,
            name = ingredientState.name,
            id = ingredientState.ingredientId,
            mealId = mealId
        )
    }

    fun fromIngredientToIngredientState(ingredient: Ingredient): IngredientState {
        return IngredientState(
            ingredientId = ingredient.id,
            name = ingredient.name,
            carbohydratesPer100 = ingredient.carbohydratesPer100,
            caloriesPer100 = ingredient.caloriesPer100,
            proteinPer100 = ingredient.proteinPer100,
            fatPer100 = ingredient.fatPer100,
            grams = ingredient.grams,
        )
    }
}
