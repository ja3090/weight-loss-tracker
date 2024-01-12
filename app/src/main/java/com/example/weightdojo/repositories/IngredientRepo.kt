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
}

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {
}