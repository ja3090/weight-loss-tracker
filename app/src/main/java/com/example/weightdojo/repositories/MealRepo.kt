package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.MealDao

interface MealRepository {
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
) : MealRepository {
}