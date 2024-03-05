package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentData
import com.example.weightdojo.datatransferobjects.IngredientWithNutrimentDataBuilder
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleIngredientDetailedBuilder
import com.example.weightdojo.datatransferobjects.SingleIngredientDetailedDTO
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.repoWrapper

interface IngredientRepository {
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>
    suspend fun searchTemplates(
        term: String
    ): RepoResponse<List<IngredientWithNutrimentData>>
    suspend fun getSingleDetailedIngredient(
        id: Long
    ): RepoResponse<SingleMealDetailedIngredient?>
    suspend fun deleteIngredient(
        id: Long
    ): RepoResponse<Unit?>
    suspend fun getSingleDetailedIngredient(): RepoResponse<SingleMealDetailedIngredient>
    suspend fun deleteIngredientTemplate(id: Long): RepoResponse<Unit?>
}

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao,
) : IngredientRepository {

    override fun getIngredientsByMealId(mealId: Long): List<Ingredient> {
        return ingredientDao.getIngredientsByMealId(mealId)
    }

    override suspend fun searchTemplates(
        term: String
    ): RepoResponse<List<IngredientWithNutrimentData>> {
        return repoWrapper(listOf()) {
            val rows = ingredientDao.searchMealTemplates(term)

            val ingredientWithNutrimentDataBuilder = IngredientWithNutrimentDataBuilder(rows)

            ingredientWithNutrimentDataBuilder.data
        }
    }

    override suspend fun getSingleDetailedIngredient(
        id: Long
    ): RepoResponse<SingleMealDetailedIngredient?> {
        return repoWrapper {
            val rows = ingredientDao.getDetailedIngredient(id)

            val builder = SingleIngredientDetailedBuilder(rows)

            builder.data ?: throw Exception("Couldn't find ingredient")
        }
    }

    override suspend fun getSingleDetailedIngredient(): RepoResponse<SingleMealDetailedIngredient> {
        return repoWrapper(SingleMealDetailedIngredient()) {
            val rows = ingredientDao.getDetailedIngredient()

            val builder = SingleIngredientDetailedBuilder(rows)

            builder.data ?: throw Exception("Couldn't find ingredient")
        }
    }

    override suspend fun deleteIngredient(
        id: Long
    ): RepoResponse<Unit?> {
        return repoWrapper {
            ingredientDao.deleteIngredient(id)
        }
    }

    override suspend fun deleteIngredientTemplate(
        id: Long
    ): RepoResponse<Unit?> {
        return repoWrapper {
            ingredientDao.deleteIngredientTemplate(id)
        }
    }
}