package com.example.weightdojo.components.searchtemplates

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.NutrimentSummary
import com.example.weightdojo.datatransferobjects.NutritionBreakdown
import com.example.weightdojo.repositories.IngredientRepository
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.mealrepo.MealRepository
import com.example.weightdojo.repositories.mealrepo.MealRepositoryImpl
import kotlinx.coroutines.async

data class SearchTemplatesState<K : NutrimentSummary, V : NutritionBreakdown<K>>(
    val templates: List<V> = listOf(),
    val errorMessage: String? = null
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

    suspend fun searchTemplates(term: String) {
        val res = viewModelScope.async {
            when (templates) {
                Templates.INGREDIENT -> ingredientRepository.searchTemplates(term)
                Templates.MEAL -> mealRepository.searchTemplates(term)
            }
        }.await()

        if (!res.success) state = state.copy(errorMessage = res.errorMessage)

        state = state.copy(templates = res.data as List<Template>)
    }
}