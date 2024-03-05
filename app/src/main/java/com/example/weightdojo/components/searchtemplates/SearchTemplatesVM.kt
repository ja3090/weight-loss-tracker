package com.example.weightdojo.components.searchtemplates

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.toast
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.NutrimentSummary
import com.example.weightdojo.datatransferobjects.NutritionBreakdown
import com.example.weightdojo.repositories.IngredientRepository
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.mealrepo.MealRepository
import com.example.weightdojo.repositories.mealrepo.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class SearchTemplatesState<K : NutrimentSummary, V : NutritionBreakdown<K>>(
    val templates: List<V> = listOf(),
    val errorMessage: String? = null,
)

enum class Templates {
    MEAL, INGREDIENT
}

class SearchTemplatesVM<NutrimentType : NutrimentSummary, Template : NutritionBreakdown<NutrimentType>>(
    private val templates: Templates,
    private val database: AppDatabase = MyApp.appModule.database,
    private val mealRepository: MealRepository = MealRepositoryImpl(database),
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl(database.ingredientDao()),
) : ViewModel() {
    var state by mutableStateOf(SearchTemplatesState<NutrimentType, Template>())

    private suspend fun searchTemplatesHandler(term: String) {
        if (term.isEmpty()) {
            state = state.copy(templates = listOf())
            return
        }

        val res = viewModelScope.async(Dispatchers.IO) {
            when (templates) {
                Templates.INGREDIENT -> ingredientRepository.searchTemplates(term)
                Templates.MEAL -> mealRepository.searchTemplates(term)
            }
        }.await()

        if (!res.success) state = state.copy(errorMessage = res.errorMessage)

        state = state.copy(templates = res.data as List<Template>)
    }

    fun searchTemplates(term: String) {
        viewModelScope.launch { searchTemplatesHandler(term) }
    }

    private suspend fun deleteTemplate(id: Long, context: Context) {
        val res = viewModelScope.async(Dispatchers.IO) {
            when (templates) {
                Templates.INGREDIENT -> ingredientRepository.deleteIngredientTemplate(id)
                Templates.MEAL -> mealRepository.deleteMealTemplate(id)
            }
        }.await()

        state = if (res.success) {
            toast("Success", context)
            val removeFromList = state.templates.filter { it.id != id }
            state.copy(templates = removeFromList)
        } else state.copy(errorMessage = res.errorMessage)
    }
    fun useDeleteTemplate(id: Long, context: Context) {
        viewModelScope.launch { deleteTemplate(id, context) }
    }
}