package com.example.weightdojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.dao.WeightDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.Weight

@Database(
    entities = [Weight::class, Meal::class, Config::class, Ingredient::class, Day::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao
    abstract fun configDao(): ConfigDao
    abstract fun dayDao(): DayDao
    abstract fun mealDao(): MealDao
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