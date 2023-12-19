package com.example.weightdojo.screens.main

import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.repositories.ConfigRepository
import com.example.weightdojo.repositories.ConfigRepositoryImpl
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class Screens {
    LockFirstTime, Lock, Home, Entries, Settings, Charts
}

data class MainState(
    var config: Config? = null,
    var authenticated: Boolean = false,
    var destination: Enum<Screens> = Screens.Home,
    var currentDate: LocalDate,
    var currentDateAsString: String,
)

class MainViewModel(
    private val database: AppDatabase,
    private val repo: ConfigRepository = ConfigRepositoryImpl(database.configDao())
) : ViewModel() {
    private val userTimezone = ZoneId.systemDefault()
    private val userDate = LocalDate.now(userTimezone)
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var state by mutableStateOf(
        MainState(
            currentDate = userDate,
            currentDateAsString = userDate.format(dateFormat)
        )
    )

    private fun formatDate(date: LocalDate): String {
        return date.format(dateFormat)
    }

    fun setDate(date: LocalDate) {
        state = state.copy(currentDate = date, currentDateAsString = formatDate(date))
    }

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