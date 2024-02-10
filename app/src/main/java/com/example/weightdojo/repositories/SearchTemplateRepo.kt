package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.SearchTemplatesDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.Searchable
import com.example.weightdojo.datatransferobjects.RepoResponse

interface SearchTemplateRepo<Template> {
    suspend fun searchTemplate(term: String): RepoResponse<List<Template>>
    suspend fun deleteTemplate(mealTemplate: Template): RepoResponse<Nothing?>
}

class SearchMealTemplateRepo(
    private val searchTemplatesDao: SearchTemplatesDao
) : SearchTemplateRepo<MealTemplate> {
    override suspend fun searchTemplate(term: String): RepoResponse<List<MealTemplate>> {
        return try {
            val results = searchTemplatesDao.searchMealTemplates(term)

            RepoResponse(
                success = true, data = results
            )
        } catch (e: Exception) {
            RepoResponse(
                success = false, data = listOf(), errorMessage = e.message
            )
        }
    }

    override suspend fun deleteTemplate(mealTemplate: MealTemplate): RepoResponse<Nothing?> {
        return try {
            searchTemplatesDao.deleteMealTemplateHandler(mealTemplate.mealTemplateId)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {
            RepoResponse(success = false, data = null, errorMessage = e.message)
        }
    }
}

class SearchIngredientTemplateRepo(
    private val searchTemplatesDao: SearchTemplatesDao
) : SearchTemplateRepo<IngredientTemplate> {
    override suspend fun searchTemplate(term: String): RepoResponse<List<IngredientTemplate>> {
        return try {
            val results = searchTemplatesDao.searchIngredientTemplates(term)

            RepoResponse(
                success = true, data = results
            )
        } catch (e: Exception) {
            RepoResponse(
                success = false, data = listOf(), errorMessage = e.message
            )
        }
    }

    override suspend fun deleteTemplate(ingTemplate: IngredientTemplate): RepoResponse<Nothing?> {
        return try {
            searchTemplatesDao.deleteIngTemplateHandler(ingTemplate)

            RepoResponse(success = true, data = null)
        } catch (e: Exception) {
            RepoResponse(success = false, data = null, errorMessage = e.message)
        }
    }
}