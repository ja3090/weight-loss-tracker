package com.example.weightdojo.utils

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.DayWithWeightAndMeals
import com.example.weightdojo.repositories.MealRepositoryImpl
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlin.random.Random

data class DayDTO(
    val date: LocalDate,
    val id: Long
)

class Seeder(
    private val database: AppDatabase,
) {
    fun execute() {
        runBlocking {
            try {
                seedDb()
            } catch (e: Exception) {
                throw Error(e)
            }
        }
    }

    private fun seedDb() {
        database.mealDao()._DELETE_ALL()
        database.weightDao()._DELETE_ALL()
        database.dayDao()._DELETE_ALL()

        val date = LocalDate.now()

        var currentDate = date.minusYears(1L)
        var compareDates = currentDate.compareTo(date)
        var counter = 0

        while (compareDates <= 0) {
            val thisDate = currentDate

            val day = enterDay(thisDate)

            enterWeight(day)
//            if (counter % 3 == 0 || counter % 4 == 0) enterWeight(day)
            for (i in 0..2) {
                enterMeal(day)
            }


            currentDate = currentDate.plusDays(1L)
            counter++
            compareDates = currentDate.compareTo(date)
        }
    }

    private fun enterDay(date: LocalDate): DayDTO {
        val dao = database.dayDao()

        dao.insert(date)
        val row = dao.getDays(date) as DayWithWeightAndMeals

        return DayDTO(date = date, id = row.day.id)
    }

    private fun enterWeight(day: DayDTO) {
        val dao = database.weightDao()

        dao.insertWeightEntry(
            dayId = day.id,
            weight = Random.nextInt(60, 80).toFloat(),
            date = day.date,
            weightUnit = if (Random.nextFloat() > 0.5f) WeightUnits.KG else WeightUnits.LBS
        )
    }

    private fun enterMeal(day: DayDTO) {
        val repo = MealRepositoryImpl(database.mealDao(), database.dayDao())

        repo.insertMeal(
            dayId = day.id,
            date = day.date,
            totalCarbohydrates = randomOrNull(40, 100),
            totalFat = randomOrNull(44, 100),
            totalProtein = randomOrNull(40, 105),
            totalCalories = random(),
            calorieUnit = if (Random.nextFloat() > 0.5f) CalorieUnits.KCAL else CalorieUnits.KJ
        )
    }

    private fun random(from: Int = 500, until: Int = 800): Float {
        return Random.nextInt(from, until)
            .toFloat()
    }

    private fun randomOrNull(from: Int, until: Int): Float? {
        return if (Random.nextFloat() > 0.5) {
            null
        } else {
            Random.nextInt(from, until)
                .toFloat()
        }
    }
}