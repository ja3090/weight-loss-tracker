package com.example.weightdojo.utils

import android.util.Log
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.DayWithMeals
import com.example.weightdojo.repositories.IngredientRepositoryImpl
import com.example.weightdojo.repositories.MealRepositoryImpl
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlin.random.Random

data class DayDTO(
    val date: LocalDate,
    val id: Long
)

class Seeder(
    private val database: AppDatabase,
    private val faker: Faker = faker { }
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

    private fun deleteAll() {
        database.ingredientDao()._DELETE_ALL()
        database.mealDao()._DELETE_ALL()
        database.dayDao()._DELETE_ALL()
    }

    private fun seedDb() {
        deleteAll()

        val date = LocalDate.now()

        var currentDate = date.minusYears(1L)
        var compareDates = currentDate.compareTo(date)
        var counter = 0

        while (compareDates <= 0) {
            val thisDate = currentDate

            val day = enterDay(thisDate)

            val numberOfMeals = Random.nextInt(1, 5)

            enterWeight(day)
//            if (counter % 3 == 0 || counter % 4 == 0) enterWeight(day)
            for (i in 0..numberOfMeals) {
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
        val row = dao.getDay(date)

        return DayDTO(date = date, id = row.day.id)
    }

    private fun enterWeight(day: DayDTO) {
        val dao = database.dayDao()

        dao.setWeight(
            dayId = day.id,
            weight = Random.nextInt(60, 80).toFloat(),
        )
    }

    private fun enterMeal(day: DayDTO) {
        val mealRepo = MealRepositoryImpl(database.mealDao(), database.dayDao())

        val id = mealRepo.insertMeal(
            dayId = day.id,
            date = day.date,
            totalCarbohydrates = randomOrNull(40, 100),
            totalFat = randomOrNull(44, 100),
            totalProtein = randomOrNull(40, 105),
            totalCalories = random(),
            calorieUnit = if (Random.nextFloat() > 0.5f) CalorieUnits.KCAL else CalorieUnits.KJ,
            name = faker.food.dish()
        )

        if (Random.nextFloat() < 0.5f) {
            enterIngredients(id)
        }
    }

    private fun enterIngredients(mealId: Long) {
        val ingredientRepo = IngredientRepositoryImpl(database.ingredientDao())

        val numberOfIngredients = Random.nextInt(3, 9)

        for (i in 0 until numberOfIngredients) {

            val calories = random(50, 150)
            val carbs = randomOrNull(20, 100)
            val protein = randomOrNull(20, 100)
            val fat = randomOrNull(20, 100)

            ingredientRepo.insertIngredient(
                name = faker.food.ingredients(),
                carbs = carbs,
                calories = calories,
                protein = protein,
                fat = fat,
                mealId = mealId
            )
        }
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