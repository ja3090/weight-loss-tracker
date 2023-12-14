package com.example.weightdojo.screens.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.repositories.ConfigRepository
import com.example.weightdojo.repositories.ConfigRepositoryImpl
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

enum class Screens {
    LockFirstTime,
    Lock,
    Home,
    Entries,
    Settings,
    Graphs
}
data class MainState(
    var config: Config? = null,
    var authenticated: Boolean = false,
    var destination: Enum<Screens> = Screens.Home,
)

class MainViewModel(
    private val database: AppDatabase,
    private val repo: ConfigRepository = ConfigRepositoryImpl(database.configDao())
): ViewModel() {
    var state by mutableStateOf(MainState())

    private fun getStartDest(config: Config?): Enum<Screens> {
        return if (config == null) Screens.LockFirstTime
        else if (config.passcodeEnabled) Screens.Lock
        else Screens.Home
    }

    init {
        runBlocking {
            val config = repo.getConfig()
            val startDest = getStartDest(config)

            state = state.copy(config = config, destination = startDest)
        }
    }

    fun setAuthenticated(boolean: Boolean) {
        state = state.copy(authenticated = boolean)
    }
}