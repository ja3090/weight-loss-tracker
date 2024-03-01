package com.example.weightdojo.utils.seeder

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.Nutriment
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
import com.example.weightdojo.utils.totalGrams
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
    private val faker: Faker = faker { },
    private val nutriments: List<String> = listOf(
        "Carbohydrates",
        "Salt",
        "Protein",
        "Fibre"
    ),
    private var nutrimentIds: MutableList<Long> = mutableListOf(),
    private val nutrimentTotals: NutrimentTotals = NutrimentTotals(database),
    private val calorieTotals: CalorieTotals = CalorieTotals(database)
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
        database.nutrimentMealDao()._DELETE_ALL()
        database.nutrimentIngredientDao()._DELETE_ALL()
        database.nutrimentDao()._DELETE_ALL()
        database.ingredientDao()._DELETE_ALL()
        database.mealDao()._DELETE_ALL()
        database.dayDao()._DELETE_ALL()
    }

    private fun createNutriments() {
        for (nutriment in nutriments) {
            val nutrimentToInsert =
                Nutriment(
                    name = nutriment,
                    dailyIntakeTarget = Random.nextInt(60, 70).toFloat()
                )

            val id = database.nutrimentDao().insertNutriment(nutrimentToInsert)

            nutrimentIds.add(id)
        }
    }

    private fun nutrimentsForMealsAndIngredients(ingredient: Ingredient, mealId: Long) {
        for (nutrimentId in nutrimentIds) {
            val gPer100 = random(10, 40)

            val nutrimentIngredient = NutrimentIngredient(
                ingredientId = ingredient.ingredientId,
                nutrimentId = nutrimentId,
                gPer100 = gPer100
            )

            database.nutrimentIngredientDao().insertNutrimentIngredient(nutrimentIngredient)

            val nutrimentMeal = NutrimentMeal(
                nutrimentId = nutrimentId,
                mealId = mealId,
                totalGrams = 0f
            )

            database.nutrimentMealDao().insertNutrimentMeal(nutrimentMeal)

            nutrimentTotals.updateTotal(
                nutrimentId = nutrimentId,
                mealId = mealId,
                value = totalGrams(ingredient.grams, gPer100)
            )
        }
    }

    private fun createIngredients(mealId: Long) {
        val ingredientsNumber = Random.nextInt(2, 5)

        for (i in 1 until ingredientsNumber) {
            val grams = random(20, 50)
            val calsPer100 = random(200, 500)

            val ingredient = Ingredient(
                name = faker.food.ingredients(),
                caloriesPer100 = calsPer100,
                mealId = mealId,
                grams = grams
            )

            val ingId = database.ingredientDao().insertIngredient(ingredient)

            calorieTotals.updateTotal(mealId, totalGrams(grams, calsPer100))

            nutrimentsForMealsAndIngredients(ingredient.copy(ingredientId = ingId), mealId)
        }

        nutrimentTotals.updateMealTotals()
        calorieTotals.updateMealTotals()
    }

    private fun createMeals(dayId: Long): Long {
        return database
            .seedDatabaseDao()
            .insertMealEntry(Meal(name = faker.food.dish(), dayId = dayId))
    }

    private fun seedDb() {
        val date = LocalDate.now()

        var currentDate = date.minusYears(1L)
        var compareDates = currentDate.compareTo(date)
        var counter = 0

        createNutriments()

        while (compareDates <= 0) {
            val thisDate = currentDate

            val day = enterDay(thisDate)

            enterWeight(day)

            for (i in 1..3) {
                val mealId = createMeals(day.id)
                createIngredients(mealId)

                if (i == 1 && counter % 40 == 0) database.seedDatabaseDao().makeTemplate(mealId)
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
}