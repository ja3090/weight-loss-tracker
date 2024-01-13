package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.utils.Hashing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface IngredientRepository {
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>
    fun updateIngredients(
        ingredientStates: List<IngredientState>, mealId: Long, dayId: Long
    )
}

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {

    override fun getIngredientsByMealId(mealId: Long): List<Ingredient> {
        return ingredientDao.getIngredientsByMealId(mealId)
    }

    override fun updateIngredients(
        ingredientStates: List<IngredientState>, mealId: Long, dayId: Long
    ) {
        ingredientDao.ingredientUpdateHandler(
            ingredientStates = ingredientStates, mealId = mealId, dayId = dayId
        )
    }
}