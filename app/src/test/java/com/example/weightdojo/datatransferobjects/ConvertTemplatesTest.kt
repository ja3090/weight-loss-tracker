package com.example.weightdojo.datatransferobjects

import com.example.weightdojo.database.models.IngredientTemplate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ConvertTemplatesTest {
    private lateinit var converter: ConvertTemplates

    @Before
    fun createDb() {
        converter = ConvertTemplates()
    }

    @Test
    fun blankStateReturnedWhenNoArgumentsProvided() {
        val blankMealState = MealState(
            mealTemplateId = 0,
            name = "",
            totalProtein = 0f,
            totalFat = 0f,
            totalCarbohydrates = 0f,
            totalCalories = 0f,
            dayId = null
        )
        val blankIngredientList: List<IngredientState> = listOf()

        val mealTWithIngT = converter.makeMealStateAndIngredientList(dayId = null)

        assertTrue(
            "produces blank meal state correctly",
            blankMealState == mealTWithIngT.first
        )

        assertTrue(
            "produces empty ingredient list correctly",
            blankIngredientList == mealTWithIngT.second
        )
    }

    @Test
    fun testToIngredientState() {

        val ingredientTemplate = IngredientTemplate(
            ingredientTemplateId = 1L,
            name = "Test Ingredient",
            grams = 100f,
            carbohydratesPer100 = 20f,
            fatPer100 = 10f,
            caloriesPer100 = 150f,
            proteinPer100 = 5f
        )

        val converter = ConvertTemplates()

        val ingredientState = converter.toIngredientState(ingredientTemplate)

        assertEquals(ingredientTemplate.ingredientTemplateId, ingredientState.ingredientTemplateId)
        assertEquals(ingredientTemplate.name, ingredientState.name)
        assertEquals(ingredientTemplate.grams, ingredientState.grams)
        assertEquals(ingredientTemplate.carbohydratesPer100, ingredientState.carbsPer100)
        assertEquals(ingredientTemplate.fatPer100, ingredientState.fatPer100)
        assertEquals(ingredientTemplate.caloriesPer100, ingredientState.caloriesPer100)
        assertEquals(ingredientTemplate.proteinPer100, ingredientState.proteinPer100)
    }
}