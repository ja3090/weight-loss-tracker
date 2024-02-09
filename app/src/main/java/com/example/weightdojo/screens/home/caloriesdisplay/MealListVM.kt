package com.example.weightdojo.screens.home.caloriesdisplay

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.datatransferobjects.ConvertTemplates
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.MealData
import com.example.weightdojo.datatransferobjects.MealState
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.repositories.IngredientRepository
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.MealRepository
import com.example.weightdojo.repositories.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

data class MealListState(
    val activeMeal: MealData? = null,
    val ingredientList: List<IngredientState>? = null,
    val isEditing: Boolean = false,
    val addIngredientModalOpen: Boolean = false,
    val activeIngredientId: UUID? = null
)

class MealListVM(
    val database: AppDatabase,
    private val templateConverter: ConvertTemplates = ConvertTemplates(),
    private val ingredientRepo: IngredientRepository = IngredientRepositoryImpl(
        database.ingredientDao()
    ),
    private val mealRepository: MealRepository = MealRepositoryImpl(
        database.mealDao()
    )
) : ViewModel() {

    var state by mutableStateOf(MealListState())

    fun openAddIngModal(boolean: Boolean) {
        state = state.copy(addIngredientModalOpen = boolean)
    }

    fun addIngredient(templ: IngredientTemplate) {
        val ingredientState = templateConverter.toIngredientState(templ)

        state = state.copy(
            ingredientList = state.ingredientList?.plus(ingredientState)
        )
    }

    fun addNewIngredient() {
        state = state.copy(
            ingredientList = state.ingredientList?.plus(IngredientState())
        )
    }

    suspend fun deleteHandler(): RepoResponse<Nothing?> {
        return viewModelScope.async {
            mealRepository.deleteMeal(state.activeMeal?.mealId, state.activeMeal?.dayId)
        }.await()
    }

    fun makeEdits() {
        val dayId = state.activeMeal?.dayId
        val mealId = state.activeMeal?.mealId
        val updatedIngredients = state.ingredientList

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
        dayId: Long,
        ingredients: List<IngredientState>,
        mealId: Long
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

    fun setActiveIngredient(ingredient: IngredientState) {
        state = if (ingredient.internalId == state.activeIngredientId) {
            state.copy(activeIngredientId = null)
        } else {
            state.copy(activeIngredientId = ingredient.internalId)
        }
    }

    fun changeIngredient(ingredient: IngredientState) {

        val updatedList = state.ingredientList?.map {
            if (it.internalId == ingredient.internalId) {
                ingredient
            } else it
        }

        state = state.copy(ingredientList = updatedList)
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

        val updatedList = state.ingredientList?.map {
            if (it === toDelete) {
                it.copy(markedFor = markedFor)
            } else it
        }

        state = state.copy(ingredientList = updatedList)
    }

    private fun getDetailedIngredients() {
        state = state.copy(isEditing = true)
    }

    private suspend fun getIngredients(meal: MealData) {
        val ingredients = ingredientRepo.getIngredientsByMealId(mealId = meal.mealId)
        val ingredientStates = ingredients.map {
            templateConverter.fromIngredientToIngredientState(it)
        }

        withContext(Dispatchers.Main) {
            state = state.copy(
                ingredientList = ingredientStates,
                activeMeal = meal,
                isEditing = false,
            )
        }
    }

    fun removeActive() {
        state = state.copy(
            activeMeal = null,
            ingredientList = null,
            isEditing = false,
        )
    }
}