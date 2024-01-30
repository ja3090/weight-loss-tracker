package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.RepoResponse

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
            ingredientStates = ingredientStates,
            mealId = mealId,
            dayId = dayId
        )
    }
}