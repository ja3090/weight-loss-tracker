package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.repoWrapper

interface MealRepository {
    suspend fun deleteMeal(mealId: Long?, dayId: Long?): RepoResponse<Unit?>
    suspend fun getSingleMealDetailed(mealId: Long): RepoResponse<SingleMealDetailed?>
    suspend fun updateMeal(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?>
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
) : MealRepository {
    override suspend fun getSingleMealDetailed(
        mealId: Long
    ): RepoResponse<SingleMealDetailed?> {
        Log.d("mealId", mealId.toString())

        return repoWrapper {
            mealDao.getMealWithIngredientsDetailed(mealId)
                ?: throw Exception("Couldn't find that meal")
        }
    }

    override suspend fun deleteMeal(mealId: Long?, dayId: Long?): RepoResponse<Unit?> {
        return repoWrapper {
            mealDao.handleDelete(
                mealId ?: throw Exception("mealId is required and null"),
                dayId ?: throw Exception("dayId is required and null")
            )
        }
    }

    override suspend fun updateMeal(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?> {
        return repoWrapper { mealDao.updateMeal(singleMealDetailed) }
    }
}