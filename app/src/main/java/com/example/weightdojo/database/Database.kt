package com.example.weightdojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weightdojo.database.dao.CalorieChartDao
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.dao.WeightChartDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal

@Database(
    entities = [Meal::class, Config::class, Ingredient::class, Day::class, Date::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun dayDao(): DayDao
    abstract fun mealDao(): MealDao
    abstract fun calorieChartDao(): CalorieChartDao
    abstract fun weightChartDao(): WeightChartDao
}

class Database {
    companion object {
        fun buildDb(context: Context, useTestDb: Boolean): AppDatabase {
            if (useTestDb) {
                return Room.inMemoryDatabaseBuilder(
                    context,
                    AppDatabase::class.java
                ).allowMainThreadQueries().build()
            } else {
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "wd-database"
                ).allowMainThreadQueries().build()
            }
        }
    }
}