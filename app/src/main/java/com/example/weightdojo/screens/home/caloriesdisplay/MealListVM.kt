package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.datatransferobjects.CalorieEntryForEditing
import com.example.weightdojo.datatransferobjects.CalorieEntryIngredients
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.repositories.CalorieRepo
import com.example.weightdojo.repositories.CalorieRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MealListState(
    val activeMeal: MealData? = null,
    val ingredientList: List<CalorieEntryIngredients>? = null,
    val isEditing: Boolean = false,
    val ingredientListAsState: List<IngredientState>? = null
)

class MealListVM(
    val database: AppDatabase,
    private val calorieRepo: CalorieRepo = CalorieRepoImpl(database)
) : ViewModel() {

    var state by mutableStateOf(MealListState())

    fun setActive(meal: MealData) {
        if (meal.mealId == state.activeMeal?.mealId) {
            removeActive()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                getIngredients(meal)
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun setIngredientsAsState(ingredientId: Long, newState: IngredientState) {
        val updatedList = state.ingredientListAsState?.map {
            if (it.calorieId == ingredientId) {
                newState
            } else it
        }

        state = state.copy(ingredientListAsState = updatedList)
    }

    fun showIngredientListAsState(dayId: Long, mealId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getDetailedIngredients(dayId = dayId, mealId = mealId)
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    private suspend fun getDetailedIngredients(dayId: Long, mealId: Long) {
        val detailedIngredients =
            calorieRepo.getIngredientsDetailedView(dayId = dayId, mealId = mealId)

        withContext(Dispatchers.Main) {
            state = state.copy(ingredientListAsState = detailedIngredients, isEditing = true)
        }
    }

    private suspend fun getIngredients(meal: MealData) {
        val ingredients =
            calorieRepo.getIngredientsForDayAndMeal(dayId = meal.dayId, mealId = meal.mealId)

        withContext(Dispatchers.Main) {
            state = state.copy(
                ingredientList = ingredients,
                activeMeal = meal,
                isEditing = false,
                ingredientListAsState = null
            )
        }
    }

    private fun removeActive() {
        state = state.copy(
            activeMeal = null,
            ingredientList = null,
            isEditing = false,
            ingredientListAsState = null
        )
    }
}