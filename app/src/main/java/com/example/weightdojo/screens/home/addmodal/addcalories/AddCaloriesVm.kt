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
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddCaloriesVm(
    private val database: AppDatabase,
    private val dayId: Long?,
    private val mealTemplateRepo: MealTemplateRepo = MealTemplateRepoImpl(database.mealTemplateDao())
) : ViewModel() {
    var stateHandler by mutableStateOf(AddCaloriesStateHandler(dayId = dayId))

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

    private suspend fun templateSubmitHandler(overwrite: Boolean): Boolean {
        return mealTemplateRepo.insertMealTemplate(
            mealState = stateHandler.state.mealState,
            ingredientStateList = stateHandler.state.ingredientList,
            overwrite = overwrite
        )
    }

    suspend fun createTemplate(): Boolean {

        val mealTemplateId = stateHandler.state.mealState?.mealTemplateId
        val overwriteTemplate = stateHandler.state.overwriteTemplate

        if (mealTemplateId == null) {
            Log.e(
                "mealStateError",
                "mealState is required and is null"
            )
            return false
        }
        if (overwriteTemplate == null) {
            Log.e(
                "createTemplateError",
                "overwriteTemplate is required and is null. " +
                        "User should be prompted to choose whether this template should be overwritten"
            )
            return false
        }

        if (mealTemplateId == 0L) {
            return templateSubmitHandler(false)
        }

        if (mealTemplateId != 0L) {
            return templateSubmitHandler(overwriteTemplate)
        }

        return false
    }
}