package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.SearchTemplatesDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.Searchable
import com.example.weightdojo.datatransferobjects.RepoResponse

interface SearchTemplateRepo<Template> {
    suspend fun searchTemplate(term: String): RepoResponse<List<Template>>
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
}