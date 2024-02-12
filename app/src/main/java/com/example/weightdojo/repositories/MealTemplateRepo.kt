package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.dao.mealtemplate.MealTemplateDao
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.RepoResponse

interface MealTemplateRepo {
    suspend fun searchMealTemplates(term: String): RepoResponse<List<MealTemplate>>
    suspend fun createMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
    ): RepoResponse<Nothing?>

    suspend fun getMealTemplateWithIngredientsById(
        mealTemplateId: Long,
        dayId: Long?
    ): RepoResponse<out TemplateState?>

    suspend fun overwriteTemplate(
        mealTemplate: MealTemplate,
        ingredientList: List<IngredientState>
    ): RepoResponse<Nothing?>
}

typealias TemplateState = Pair<MealState, List<IngredientState>>

class MealTemplateRepoImpl(
    private val mealTemplateDao: MealTemplateDao,
    private val templateConverter: ConvertTemplates = ConvertTemplates()
) : MealTemplateRepo {

    override suspend fun searchMealTemplates(term: String): RepoResponse<List<MealTemplate>> {
        return try {
            val mealTempls = mealTemplateDao.searchMealTemplates(term)

            RepoResponse(
                success = true,
                data = mealTempls
            )
        } catch (e: Exception) {

            RepoResponse(
                success = false,
                data = listOf(),
                errorMessage = e.message
            )
        }
    }

    override suspend fun getMealTemplateWithIngredientsById(
        mealTemplateId: Long,
        dayId: Long?
    ): RepoResponse<out TemplateState?> {

        return try {
            val mealWithIngredients =
                mealTemplateDao.getMealTemplateWithIngredientsById(mealTemplateId)

            val mealIngredientList = templateConverter.makeMealStateAndIngredientList(
                mealWithIngredients.mealTemplate, mealWithIngredients.ingredients, dayId
            )

            RepoResponse(
                success = true,
                data = mealIngredientList
            )
        } catch (e: Exception) {
            RepoResponse(
                errorMessage = e.message,
                success = false,
                data = null
            )

        }
    }

    override suspend fun overwriteTemplate(
        mealTemplate: MealTemplate,
        ingredientList: List<IngredientState>
    ): RepoResponse<Nothing?> {
        return try {
            mealTemplateDao.updateMealTemplateHandler(mealTemplate, ingredientList)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {
            RepoResponse(success = false, data = null, errorMessage = e.message)
        }
    }

    override suspend fun createMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
    ): RepoResponse<Nothing?> {
        return try {
            mealState ?: throw Exception("mealState is null")

            mealTemplateDao.createMealTemplate(
                mealTemplate = templateConverter.toMealTemplate(mealState),
                ingredientTemplates = ingredientStateList
            )

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {
            Log.e("templateSubmissionError", e.message.toString())

            RepoResponse(success = false, data = null, errorMessage = e.message)
        }
    }
}