package com.example.weightdojo.repositories

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.IngredientTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.RepoResponse

interface IngredientTemplateRepo {
    suspend fun deleteIngTemplate(
        ingredientTemplate: IngredientTemplate
    ): RepoResponse<Nothing?>
}

class IngredientTemplateRepoImpl(
    private val ingredientTemplateDao: IngredientTemplateDao
) : IngredientTemplateRepo {
    override suspend fun deleteIngTemplate(
        ingredientTemplate: IngredientTemplate
    ): RepoResponse<Nothing?> {
        return try {
            ingredientTemplateDao.deleteIngTemplateHandler(ingredientTemplate)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {

            RepoResponse(success = false, errorMessage = e.message, data = null)
        }
    }
}