package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.repositories.IngredientRepository
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MealListState(
    val activeMeal: MealData? = null,
    val ingredientList: List<Ingredient>? = null,
    val isEditing: Boolean = false,
    val ingredientListAsState: List<IngredientState>? = null
)

class MealListVM(
    val database: AppDatabase,
    private val ingredientRepo: IngredientRepository = IngredientRepositoryImpl(
        database.ingredientDao()
    )
) : ViewModel() {

    var state by mutableStateOf(MealListState())

    fun makeEdits() {
        val dayId = state.activeMeal?.dayId
        val mealId = state.activeMeal?.mealId
        val updatedIngredients = state.ingredientListAsState

        if (dayId == null || updatedIngredients == null || mealId == null) {
            Log.e(
                "Submission error",
                "required arguments to submit calorie entry not available"
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                submitEdits(dayId, updatedIngredients, mealId)
            } catch (e: Exception) {
                Log.e("Transaction error", e.message.toString())
            }
        }
    }

    private suspend fun submitEdits(
        dayId: Long, ingredients: List<IngredientState>, mealId: Long
    ) {
        ingredientRepo.updateIngredients(ingredients, mealId, dayId)

        withContext(Dispatchers.Main) {
            removeActive()
        }
    }

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

    fun changeGrams(stateId: Long, grams: Float) {
        val updatedList = state.ingredientListAsState?.map {
            if (it.ingredientId == stateId) {
                it.copy(grams = grams)
            } else it
        }

        state = state.copy(ingredientListAsState = updatedList)
    }

    fun showIngredientListAsState() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getDetailedIngredients()
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun deleteIngredient(toDelete: IngredientState) {
        val markedFor = if (toDelete.markedFor == Marked.DELETE) Marked.EDIT else Marked.DELETE

        val updatedList = state.ingredientListAsState?.map {
            if (it === toDelete) {
                it.copy(markedFor = markedFor)
            } else it
        }

        state = state.copy(ingredientListAsState = updatedList)
    }

    private suspend fun getDetailedIngredients() {
        val detailedIngredients = state.ingredientList?.map {
            IngredientState(
                caloriesPer100 = it.caloriesPer100,
                ingredientId = it.id,
                name = it.name,
                grams = it.grams,
                carbsPer100 = it.carbohydratesPer100,
                fatPer100 = it.fatPer100,
                proteinPer100 = it.proteinPer100
            )
        }

        withContext(Dispatchers.Main) {
            state = state.copy(ingredientListAsState = detailedIngredients, isEditing = true)
        }
    }

    private suspend fun getIngredients(meal: MealData) {
        val ingredients = ingredientRepo.getIngredientsByMealId(mealId = meal.mealId)

        withContext(Dispatchers.Main) {
            state = state.copy(
                ingredientList = ingredients,
                activeMeal = meal,
                isEditing = false,
                ingredientListAsState = null
            )
        }
    }

    fun removeActive() {
        state = state.copy(
            activeMeal = null,
            ingredientList = null,
            isEditing = false,
            ingredientListAsState = null
        )
    }
}