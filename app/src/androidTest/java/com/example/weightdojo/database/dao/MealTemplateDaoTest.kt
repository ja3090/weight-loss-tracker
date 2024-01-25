package com.example.weightdojo.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.dao.test.TestMealTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test


class MealTemplateDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var testMealTemplateDao: TestMealTemplateDao
    private var mealTemplate = MealTemplate(
        name = "test",
        totalFat = null,
        totalCalories = null,
        totalCarbohydrates = null,
        totalProtein = null
    )
    private var ingredientTemplate = IngredientTemplate(
        name = "test",
        caloriesPer100 = 0f,
        carbohydratesPer100 = 0f,
        fatPer100 = 0f,
        proteinPer100 = 0f,
        grams = 0f
    )
    private lateinit var mealIngredientId: Pair<Long, Long>
    private val convertTemplates = ConvertTemplates()

    private fun createTemplates() {
        testMealTemplateDao = db.testMealTemplateDao()
        val ingredientTemplateId = testMealTemplateDao.insertIngredientTemplate(ingredientTemplate)
        ingredientTemplate = ingredientTemplate.copy(
            ingredientTemplateId = ingredientTemplateId
        )
        val mealTemplateId = testMealTemplateDao.insertMealTemplate(mealTemplate)
        mealTemplate = mealTemplate.copy(
            mealTemplateId = mealTemplateId
        )
        testMealTemplateDao.insertMealIngredientJunction(
            MealIngredientTemplate(mealTemplateId, ingredientTemplateId)
        )
        mealIngredientId = Pair(mealTemplateId, ingredientTemplateId)
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Database.buildDb(context, true)
        createTemplates()
    }

    @Test
    fun processIngredients_DeletesRelationshipNotIngredient() {
        val ingredientStateList = listOf(
            IngredientState(
                markedFor = Marked.DELETE,
                ingredientTemplateId = ingredientTemplate.ingredientTemplateId
            )
        )

        testMealTemplateDao.processIngredients(ingredientStateList, mealTemplate.mealTemplateId)

        val mealIngJunction = testMealTemplateDao.getMealIngById(
            mealTemplate.mealTemplateId, ingredientTemplate.ingredientTemplateId
        )

        assertTrue("relationship is deleted", mealIngJunction == null)

        val ingredientTemplateAfterCall =
            testMealTemplateDao.getIngTemplateById(ingredientTemplate.ingredientTemplateId)

        assertNotNull("ingredientTemplate is not null", ingredientTemplateAfterCall)

        assertTrue("hasn't changed", ingredientTemplateAfterCall == ingredientTemplate)
    }

    @Test
    fun processIngredients_createsNewWithNoIdGiven() {
        testMealTemplateDao.processIngredients(
            listOf(
                IngredientState()
            ), mealTemplate.mealTemplateId
        )

        val allMealIngredients = testMealTemplateDao.getAllMealIngredient()

        assertTrue(allMealIngredients.size == 1)
        assertTrue(allMealIngredients[0].ingredients.size == 2)
    }

    @Test
    fun processIngredients_overwritesIngredient() {
        val ingredientState = IngredientState(
            ingredientTemplateId = ingredientTemplate.ingredientTemplateId, name = "New name"
        )

        testMealTemplateDao.processIngredients(listOf(ingredientState), mealTemplate.mealTemplateId)

        val allMealIngredients = testMealTemplateDao.getAllMealIngredient()

        assertTrue(allMealIngredients.size == 1)
        assertTrue(allMealIngredients[0].ingredients.size == 1)
        assertTrue(
            "name updated successfully",
            allMealIngredients[0].ingredients[0].name == "New name"
        )
        assertTrue(
            "overwritten template",
            allMealIngredients[0].ingredients[0].ingredientTemplateId == ingredientTemplate.ingredientTemplateId
        )
    }
}