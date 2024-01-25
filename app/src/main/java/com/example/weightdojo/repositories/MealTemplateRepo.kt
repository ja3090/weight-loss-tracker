package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.dao.mealtemplate.MealTemplateDao
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.RepoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface MealTemplateRepo {
    fun searchMealTemplates(term: String): List<MealTemplate>
    suspend fun createMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
        overwrite: Boolean
    ): Boolean
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

    override fun searchMealTemplates(term: String): List<MealTemplate> {
        return mealTemplateDao.searchMealTemplates(term)
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
            mealTemplateDao.updateMealTemplate(mealTemplate, ingredientList)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {
            RepoResponse(success = false, data = null, errorMessage = e.message)
        }
    }

    override suspend fun createMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
        overwrite: Boolean
    ): Boolean {

        val success = CoroutineScope(Dispatchers.IO).async {
            try {
                mealState ?: throw Exception("mealState is null")

                val ingTemplates =
                    ingredientStateList.map { templateConverter.toIngredientTemplate(it) }

                mealTemplateDao.createMealTemplate(
                    mealTemplate = templateConverter.toMealTemplate(mealState, false),
                    ingredientTemplates = ingTemplates
                )

                return@async true
            } catch (e: Exception) {
                Log.e("templateSubmissionError", e.message.toString())

                return@async false
            }
        }

        return success.await()
    }
}