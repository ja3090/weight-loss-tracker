package com.example.weightdojo.screens.home.addmodal.addcalories

import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class AddCaloriesStateHandlerTest {

    private lateinit var handler: AddCaloriesStateHandler
    private val dayId = 12345L // Example dayId

    @Before
    fun setUp() {
        // Initialize the handler before each test
        handler = AddCaloriesStateHandler(dayId)
    }

    @Test
    fun `setErrorMessage updates the state's error message`() {
        val errorMessage = "An error occurred"
        handler.setErrorMessage(errorMessage)
        assertEquals(
            "Error message should be updated in the state",
            errorMessage,
            handler.state.error
        )
    }

    @Test
    fun `setActiveIngredient updates activeIngredientId correctly and toggles it off if set again`() {
        val ingredientId = UUID.randomUUID()
        val ingredient = IngredientState(internalId = ingredientId)

        handler.setActiveIngredient(ingredient)

        assertEquals(
            "Active ingredient ID should be updated to the ingredient's ID",
            ingredientId,
            handler.state.activeIngredientId
        )

        handler.setActiveIngredient(ingredient)

        assertNull(
            "Active ingredient ID should be null when toggled off",
            handler.state.activeIngredientId
        )
    }

    @Test
    fun `addNewIngredient adds a new ingredient to the ingredientList`() {
        handler.addNewIngredient()
        assertFalse(
            "Ingredient list should not be empty after adding a new ingredient",
            handler.state.ingredientList.isEmpty()
        )
    }

    @Test
    fun `deleteIngredient marks an ingredient for deletion`() {
        handler.addNewIngredient()
        val ingredientToDelete = handler.state.ingredientList.first()

        handler.deleteIngredient(ingredientToDelete)

        assertEquals(
            "Ingredient should be marked for deletion",
            Marked.DELETE,
            handler.state.ingredientList.first().markedFor,
        )
    }

    @Test
    fun `changeName updates the meal name correctly`() {
        val newName = "Healthy Salad"

        val initialName = handler.state.mealState?.name
        assertNotEquals("Initial meal name should not match the new name", newName, initialName)

        handler.initMealCreation(dayId)

        handler.changeName(newName)

        assertNotNull("MealState should not be null after setting a name", handler.state.mealState)
        assertEquals(
            "Meal name should be updated to the new name",
            newName,
            handler.state.mealState?.name
        )
    }

    @Test
    fun `addIngredientToMeal adds a new ingredient to the ingredientList`() {
        val ingredientTemplate = IngredientTemplate(
            name = "Tomato",
            caloriesPer100 = 18f,
            grams = 0f
        )

        val initialSize = handler.state.ingredientList.size

        handler.addIngredientToMeal(ingredientTemplate)

        assertTrue(
            "The ingredient list should increase in size by 1",
            handler.state.ingredientList.size == initialSize + 1
        )

        with(handler.state.ingredientList.last()) {
            assertTrue(
                "The name of the last ingredient should match the template",
                "Tomato" == name,
            )
            assertTrue(
                "The caloriesPer100g of the last ingredient should match the template",
                18f == caloriesPer100
            )
        }
    }

    @Test
    fun `moveToAddIngredient updates currentSubModal to AddIngredientTemplate`() {
        handler.moveToAddIngredient()

        assertEquals(
            "The currentSubModal should be updated to AddIngredientTemplate",
            AddCalsSubModals.AddIngredientTemplate,
            handler.state.currentSubModal
        )
    }

    @Test
    fun `changeIngredient updates ingredient details correctly`() {
        val initialIngredient =
            IngredientState(internalId = UUID.randomUUID(), name = "Tomato", caloriesPer100 = 18f)
        handler.state = handler.state.copy(ingredientList = listOf(initialIngredient))

        val modifiedIngredient =
            initialIngredient.copy(name = "Cherry Tomato", caloriesPer100 = 20f)

        handler.changeIngredient(modifiedIngredient)

        assertTrue(
            "Ingredient list should contain the modified ingredient",
            handler.state.ingredientList.any { it.internalId == modifiedIngredient.internalId })

        handler.state.ingredientList.find { it.internalId == modifiedIngredient.internalId }
            ?.let { updatedIngredient ->
                assertTrue(
                    "The name of the ingredient should be updated",
                    "Cherry Tomato" == updatedIngredient.name,
                )
                assertTrue(
                    "The calories per 100g of the ingredient should be updated",
                    20f == updatedIngredient.caloriesPer100,
                )
            } ?: fail("Updated ingredient not found in the ingredient list")
    }

    @Test
    fun `initMealCreation initializes meal creation with expected values`() {
        handler.initMealCreation(dayId)

        assertNotNull("MealState should be initialized and not null", handler.state.mealState)

        assertTrue(
            "IngredientList should be populated and not empty after meal creation initialization",
            handler.state.ingredientList.isEmpty()
        )

        assertTrue(
            "CurrentSubModal should be set to MealCreation after initializing meal creation",
            AddCalsSubModals.MealCreation == handler.state.currentSubModal
        )
    }

    @Test
    fun `moveToSubModal updates currentSubModal to specified sub-modal`() {

        handler.moveToSubModal(AddCalsSubModals.AddMealTemplate)
        assertEquals(
            "The currentSubModal should be updated to AddMealTemplate",
            AddCalsSubModals.AddMealTemplate,
            handler.state.currentSubModal
        )

        handler.moveToSubModal(AddCalsSubModals.MealCreation)
        assertEquals(
            "The currentSubModal should be updated to MealCreation",
            AddCalsSubModals.MealCreation,
            handler.state.currentSubModal
        )

        handler.moveToSubModal(AddCalsSubModals.AddIngredientTemplate)
        assertEquals(
            "The currentSubModal should be updated to AddIngredientTemplate",
            AddCalsSubModals.AddIngredientTemplate,
            handler.state.currentSubModal
        )
    }

    @Test
    fun `setMealTemplate updates mealState and ingredientList correctly`() {
        val mealState = MealState(name = "Sample Meal")
        val ingredientList = listOf(
            IngredientState(
                name = "Ingredient 1",
                caloriesPer100 = 100f,
                internalId = UUID.randomUUID()
            ),
            IngredientState(
                name = "Ingredient 2",
                caloriesPer100 = 200f,
                internalId = UUID.randomUUID()
            )
        )

        handler.setMealTemplate(mealState, ingredientList)

        assertTrue(
            "The mealState should be updated to match the provided mealState",
            mealState == handler.state.mealState
        )

        assertTrue(
            "The ingredientList should be updated to match the provided ingredientList",
            handler.state.ingredientList.containsAll(ingredientList)
        )

        assertEquals(
            "The currentSubModal should be set to MealCreation after setting the meal template",
            AddCalsSubModals.MealCreation,
            handler.state.currentSubModal
        )
    }

    @Test
    fun `reset resets the state to initial values`() {
        handler.setErrorMessage("Error")
        handler.addNewIngredient()

        handler.reset()

        assertNull("Error message should be reset to null", handler.state.error)
        assertTrue(
            "Ingredient list should be empty after reset",
            handler.state.ingredientList.isEmpty()
        )
        assertEquals("DayId should remain unchanged after reset", dayId, handler.state.dayId)
    }
}