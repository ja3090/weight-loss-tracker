package com.example.weightdojo.screens.home.meallist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.repositories.mealrepo.MealRepository
import com.example.weightdojo.repositories.mealrepo.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class MealListVMState(
    val activeMeal: MealWithNutrimentData? = null,
    val mealCreationModalOpen: Boolean = false,
    val singleMealDetailed: SingleMealDetailed? = null,
    val errorMessage: String? = null
)

class MealListViewModel(
    private val database: AppDatabase = MyApp.appModule.database,
    private val mealRepository: MealRepository = MealRepositoryImpl(database)
) : ViewModel() {

    var state by mutableStateOf(MealListVMState())

    fun reset() {
        state = MealListVMState()
    }

    fun dismissErrorMessage() {
        state = state.copy(errorMessage = null)
    }

    fun openModal(boolean: Boolean) {
        state = state.copy(mealCreationModalOpen = boolean)
    }

    fun setActive(meal: MealWithNutrimentData) {
        if (meal == state.activeMeal) state = state.copy(activeMeal = null)
        else state = state.copy(activeMeal = meal)
    }

    private suspend fun getSingleMeal(mealId: Long) {
        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.getSingleMealDetailed(mealId)
        }

        val res = job.await()

        if (res.success && res.data !== null) {
            state = state.copy(singleMealDetailed = res.data, mealCreationModalOpen = true)
        } else {
            state = state.copy(errorMessage = res.errorMessage)
        }

        Log.d("res", res.toString())
    }

    fun editMeal(mealId: Long) {
        viewModelScope.launch { getSingleMeal(mealId) }
    }


}