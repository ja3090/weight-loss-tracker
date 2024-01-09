package com.example.weightdojo.repositories

import com.example.weightdojo.AppConfig
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

interface MealRepository {
    fun insertMeal(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float? = null,
        totalFat: Float? = null,
        totalCalories: Float,
        totalProtein: Float? = null,
        calorieUnit: CalorieUnits,
        name: String,
    ): Long
    fun getMealByDayId(dayId: Long): Meal
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val dayDao: DayDao
) : MealRepository {
    override fun insertMeal(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        calorieUnit: CalorieUnits,
        name: String,
    ): Long {
        return mealDao.handleMealInsert(
            dayId = dayId,
            date = date,
            totalCarbohydrates = totalCarbohydrates,
            totalFat = totalFat,
            totalCalories = CalorieUnit.convert(
                from = calorieUnit,
                value = totalCalories
            ),
            totalProtein = totalProtein,
            name = name
        )
    }

    override fun getMealByDayId(dayId: Long): Meal {
        return mealDao.getMealByDayId(dayId)
    }
}