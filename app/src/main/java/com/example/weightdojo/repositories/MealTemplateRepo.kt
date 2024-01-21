package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.dao.MealTemplateDao
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.RepoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface MealTemplateRepo {
    fun getMealTemplateWithIngredients(): List<MealTemplate>
//    fun getMealTemplateWithIngredientsById(mealTemplateId: Long): MealTemplateWithIngredients
    fun searchMealTemplates(term: String): List<MealTemplate>
    suspend fun insertMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
        overwrite: Boolean
    ): Boolean
    suspend fun getMealTemplateWithIngredientsById(
    mealTemplateId: Long,
    dayId: Long?
    ): RepoResponse<out TemplateState?>
}

typealias TemplateState = Pair<MealState, List<IngredientState>>

class MealTemplateRepoImpl(
    private val mealTemplateDao: MealTemplateDao,
    private val templateConverter: ConvertTemplates = ConvertTemplates()
) : MealTemplateRepo {

    override fun getMealTemplateWithIngredients(): List<MealTemplate> {
        return mealTemplateDao.getMealTemplates()
    }

    override fun searchMealTemplates(term: String): List<MealTemplate> {
        return mealTemplateDao.searchMealTemplates(term)
    }

    override suspend fun getMealTemplateWithIngredientsById(
        mealTemplateId: Long,
        dayId: Long?
    ): RepoResponse<out TemplateState?> {

        return CoroutineScope(Dispatchers.IO).async {
            try {

                val mealWithIngredients =
                    mealTemplateDao.getMealTemplateWithIngredientsById(mealTemplateId)

                val (meal, ingredientList) = templateConverter.makeMealState(
                    mealWithIngredients.mealTemplate, mealWithIngredients.ingredients, dayId
                )

                return@async RepoResponse(
                    success = true,
                    data = Pair(meal, ingredientList)
                )
            } catch (e: Exception) {
                return@async RepoResponse(
                    errorMessage = e.message,
                    success = false,
                    data = null
                )
            }
        }.await()
    }

    override suspend fun insertMealTemplate(
        mealState: MealState?,
        ingredientStateList: List<IngredientState>,
        overwrite: Boolean
    ): Boolean {

        val success = CoroutineScope(Dispatchers.IO).async {
            try {
                mealState ?: throw Exception("mealState is null")

                mealTemplateDao.handleMealTemplateInsertion(
                    mealState = mealState,
                    ingredientList = ingredientStateList,
                    overwrite = overwrite
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