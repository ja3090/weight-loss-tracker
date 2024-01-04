package com.example.weightdojo

import android.content.Context
import android.content.SharedPreferences
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.utils.ConfigSessionCache
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface AppModule {
    val database: AppDatabase
    val configSessionCache: ConfigSessionCache
    val currentDateAsString: String
    val currentDate: LocalDate
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    companion object {
        private const val SHARED_PREFS_NAME = "WeightDojoPrefs"
    }

    override val database: AppDatabase by lazy {
        Database.buildDb(appContext, false)
    }

    override val configSessionCache: ConfigSessionCache by lazy {
        val sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        ConfigSessionCache(sharedPreferences)
    }

    override val currentDate: LocalDate by lazy {
        val userTimezone = ZoneId.systemDefault()
        LocalDate.now(userTimezone)
    }

    override val currentDateAsString: String by lazy {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        currentDate.format(dateFormat)
    }
}