package com.example.weightdojo.repositories

import com.example.weightdojo.AppConfig
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

interface MealRepository {
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
) : MealRepository {
}