package com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.Searchable
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.repositories.MealTemplateRepo
import com.example.weightdojo.repositories.MealTemplateRepoImpl
import com.example.weightdojo.repositories.SearchIngredientTemplateRepo
import com.example.weightdojo.repositories.SearchMealTemplateRepo
import com.example.weightdojo.repositories.SearchTemplateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SearchTemplate(
    val templates: List<Searchable> = listOf(),
    val activeTemplate: Searchable? = null
)

open class SearchTemplatesBaseVM<Template : Searchable>(
    private val templateRepo: SearchTemplateRepo<Template>
) : ViewModel() {

    var state by mutableStateOf(
        SearchTemplate()
    )

    fun reset() {
        state = state.copy(templates = listOf(), activeTemplate = null)
    }

    fun makeActive(template: Searchable) {
        val isActive = template == state.activeTemplate

        setActive(template, isActive)
    }

    private fun setActive(template: Searchable, isActive: Boolean) {
        state = if (isActive) {
            state.copy(
                activeTemplate = null,
            )
        } else {
            state.copy(
                activeTemplate = template,
            )
        }
    }

    fun searchTemplates(term: String) {
        if (term.isEmpty()) state = state.copy(templates = listOf())
        else {
            viewModelScope.launch {
                getSearchResults(term, templateRepo::searchTemplate)
            }
        }
    }

    private suspend fun getSearchResults(
        term: String,
        search: suspend (term: String) -> RepoResponse<List<Template>>
    ) {
        val job = viewModelScope.async(Dispatchers.IO) {
            search(term)
        }

        val res = job.await()

        withContext(Dispatchers.Main) {
            state = state.copy(templates = res.data)
        }
    }
}

class SearchMealTemplatesVM(
    database: AppDatabase = MyApp.appModule.database
) : SearchTemplatesBaseVM<MealTemplate>(
    templateRepo = SearchMealTemplateRepo(database.searchTemplatesDao())
)
class SearchIngredientTemplatesVM(
    database: AppDatabase = MyApp.appModule.database
) : SearchTemplatesBaseVM<IngredientTemplate>(
    templateRepo = SearchIngredientTemplateRepo(database.searchTemplatesDao())
)