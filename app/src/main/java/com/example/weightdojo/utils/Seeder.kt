package com.example.weightdojo.utils

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Calorie
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
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
    private lateinit var mealIds: List<Long>
    private lateinit var ingredients: List<Ingredient>

    private fun randomMeal(): Long {
        return mealIds[Random.nextInt(0, mealIds.size - 1)]
    }

    private fun randomIngredient(): Ingredient {
        return ingredients[Random.nextInt(0, ingredients.size - 1)]
    }

    fun execute() {
        runBlocking {
            try {
                deleteAll()
                mealIds = createMeals()
                ingredients = createIngredients()
                seedDb()
            } catch (e: Exception) {
                throw Error(e)
            }
        }
    }

    private fun deleteAll() {
        database.calorieDao()._DELETE_ALL()
        database.ingredientDao()._DELETE_ALL()
        database.mealDao()._DELETE_ALL()
        database.dayDao()._DELETE_ALL()
    }

    private fun createIngredients(): List<Ingredient> {
        val createdIngredients = mutableListOf<Ingredient>()

        for (i in 0 until 16) {
            val ingredient = Ingredient(
                    name = faker.food.ingredients(),
                    carbohydratesPer100 = randomOrNull(20, 100),
                    proteinPer100 = randomOrNull(20, 100),
                    fatPer100 = randomOrNull(20, 100),
                    caloriesPer100 = random(),
                )

            val id = database.ingredientDao().insertIngredient(ingredient)

            createdIngredients.add(ingredient.copy(id = id))
        }

        return createdIngredients
    }

    private fun createMeals(): List<Long> {
        val mealIds = mutableListOf<Long>()

        for (i in 0 until 16) {
            val id = database.mealDao().insertMealEntry(Meal(
                name = faker.food.dish()
            ))

            mealIds.add(id)
        }

        return mealIds
    }

    private fun seedDb() {
        val date = LocalDate.now()

        var currentDate = date.minusYears(1L)
        var compareDates = currentDate.compareTo(date)
        var counter = 0

        while (compareDates <= 0) {
            val thisDate = currentDate

            val day = enterDay(thisDate)

            val numberOfMeals = Random.nextInt(1, 5)

            enterWeight(day)

            for (i in 0..numberOfMeals) {
                enterIngredientMealCross(day)
            }

            currentDate = currentDate.plusDays(1L)
            counter++
            compareDates = currentDate.compareTo(date)
        }
    }

    private fun enterDay(date: LocalDate): DayDTO {
        val dao = database.dayDao()

        val id = dao.insert(Day(date = date))

        return DayDTO(date = date, id = id)
    }

    private fun enterWeight(day: DayDTO) {
        val dao = database.dayDao()

        dao.setWeight(
            dayId = day.id,
            weight = Random.nextInt(60, 80).toFloat(),
        )
    }

    private fun enterIngredientMealCross(day: DayDTO) {
        val randomNoOfIngredients = Random.nextInt(1, 5)
        val mealId = randomMeal()

        for (i in 1 until randomNoOfIngredients) {
            val ingredient = randomIngredient()
            val grams = random(10, 100)

            database.calorieDao().calorieInsertionHandler(
                Calorie(
                    totalCalories = (grams / 100) * ingredient.caloriesPer100,
                    totalFat = randomOrNull(20, 100),
                    totalProtein = randomOrNull(20, 100),
                    totalCarbohydrates = randomOrNull(20, 100),
                    grams = grams,
                    dayId = day.id,
                    mealId = mealId,
                    ingredientId = ingredient.id
                )
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