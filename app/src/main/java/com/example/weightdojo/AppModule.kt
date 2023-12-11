package com.example.weightdojo

import android.content.Context
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database

interface AppModule {
    val database: AppDatabase
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    override val database: AppDatabase by lazy {
        Database.buildDb(appContext, false)
    }
}