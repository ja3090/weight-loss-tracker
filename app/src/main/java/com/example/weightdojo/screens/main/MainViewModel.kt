package com.example.weightdojo.screens.main

import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.models.Config
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.weightdojo.utils.ConfigSessionCache

enum class Screens {
    LockFirstTime, Lock, Home, Settings, Charts
}

data class MainState(
    var config: Config? = null,
    var authenticated: Boolean = false,
    var startDestination: Screens? = null,
)

class MainViewModel(
    configSessionCache: ConfigSessionCache,
) : ViewModel() {

    var state by mutableStateOf(
        MainState()
    )

    private fun getStartDest(config: Config?): Screens {
        return if (config == null) Screens.LockFirstTime
        else if (config.passcodeEnabled) Screens.Lock
        else Screens.Home
    }

    init {
        var config: Config?

        runBlocking {
            config = configSessionCache.getActiveSession()
        }

        val startDest = getStartDest(config)

        state = state.copy(config = config, startDestination = startDest)
    }

    fun setAuthenticated(boolean: Boolean) {
        state = state.copy(authenticated = boolean)
    }
}