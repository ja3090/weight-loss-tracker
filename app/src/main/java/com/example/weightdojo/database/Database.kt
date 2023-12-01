package com.example.weightdojo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.WeightDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.Weight

@Database(entities = [Weight::class, Meal::class, Config::class, Ingredient::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao
    abstract fun configDao(): ConfigDao
}

class Database(
    context: Context? = null
) {
    companion object {
        private var db: AppDatabase? = null
    }

    fun getDb(): AppDatabase {
        if (db == null) {
            throw Exception("db is null")
        } else {
            return db as AppDatabase
        }
    }
    private fun setDb(context: Context?) {
        if (db == null) {
            if (context == null) throw Exception("Provide context arg")
            val instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "wd-database"
            ).build()

            db = instance
        }
    }

    init {
        setDb(context)
    }
}