package com.example.weightdojo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.example.weightdojo.screens.main.MainScreen
import com.example.weightdojo.ui.AppTheme
import com.example.weightdojo.utils.Seeder
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("session", MyApp.appModule.configSessionCache.getActiveSession().toString())
        Log.d("currentDate", MyApp.appModule.currentDate.toString())
        Log.d("currentDateAsString", MyApp.appModule.currentDateAsString)

        if (AppConfig.seedDatabase) {
            val seeder = Seeder(MyApp.appModule.database)

            runBlocking {
                seeder.execute()
            }
        }

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}