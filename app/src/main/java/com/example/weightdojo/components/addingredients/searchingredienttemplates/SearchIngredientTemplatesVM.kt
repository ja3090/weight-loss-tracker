package com.example.weightdojo.components.addingredients.searchingredienttemplates

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.IngredientTemplateDao
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.IngredientTemplateRepo
import com.example.weightdojo.repositories.IngredientTemplateRepoImpl
import com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates.SearchMealTemplatesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SearchIngredientTemplatesState(
    val ingredientTemplates: List<IngredientTemplate> = listOf(),
    val activeIngredientTemplate: IngredientTemplate? = null,
)

class SearchIngredientTemplatesVM(
    private val database: AppDatabase,
    private val ingredientTemplateRepo: IngredientTemplateRepo = IngredientTemplateRepoImpl(
        database.ingredientTemplateDao()
    )
) : ViewModel() {

    var state by mutableStateOf(
        SearchIngredientTemplatesState()
    )

    fun reset() {
        state = state.copy(activeIngredientTemplate = null, ingredientTemplates = listOf())
    }

    fun makeActive(mealTemplate: IngredientTemplate) {
        val isActive =
            mealTemplate.ingredientTemplateId == state.activeIngredientTemplate?.ingredientTemplateId

        state = if (isActive) {
            state.copy(
                activeIngredientTemplate = null,
            )
        } else {
            state.copy(
                activeIngredientTemplate = mealTemplate,
            )
        }
    }

    fun setSearchResults(term: String) {
        if (term.isEmpty()) state = state.copy(ingredientTemplates = listOf())
        else {
            viewModelScope.launch {
                getSearchResults(term)
            }
        }
    }

    private suspend fun getSearchResults(term: String) {
        val job = viewModelScope.async(Dispatchers.IO) {
            ingredientTemplateRepo.getIngredientTemplates(term)
        }

        val res = job.await()

        withContext(Dispatchers.Main) {
            state = state.copy(ingredientTemplates = res.data)
        }
    }
}