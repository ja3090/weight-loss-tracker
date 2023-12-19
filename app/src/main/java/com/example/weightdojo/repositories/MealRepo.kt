package com.example.weightdojo.repositories

import com.example.weightdojo.AppConfig
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.MealDao
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
        calorieUnit: CalorieUnits
    )
}

class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val dayDao: DayDao? = null
) : MealRepository {
    override fun insertMeal(
        dayId: Long,
        date: LocalDate,
        totalCarbohydrates: Float?,
        totalFat: Float?,
        totalCalories: Float,
        totalProtein: Float?,
        calorieUnit: CalorieUnits
    ) {
        if (dayDao == null) throw Error("You must inject DayDao if you intend to use this function")

        mealDao.insertMealEntry(
            dayId = dayId,
            date = date,
            totalCarbohydrates = totalCarbohydrates,
            totalFat = totalFat,
            totalCalories = CalorieUnit.convert(
                from = calorieUnit,
                value = totalCalories
            ),
            totalProtein = totalProtein,
            calorieUnit = calorieUnit
        )

        val averages = mealDao.getNutritionAverages(dayId)

        dayDao.setCalorieStats(
            calAvg = averages.calAvg,
            proAvg = averages.proAvg,
            fatAvg = averages.fatAvg,
            carbAvg = averages.carbAvg,
            dayId = dayId
        )
    }
}