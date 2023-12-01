package com.example.weightdojo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.weightdojo.database.Database
import com.example.weightdojo.screens.MainScreen
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var database: Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Database(applicationContext)

        val config = runBlocking {
            database.getDb().configDao().getConfig()
        }

        setContent {
            MainScreen(config = config)
        }
    }
}