package com.example.weightdojo.screens.main

import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.repositories.ConfigRepository
import com.example.weightdojo.repositories.ConfigRepositoryImpl
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.weightdojo.utils.ConfigSessionCache

enum class Screens {
    LockFirstTime, Lock, Home, Settings, Charts, AddWeight
}

data class MainState(
    var config: Config? = null,
    var authenticated: Boolean = false,
    var startDestination: Enum<Screens>? = null,
)

class MainViewModel(
    private val database: AppDatabase,
    private val configSessionCache: ConfigSessionCache,
    private val repo: ConfigRepository = ConfigRepositoryImpl(database.configDao())
) : ViewModel() {

    var state by mutableStateOf(
        MainState()
    )

    private fun getStartDest(config: Config?): Enum<Screens> {
        return if (config == null) Screens.LockFirstTime
        else if (config.passcodeEnabled) Screens.Lock
        else Screens.Home
    }

    init {
        var config: Config?

        runBlocking {
            config = repo.getConfig()
        }

        configSessionCache.saveSession(config)

        val startDest = getStartDest(config)

        state = state.copy(config = config, startDestination = startDest)
    }

    fun setAuthenticated(boolean: Boolean) {
        state = state.copy(authenticated = boolean)
    }
}