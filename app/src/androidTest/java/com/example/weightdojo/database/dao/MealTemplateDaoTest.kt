package com.example.weightdojo.database.dao

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.dao.mealtemplate.MealTemplateDao
import com.example.weightdojo.database.dao.test.TestMealTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.MealTemplateWithIngredients
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test


class MealTemplateDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var mealTemplateDao: MealTemplateDao
    private val convertTemplates: ConvertTemplates = ConvertTemplates()
    private val ingTemplate = IngredientTemplate(name = "Test", grams = 0f, caloriesPer100 = 0f)
    private val mealTemplate = MealTemplate(name = "Test")
    private var insertedIngTemplateId: Long = 0
    private var insertedMealTemplateId: Long = 0

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Database.buildDb(context, true)
        mealTemplateDao = db.mealTemplateDao()
        insertedIngTemplateId = mealTemplateDao.overwriteIngredientTemplate(ingTemplate)
        insertedMealTemplateId = mealTemplateDao.overwriteMealTemplate(mealTemplate)
    }

    @Test
    fun processIngredients_deletes_relationship_not_ingredient_template() {
        val mealIngredientTemplate =
            MealIngredientTemplate(insertedMealTemplateId, insertedIngTemplateId)

        mealTemplateDao.overwriteMealIngredientJunction(mealIngredientTemplate)

        var toIngredientState = convertTemplates.toIngredientState(ingTemplate)

        toIngredientState = toIngredientState.copy(
            ingredientTemplateId = insertedIngTemplateId, markedFor = Marked.DELETE
        )

        mealTemplateDao.processIngredients(listOf(toIngredientState), insertedMealTemplateId)

        val mealIng =
            db.testMealTemplateDao().getMealIngById(insertedMealTemplateId, insertedIngTemplateId)

        assertTrue("MealIngredient should be null", mealIng == null)

        val ingTemplateFromDb = db.testMealTemplateDao().getIngTemplateById(insertedIngTemplateId)

        assertTrue("Ingredient Template still exists", ingTemplateFromDb !== null)
    }

    @Test
    fun processIngredients_overwrites_ingredient() {
        val mealIngredientTemplate =
            MealIngredientTemplate(insertedMealTemplateId, insertedIngTemplateId)

        mealTemplateDao.overwriteMealIngredientJunction(mealIngredientTemplate)

        val changedIngredient =
            ingTemplate.copy(name = "Test 2", ingredientTemplateId = insertedIngTemplateId)

        val ingState = convertTemplates.toIngredientState(changedIngredient)

        mealTemplateDao.processIngredients(listOf(ingState), insertedMealTemplateId)

        val ingTemplateAfterInsert =
            db.testMealTemplateDao().getIngTemplateById(insertedIngTemplateId)

        assertTrue(
            "same ingredientTemplate is changed",
            ingTemplate.name != ingTemplateAfterInsert?.name &&
                    insertedIngTemplateId == ingTemplateAfterInsert?.ingredientTemplateId
        )
    }

    @Test
    fun createMealTemplate_creates_new_meal_template_correctly() {
        val mealTemplateToInsert = mealTemplate.copy(mealTemplateId = insertedMealTemplateId)

        val ingState = convertTemplates.toIngredientState(ingTemplate)

        val ingStateArr = listOf(
            ingState.copy(grams = 100f, caloriesPer100 = 10f, fatPer100 = 10f),
            ingState.copy(grams = 100f, caloriesPer100 = 10f),
            ingState.copy(grams = 50f, caloriesPer100 = 10f, markedFor = Marked.DELETE),
        )

        val mealTemplateId =
            mealTemplateDao.createMealTemplate(mealTemplateToInsert, ingStateArr)

        assertTrue(
            "New meal template created",
            mealTemplateToInsert.mealTemplateId != mealTemplateId
        )

        val mealTemplate = db.testMealTemplateDao().getMealTemplateById(mealTemplateId)

        assertTrue("mealTemplateEntered", mealTemplate !== null)

        assertTrue("name is right", mealTemplate?.name == mealTemplateToInsert.name)

        assertTrue(
            "calorie total is correct",
            mealTemplate?.totalCalories == 20f
        )

        assertTrue(
            "fat total is correct",
            mealTemplate?.totalFat == 10f
        )

        assertTrue(
            "protein total is correct",
            mealTemplate?.totalProtein == 0f
        )

        assertTrue(
            "carbs total is correct",
            mealTemplate?.totalCarbohydrates == 0f
        )
    }

    @Test
    fun updateMealTemplateHandler_updates_meal_template_correctly() {
        val ingState = convertTemplates.toIngredientState(ingTemplate)

        val ingStateArr1 = listOf(
            ingState,
            ingState,
            ingState,
        )

        val mealTemplId = mealTemplateDao.createMealTemplate(mealTemplate, ingStateArr1)

        var mealTemplWithIng = db.testMealTemplateDao().getAllMealIngredientByMealTemplId()

        mealTemplWithIng = mealTemplWithIng.filter { it.mealTemplate.mealTemplateId == mealTemplId }

        assertTrue("entered successfully", mealTemplWithIng.isNotEmpty())

        assertTrue(
            "ingredients added successfully",
            mealTemplWithIng[0].ingredients.size == ingStateArr1.size
        )

        val changedIngs = mealTemplWithIng[0].ingredients.mapIndexed { ind, it ->
            val ingState = convertTemplates.toIngredientState(it)

            if (ind == mealTemplWithIng[0].ingredients.size - 1) {
                ingState.copy(markedFor = Marked.DELETE, grams = 1000f, caloriesPer100 = 1000f)
            } else {
                ingState.copy(grams = 100f, caloriesPer100 = 100f, carbohydratesPer100 = 100f)
            }
        }

        val updatedMeal = mealTemplate.copy(mealTemplateId = mealTemplId)

        mealTemplateDao.updateMealTemplateHandler(updatedMeal, changedIngs)

        var updatedMealTemplate =
            db.testMealTemplateDao().getAllMealIngredientByMealTemplId()

        updatedMealTemplate =
            updatedMealTemplate.filter { it.mealTemplate.mealTemplateId == mealTemplId }

        assertTrue("list is not empty", updatedMealTemplate.isNotEmpty())

        assertTrue(
            "id hasn't changed",
            updatedMealTemplate[0].mealTemplate.mealTemplateId == mealTemplId
        )

        assertTrue(
            "carb totals changed",
            updatedMealTemplate[0].mealTemplate.totalCarbohydrates == 200f
        )

        assertTrue(
            "cals totals changed",
            updatedMealTemplate[0].mealTemplate.totalCalories == 200f
        )

        assertTrue(
            "ingredient list size changed",
            updatedMealTemplate[0].ingredients.size == 2
        )
    }
}