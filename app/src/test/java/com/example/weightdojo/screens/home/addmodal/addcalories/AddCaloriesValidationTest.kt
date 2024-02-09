package com.example.weightdojo.screens.home.addmodal.addcalories

import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test

class AddCaloriesValidationTest {

    private val validation = AddCaloriesValidation()

    @Test
    fun `validateSubmission with null meal name returns mealState is null error`() {
        val state = AddCaloriesState(mealState = null, ingredientList = listOf())
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.mealStateNull, response.errorMessage)
    }

    @Test
    fun `validateSubmission with empty meal name returns no meal name provided error`() {
        val state = AddCaloriesState(mealState = MealState(name = ""), ingredientList = listOf())
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.noMealNameProvided, response.errorMessage)
    }

    @Test
    fun `validateSubmission with empty ingredient list returns empty ingredient list error`() {
        val state =
            AddCaloriesState(mealState = MealState(name = "Meal"), ingredientList = listOf())
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.emptyIngredientList, response.errorMessage)
    }

    @Test
    fun `validateSubmission with ingredient having empty name returns no ingredient name given error`() {
        val ingredient = IngredientState(name = "", grams = 100f, caloriesPer100 = 200f)
        val state = AddCaloriesState(
            mealState = MealState(name = "Meal"),
            ingredientList = listOf(ingredient)
        )
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.noIngredientNameGiven, response.errorMessage)
    }

    @Test
    fun `validateSubmission with ingredient having 0 grams returns grams field empty error`() {
        val ingredient = IngredientState(name = "Ingredient", grams = 0f, caloriesPer100 = 200f)
        val state = AddCaloriesState(
            mealState = MealState(name = "Meal"),
            ingredientList = listOf(ingredient)
        )
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.gramsFieldEmpty, response.errorMessage)
    }

    @Test
    fun `validateSubmission with ingredient having 0 calories per 100 grams returns calories field empty error`() {
        val ingredient = IngredientState(name = "Ingredient", grams = 100f, caloriesPer100 = 0f)
        val state = AddCaloriesState(
            mealState = MealState(name = "Meal"),
            ingredientList = listOf(ingredient)
        )
        val response = validation.validateSubmission(state)
        assertFalse(response.success)
        assertEquals(validation.errorMessages.caloriesFieldEmpty, response.errorMessage)
    }

    @Test
    fun `validateSubmission with valid input returns success`() {
        val ingredient = IngredientState(name = "Ingredient", grams = 100f, caloriesPer100 = 200f)
        val state = AddCaloriesState(
            mealState = MealState(name = "Meal"),
            ingredientList = listOf(ingredient)
        )
        val response = validation.validateSubmission(state)
        assertEquals(true, response.success)
        assertEquals(null, response.errorMessage)
    }
}