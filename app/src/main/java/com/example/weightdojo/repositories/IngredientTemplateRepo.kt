package com.example.weightdojo.repositories

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.IngredientTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.RepoResponse

interface IngredientTemplateRepo {
    suspend fun getIngredientTemplates(term: String): RepoResponse<List<IngredientTemplate>>
    suspend fun deleteIngTemplate(
        ingredientTemplate: IngredientTemplate
    ): RepoResponse<Nothing?>
}

class IngredientTemplateRepoImpl(
    private val ingredientTemplateDao: IngredientTemplateDao
) : IngredientTemplateRepo {

    override suspend fun getIngredientTemplates(
        term: String
    ): RepoResponse<List<IngredientTemplate>> {
        return try {
            val ingTempls = ingredientTemplateDao.searchIngredientTemplates(term)

            RepoResponse(
                success = true,
                data = ingTempls,
            )
        } catch (e: Exception) {
            RepoResponse(
                success = false,
                data = listOf(),
                errorMessage = e.message
            )
        }
    }

    override suspend fun deleteIngTemplate(
        ingredientTemplate: IngredientTemplate
    ): RepoResponse<Nothing?> {
        return try {
            ingredientTemplateDao.deleteIngTemplate(ingredientTemplate)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {

            RepoResponse(success = false, errorMessage = e.message, data = null)
        }
    }
}