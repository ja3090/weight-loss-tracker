package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.utils.Hashing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface IngredientRepository {
    fun insertIngredient(
        name: String,
        carbs: Float? = null,
        protein: Float? = null,
        fat: Float? = null,
        calories: Float,
        mealId: Long
    )
    fun getIngredients(mealId: Long): List<Ingredient>?
}

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {
    override fun insertIngredient(
        name: String,
        carbs: Float?,
        protein: Float?,
        fat: Float?,
        calories: Float,
        mealId: Long
    ) {
        ingredientDao.handleIngredientInsert(
            name,
            carbs,
            protein,
            fat,
            calories,
            mealId
        )
    }

    override fun getIngredients(mealId: Long): List<Ingredient>? {
        return ingredientDao.getIngredients(mealId)
    }
}