package com.example.weightdojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weightdojo.database.dao.CalorieChartDao
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.dao.IngredientTemplateDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.dao.MealIngredientTemplateDao
import com.example.weightdojo.database.dao.MealTemplateDao
import com.example.weightdojo.database.dao.WeightChartDao
import com.example.weightdojo.database.dao.seeder.SeedMealTemplateDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate

@Database(
    entities = [
        Meal::class,
        Config::class,
        Ingredient::class,
        Day::class,
        Date::class,
        MealTemplate::class,
        MealIngredientTemplate::class,
        IngredientTemplate::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun dayDao(): DayDao
    abstract fun mealDao(): MealDao
    abstract fun calorieChartDao(): CalorieChartDao
    abstract fun weightChartDao(): WeightChartDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun mealTemplateDao(): MealTemplateDao
    abstract fun mealIngredientTemplateDao(): MealIngredientTemplateDao
    abstract fun ingredientTemplateDao(): IngredientTemplateDao
    abstract fun seedMealTemplateDao(): SeedMealTemplateDao
}

class Database {
    companion object {
        fun buildDb(context: Context, useTestDb: Boolean): AppDatabase {
            return if (useTestDb) {
                Room.inMemoryDatabaseBuilder(
                    context, AppDatabase::class.java
                ).allowMainThreadQueries().build()
            } else {
                Room.databaseBuilder(
                    context, AppDatabase::class.java, "wd-database"
                ).allowMainThreadQueries().build()
            }
        }
    }
}