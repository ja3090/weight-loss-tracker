package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ConvertTemplatesTest {
    private lateinit var convertTemplates: ConvertTemplates
    private val dayId = 123L
    private val ingredientTemplates = listOf(
        IngredientTemplate(
            ingredientTemplateId = 1L,
            name = "Test Ingredient 1",
            grams = 100f,
            proteinPer100 = 2f,
            fatPer100 = 1f,
            carbohydratesPer100 = 3f,
            caloriesPer100 = 50f
        ), IngredientTemplate(
            ingredientTemplateId = 2L,
            name = "Test Ingredient 2",
            grams = 150f,
            proteinPer100 = 3f,
            fatPer100 = 1.5f,
            carbohydratesPer100 = 4f,
            caloriesPer100 = 75f
        )
    )
    private val mealTemplate = MealTemplate(
        mealTemplateId = 1L,
        name = "Test Meal",
        totalProtein = 10f,
        totalFat = 5f,
        totalCarbohydrates = 20f,
        totalCalories = 250f
    )
    private val mealState = MealState(
        mealTemplateId = 1L,
        name = "Test Meal",
        totalProtein = 50f,
        totalFat = 20f,
        totalCarbohydrates = 70f,
        totalCalories = 500f,
        dayId = 123L
    )

    @Before
    fun createDb() {
        convertTemplates = ConvertTemplates()
    }

    @Test
    fun testMakeMealStateAndIngredientList_WithDefaultValues() {
        val defaultMealId: Long = 0
        val defaultName = ""
        val defaultNutrientValue = 0f
        val defaultDayId: Long? = null

        val result = convertTemplates.makeMealStateAndIngredientList(
            null, listOf(), defaultDayId
        )

        assertEquals(defaultMealId, result.first.mealTemplateId)
        assertEquals(defaultName, result.first.name)
        assertEquals(defaultNutrientValue, result.first.totalProtein)
        assertEquals(defaultNutrientValue, result.first.totalFat)
        assertEquals(defaultNutrientValue, result.first.totalCarbohydrates)
        assertEquals(defaultNutrientValue, result.first.totalCalories)
        assertEquals(defaultDayId, result.first.dayId)

        assertTrue(result.second.isEmpty())
    }

    @Test
    fun testMakeMealStateAndIngredientList_WithValidInput() {
        val result = convertTemplates.makeMealStateAndIngredientList(
            mealTemplate, ingredientTemplates, dayId
        )

        assertEquals(mealTemplate.mealTemplateId, result.first.mealTemplateId)
        assertEquals(mealTemplate.name, result.first.name)
        assertEquals(mealTemplate.totalProtein, result.first.totalProtein)
        assertEquals(mealTemplate.totalFat, result.first.totalFat)
        assertEquals(mealTemplate.totalCarbohydrates, result.first.totalCarbohydrates)
        assertEquals(mealTemplate.totalCalories, result.first.totalCalories)
        assertEquals(dayId, result.first.dayId)
        assertEquals(2, result.second.size) // Check if two ingredients are converted
        assertEquals(ingredientTemplates[0].name, result.second[0].name)
        assertEquals(ingredientTemplates[1].grams, result.second[1].grams)
    }

    @Test
    fun testConvertToIngredientList_WithValidIngredients() {
        val result = convertTemplates.convertToIngredientList(ingredientTemplates)

        assertEquals(2, result.size)
        assertEquals(ingredientTemplates[0].name, result[0].name)
        assertEquals(ingredientTemplates[1].grams, result[1].grams)
        assertEquals(ingredientTemplates[0].proteinPer100, result[0].proteinPer100)
        assertEquals(ingredientTemplates[1].fatPer100, result[1].fatPer100)
    }

    @Test
    fun testToIngredientState_Conversion() {
        val ingredientTemplate = ingredientTemplates[0]

        val result = convertTemplates.toIngredientState(ingredientTemplate)

        assertEquals(ingredientTemplate.caloriesPer100, result.caloriesPer100)
        assertEquals(ingredientTemplate.grams, result.grams)
        assertEquals(ingredientTemplate.name, result.name)
        assertEquals(0L, result.ingredientId)
        assertEquals(ingredientTemplate.carbohydratesPer100, result.carbohydratesPer100)
        assertEquals(ingredientTemplate.fatPer100, result.fatPer100)
        assertEquals(ingredientTemplate.proteinPer100, result.proteinPer100)
        assertEquals(ingredientTemplate.ingredientTemplateId, result.ingredientTemplateId)
    }
    @Test
    fun testToIngredientTemplate_Conversion() {
        val convertTemplates = ConvertTemplates()
        val ingredientState = IngredientState(
            caloriesPer100 = 200f,
            grams = 150f,
            name = "Test Ingredient",
            ingredientId = 1L,
            carbohydratesPer100 = 30f,
            fatPer100 = 10f,
            proteinPer100 = 20f,
            ingredientTemplateId = 2L
        )

        val result = convertTemplates.toIngredientTemplate(ingredientState)

        assertEquals(ingredientState.caloriesPer100, result.caloriesPer100)
        assertEquals(ingredientState.grams, result.grams)
        assertEquals(ingredientState.name, result.name)
        assertEquals(ingredientState.carbohydratesPer100, result.carbohydratesPer100)
        assertEquals(ingredientState.fatPer100, result.fatPer100)
        assertEquals(ingredientState.proteinPer100, result.proteinPer100)
        assertEquals(ingredientState.ingredientTemplateId, result.ingredientTemplateId)
    }

    @Test
    fun testToMealTemplate_ConversionWithOverwrite() {
        val resultWithOverwrite = convertTemplates.toMealTemplate(mealState)

        assertEquals(mealState.mealTemplateId, resultWithOverwrite.mealTemplateId)
        assertEquals(mealState.name, resultWithOverwrite.name)
        assertEquals(mealState.totalProtein, resultWithOverwrite.totalProtein)
        assertEquals(mealState.totalFat, resultWithOverwrite.totalFat)
        assertEquals(mealState.totalCarbohydrates, resultWithOverwrite.totalCarbohydrates)
        assertEquals(mealState.totalCalories, resultWithOverwrite.totalCalories)
    }

    @Test
    fun testToMealTemplate_ConversionWithoutOverwrite() {
        val resultWithoutOverwrite = convertTemplates.toMealTemplate(mealState)

        assertEquals(0L, resultWithoutOverwrite.mealTemplateId)
        assertEquals(mealState.name, resultWithoutOverwrite.name)
        assertEquals(mealState.totalProtein, resultWithoutOverwrite.totalProtein)
        assertEquals(mealState.totalFat, resultWithoutOverwrite.totalFat)
        assertEquals(mealState.totalCarbohydrates, resultWithoutOverwrite.totalCarbohydrates)
        assertEquals(mealState.totalCalories, resultWithoutOverwrite.totalCalories)
    }
}