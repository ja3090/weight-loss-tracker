package com.example.weightdojo.utils

import com.example.weightdojo.database.AppDatabase
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

data class FakeMeal(
    val mealId: Long,
    val ingredients: List<Ingredient>
)

class Seeder(
    private val database: AppDatabase,
    private val faker: Faker = faker { }
) {
    fun execute() {
        runBlocking {
            try {
                deleteAll()
                seedDb()
            } catch (e: Exception) {
                throw Error(e)
            }
        }
    }

    private fun deleteAll() {
        database.mealIngredientTemplateDao()._DELETE_ALL()
        database.ingredientTemplateDao()._DELETE_ALL()
        database.mealTemplateDao()._DELETE_ALL()
        database.ingredientDao()._DELETE_ALL()
        database.mealDao()._DELETE_ALL()
        database.dayDao()._DELETE_ALL()
    }

    private fun createIngredients(mealId: Long, dayId: Long) {
        val createdIngredients = mutableListOf<Ingredient>()

        for (i in 1 until 5) {
            val grams = random(20, 100)
            val calsPer100 = random(200, 500)

            val ingredient = Ingredient(
                name = faker.food.ingredients(),
                carbohydratesPer100 = totalGramsNullable(grams, randomOrNull()),
                proteinPer100 = totalGramsNullable(grams, randomOrNull()),
                fatPer100 = totalGramsNullable(grams, randomOrNull()),
                caloriesPer100 = totalGramsNonNull(grams, calsPer100),
                mealId = mealId,
                grams = grams
            )

            createdIngredients.add(ingredient)
        }

        database.ingredientDao().handleIngredientInsert(
            mealId = mealId,
            dayId = dayId,
            ingredients = createdIngredients
        )
    }

    private fun createMeals(dayId: Long): Long {
        return database
            .mealDao()
            .insertMealEntry(Meal(name = faker.food.dish(), dayId = dayId))
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

            for (i in 1..numberOfMeals) {
                val mealId = createMeals(day.id)
                createIngredients(mealId, day.id)

                if (counter % 15 == 0) database.mealTemplateDao().handleTemplateInsertion(mealId)
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

    private fun random(from: Int = 500, until: Int = 800): Float {
        return Random.nextInt(from, until)
            .toFloat()
    }

    private fun randomOrNull(from: Int = 20, until: Int = 100): Float? {
        return if (Random.nextFloat() > 0.5) {
            null
        } else {
            Random.nextInt(from, until)
                .toFloat()
        }
    }
}