package com.example.weightdojo.components.mealcreation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.toast
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.repositories.IngredientRepository
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.mealrepo.MealRepository
import com.example.weightdojo.repositories.mealrepo.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

enum class MealCreationOptions {
    EDITING,
    CREATING
}

class MealCreationVM(
    singleMealDetailed: SingleMealDetailed?,
    private val dayId: Long,
    database: AppDatabase = MyApp.appModule.database,
    private val mealRepository: MealRepository = MealRepositoryImpl(database),
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl(database.ingredientDao()),
) : ViewModel() {
    val stateHandler by mutableStateOf(MealCreationStateHandler(singleMealDetailed, dayId))
    val validator = MealCreationValidation()

    suspend fun submitEdit(): RepoResponse<Unit?> {
        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.updateMeal(stateHandler.state.singleMealDetailed)
        }

        return job.await()
    }

    suspend fun submitMeal(): RepoResponse<Unit?> {
        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.enterMeal(stateHandler.state.singleMealDetailed)
        }

        return job.await()
    }

    private suspend fun getIngredientDetailed(id: Long) {
        val res = viewModelScope.async(Dispatchers.IO) {
            ingredientRepository.getSingleDetailedIngredient(id)
        }.await()

        if (res.success && res.data !== null) stateHandler.addIngredientToMeal(res.data)
        else stateHandler.setError(res.errorMessage)
    }

    fun useTemplate(id: Long) {
        viewModelScope.launch { getIngredientDetailed(id) }
    }
}