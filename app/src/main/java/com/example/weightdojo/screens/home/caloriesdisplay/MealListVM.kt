package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.IngredientDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MealListState(
    val activeMeal: Meal? = null,
    val ingredientList: List<Ingredient>? = null
)

class MealListVM(
    val database: AppDatabase,
    private val ingredientDao: IngredientDao = database.ingredientDao()
): ViewModel() {

    var state by mutableStateOf(MealListState())

    fun setActive(meal: Meal) {
        if (meal.uid == state.activeMeal?.uid) {
            removeActive()
            return
        }

        if (meal.hasIngredients) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    getIngredients(meal)
                } catch (e: Exception) {
                    Log.e("error", e.message.toString())
                }
            }
        } else {
            state = state.copy(activeMeal = meal, ingredientList = null)
        }
    }

    private suspend fun getIngredients(meal: Meal) {
        val ingredients = ingredientDao.getIngredients(meal.uid)

        withContext(Dispatchers.Main) {
            state = state.copy(ingredientList = ingredients, activeMeal = meal)
        }
    }

    fun removeActive() {
        state = state.copy(activeMeal = null, ingredientList = null)
    }
}