package com.example.weightdojo.repositories.mealrepo

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.MealWithNutrimentDataBuilder
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.datatransferobjects.repoWrapper
import com.example.weightdojo.utils.seeder.CalorieTotals
import com.example.weightdojo.utils.seeder.NutrimentTotals
import com.example.weightdojo.utils.totalGrams

interface MealRepository {
    suspend fun deleteMeal(mealId: Long?, dayId: Long?): RepoResponse<Unit?>
    suspend fun getSingleMealDetailed(mealId: Long): RepoResponse<SingleMealDetailed?>
    suspend fun updateMeal(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?>
    suspend fun searchTemplates(term: String): RepoResponse<List<MealWithNutrimentData>>
    suspend fun enterMeal(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?>
}

class MealRepositoryImpl(
    override val database: AppDatabase,
    override val mealDao: MealDao = database.mealDao(),
    override val calorieTotals: CalorieTotals = CalorieTotals(database),
    override val nutrimentTotals: NutrimentTotals = NutrimentTotals(database),
) : MealRepository, UpdateMeal, CreateMeal {
    override suspend fun getSingleMealDetailed(
        mealId: Long
    ): RepoResponse<SingleMealDetailed?> {
        return repoWrapper {
            mealDao.getMealWithIngredientsDetailed(mealId)
                ?: throw Exception("Couldn't find that meal")
        }
    }
    override suspend fun enterMeal(
        singleMealDetailed: SingleMealDetailed
    ): RepoResponse<Unit?> {
        return repoWrapper {
            database.runInTransaction { createHandler(singleMealDetailed) }
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

    override suspend fun updateMeal(
        singleMealDetailed: SingleMealDetailed
    ): RepoResponse<Unit?> {
        return repoWrapper { database.runInTransaction { updateHandler(singleMealDetailed) } }
    }

    override suspend fun searchTemplates(term: String): RepoResponse<List<MealWithNutrimentData>> {
        return repoWrapper(listOf()) {
            val rows = mealDao.searchMealTemplates(term)
            val mealWithNutrimentDataBuilder = MealWithNutrimentDataBuilder(rows)

            mealWithNutrimentDataBuilder.data
        }
    }
}