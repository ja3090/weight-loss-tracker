package com.example.weightdojo.components.mealcreation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.weightdojo.screens.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

enum class MealCreationOptions {
    EDITING,
    CREATING
}

class MealCreationVM(
    singleMealDetailed: SingleMealDetailed?,
    mealCreationOptions: MealCreationOptions,
    database: AppDatabase = MyApp.appModule.database,
    private val mealRepository: MealRepository = MealRepositoryImpl(database),
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl(database.ingredientDao()),
) : ViewModel() {
    val stateHandler by mutableStateOf(
        MealCreationStateHandler(
            singleMealDetailed,
            mealCreationOptions
        )
    )
    val validator = MealCreationValidation()

    private suspend fun getIngredientDetailed(id: Long) {
        val res = viewModelScope.async(Dispatchers.IO) {
            ingredientRepository.getSingleDetailedIngredient(id)
        }.await()

        if (res.success && res.data !== null) stateHandler.addIngredientToMeal(res.data)
        else stateHandler.setError(res.errorMessage)
    }

    private suspend fun overwriteTemplate(context: Context) {
        val res = viewModelScope.async(Dispatchers.IO) {
            mealRepository.updateMealTemplate(stateHandler.state.singleMealDetailed)
        }.await()

        if (res.success && res.data !== null) toast("Success", context)
        else stateHandler.setError(res.errorMessage)
    }

    private suspend fun createTemplate(context: Context) {
        val res = viewModelScope.async(Dispatchers.IO) {
            mealRepository.createMealTemplate(stateHandler.state.singleMealDetailed)
        }.await()

        if (res.success && res.data !== null) toast("Success", context)
        else stateHandler.setError(res.errorMessage)
    }

    private suspend fun addNewIngredient() {
        val res = viewModelScope.async(Dispatchers.IO) {
            ingredientRepository.getSingleDetailedIngredient()
        }.await()

        if (res.success) stateHandler.addIngredientToMeal(res.data)
        else stateHandler.setError(res.errorMessage)
    }

    fun addNewIngredientToMeal() {
        viewModelScope.launch { addNewIngredient() }
    }

    private suspend fun getFullMealTemplate(id: Long) {
        val res = viewModelScope.async(Dispatchers.IO) {
            mealRepository.useTemplate(id)
        }.await()

        if (res.success && res.data !== null) stateHandler.useTemplate(res.data)
        else stateHandler.setError(res.errorMessage)
    }

    fun useIngredientTemplate(id: Long) {
        viewModelScope.launch { getIngredientDetailed(id) }
    }

    fun useMealTemplate(id: Long) {
        viewModelScope.launch { getFullMealTemplate(id) }
    }

    fun useOverwriteTemplate(context: Context) {
        viewModelScope.launch { overwriteTemplate(context) }
    }

    fun useCreateTemplate(context: Context) {
        viewModelScope.launch { createTemplate(context) }
    }
}