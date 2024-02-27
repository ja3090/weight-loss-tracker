package com.example.weightdojo.components.mealcreation

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.repositories.MealRepository
import com.example.weightdojo.repositories.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.UUID

enum class MealCreationOptions {
    EDITING,
    CREATING
}

class MealCreationVM(
    singleMealDetailed: SingleMealDetailed?,
    database: AppDatabase = MyApp.appModule.database,
    private val mealRepository: MealRepository = MealRepositoryImpl(database.mealDao())
) : ViewModel() {
    val stateHandler by mutableStateOf(MealCreationStateHandler(singleMealDetailed))
    val validator = MealCreationValidation()

    suspend fun submitEdit(): RepoResponse<Unit?> {
        val job = viewModelScope.async {
            mealRepository.updateMeal(stateHandler.state.singleMealDetailed)
        }

        return job.await()
    }

    suspend fun submitMeal(): RepoResponse<Unit?> {
        return RepoResponse(true, null)
    }
}