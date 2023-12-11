package com.example.weightdojo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.weightdojo.database.Database
import com.example.weightdojo.screens.MainScreen
import com.example.weightdojo.ui.AppTheme
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = runBlocking {
            MyApp.appModule.database.configDao().getConfig()
        }

        setContent {
            AppTheme {
                MainScreen(config = config)
            }
        }
    }
}