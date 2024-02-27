package com.example.weightdojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weightdojo.database.dao.CalorieChartDao
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.IngredientDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.dao.NutrimentDao
import com.example.weightdojo.database.dao.NutrimentIngredientDao
import com.example.weightdojo.database.dao.NutrimentMealDao
import com.example.weightdojo.database.dao.SeedDatabase
import com.example.weightdojo.database.dao.WeightChartDao
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredient
import com.example.weightdojo.database.models.Nutriment
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal

@Database(
    entities = [
        Meal::class,
        Ingredient::class,
        Day::class,
        Date::class,
        Nutriment::class,
        NutrimentIngredient::class,
        NutrimentMeal::class,
        MealIngredient::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dayDao(): DayDao
    abstract fun mealDao(): MealDao
    abstract fun calorieChartDao(): CalorieChartDao
    abstract fun weightChartDao(): WeightChartDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun nutrimentDao(): NutrimentDao
    abstract fun nutrimentMealDao(): NutrimentMealDao
    abstract fun nutrimentIngredientDao(): NutrimentIngredientDao
    abstract fun seedDatabaseDao(): SeedDatabase
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