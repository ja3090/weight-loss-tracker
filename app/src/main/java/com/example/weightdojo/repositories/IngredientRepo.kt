package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.RepoResponse

interface IngredientRepository {
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>
}

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {

    override fun getIngredientsByMealId(mealId: Long): List<Ingredient> {
        return ingredientDao.getIngredientsByMealId(mealId)
    }
}