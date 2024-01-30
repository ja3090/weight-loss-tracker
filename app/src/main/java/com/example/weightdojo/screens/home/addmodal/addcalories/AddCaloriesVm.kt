package com.example.weightdojo.screens.home.addmodal.addcalories

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.repositories.MealTemplateRepo
import com.example.weightdojo.repositories.MealTemplateRepoImpl
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.repositories.MealRepository
import com.example.weightdojo.repositories.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddCaloriesVm(
    private val database: AppDatabase,
    private val dayId: Long?,
    private val mealTemplateRepo: MealTemplateRepo = MealTemplateRepoImpl(database.mealTemplateDao()),
    private val mealRepository: MealRepository = MealRepositoryImpl(database.mealDao())
) : ViewModel() {
    var stateHandler by mutableStateOf(AddCaloriesStateHandler(dayId = dayId))
    private val addCaloriesValidation = AddCaloriesValidation()

    fun moveToAddNewMeal(mealTemplateId: Long? = null) {
        if (mealTemplateId == null) {
            return stateHandler.initMealCreation(dayId)
        }

        viewModelScope.launch(Dispatchers.IO) {
            mealWithIngredientsDbCall(mealTemplateId)
        }
    }

    private suspend fun mealWithIngredientsDbCall(mealTemplateId: Long) {
        val response =
            mealTemplateRepo.getMealTemplateWithIngredientsById(mealTemplateId, dayId)

        if (!response.success || response.data == null) {
            Log.e("error", response.errorMessage.toString())
            return
        }

        withContext(Dispatchers.Main) {
            stateHandler.setMealTemplate(
                mealState = response.data.first,
                ingredientList = response.data.second
            )
        }
    }

    suspend fun overwriteTemplate(): Boolean {
        val mealState = stateHandler.state.mealState

        if (mealState == null) {
            Log.e("Error", "mealState is null")
            return false
        }

        val validate = addCaloriesValidation.validateSubmission(stateHandler.state)

        if (!validate.success) {
            stateHandler.setErrorMessage(error = validate.errorMessage)
            return false
        }

        val mealTemplate = stateHandler.templateConverter.toMealTemplate(mealState)

        val job = viewModelScope.async(Dispatchers.IO) {
            mealTemplateRepo.overwriteTemplate(mealTemplate, stateHandler.state.ingredientList)
        }

        val res = job.await()

        if (!res.success) {
            stateHandler.setErrorMessage(res.errorMessage)
        }

        return res.success
    }

    suspend fun submitMeal(): Boolean {
        val mealState = stateHandler.state.mealState
        val dayId = dayId

        if (mealState == null || dayId == null) {
            Log.e("Error", "one of the required arguments is null")
            return false
        }

        val validate = addCaloriesValidation.validateSubmission(stateHandler.state)

        if (!validate.success) {
            stateHandler.setErrorMessage(error = validate.errorMessage)
            return false
        }

        val meal = Meal(
            name = mealState.name,
            totalFat = mealState.totalFat,
            totalProtein = mealState.totalProtein,
            totalCarbohydrates = mealState.totalCarbohydrates,
            totalCalories = mealState.totalCalories,
            dayId = dayId
        )

        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.handleInsert(
                mealState = meal,
                ingredientList = stateHandler.state.ingredientList
            )
        }

        val res = job.await()

        if (!res.success) {
            stateHandler.setErrorMessage(res.errorMessage)
        }

        return res.success
    }

    suspend fun createTemplate(): Boolean {
        val validate = addCaloriesValidation.validateSubmission(stateHandler.state)

        if (!validate.success) {
            stateHandler.setErrorMessage(error = validate.errorMessage)
            return false
        }

        val job = viewModelScope.async(Dispatchers.IO) {
            mealTemplateRepo.createMealTemplate(
                mealState = stateHandler.state.mealState,
                ingredientStateList = stateHandler.state.ingredientList,
            )
        }

        val res = job.await()

        if (!res.success) {
            stateHandler.setErrorMessage(res.errorMessage)
        }

        return res.success
    }
}