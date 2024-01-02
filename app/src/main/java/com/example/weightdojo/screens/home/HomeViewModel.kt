package com.example.weightdojo.screens.home

import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.models.DayWithWeightAndMeals
import com.example.weightdojo.repositories.DayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


data class HomeState(
    var day: DayWithWeightAndMeals? = null,
    var showAddModal: Boolean = false
)

class HomeViewModel(
    private val database: AppDatabase,
    private val currentDate: LocalDate,
    private val repo: DayRepositoryImpl = DayRepositoryImpl(database.dayDao()),
) : ViewModel() {

    var state by mutableStateOf(
        HomeState()
    )

    init {
        getAndSetDay()
    }

    fun showModal(show: Boolean) {
        state = state.copy(showAddModal = show)
    }

    fun getAndSetDay(date: LocalDate? = null) {
        val dateToUse = if (date !== null) date else currentDate

        viewModelScope.launch(Dispatchers.IO) {
            val day = async {
                try {
                    return@async repo.getDay(dateToUse)
                } catch(e: Exception) {
                    throw Error(e)
                }
            }

            withContext(Dispatchers.Main) {
                state = state.copy(day = day.await())
            }
        }
    }
}