package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.RepoResponse

interface MealRepository {
    suspend fun handleInsert(
        mealState: Meal,
        ingredientList: List<IngredientState>
    ): RepoResponse<Nothing?>
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
) : MealRepository {

    override suspend fun handleInsert(
        mealState: Meal,
        ingredientList: List<IngredientState>
    ): RepoResponse<Nothing?> {
        return try {

            mealDao.insertMeal(mealState, ingredientList)

            RepoResponse(
                success = true,
                data = null,
                errorMessage = null
            )
        } catch (e: Exception) {
            RepoResponse(
                success = false,
                data = null,
                errorMessage = e.message
            )
        }
    }
}