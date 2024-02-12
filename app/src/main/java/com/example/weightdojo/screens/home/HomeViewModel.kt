package com.example.weightdojo.screens.home

import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.repositories.DayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


data class HomeState(
    var dayData: DayData? = null,
    var showAddModal: Boolean = false,
    var currentDate: LocalDate = MyApp.appModule.currentDate,
    var mostRecentWeight: Float? = null
)

class HomeViewModel(
    private val database: AppDatabase,
    private val repo: DayRepositoryImpl = DayRepositoryImpl(database.dayDao()),
) : ViewModel() {

    var state by mutableStateOf(
        HomeState()
    )

    init {
        getAndSetDay()
    }

    fun getDayData(): DayData? {
        return state.dayData
    }

    fun getDayId(): Long? {
        return state.dayData?.day?.id
    }

    fun showModal(show: Boolean) {
        state = state.copy(showAddModal = show)
    }

    fun getAndSetDay(date: LocalDate? = null) {
        val dateToUse = if (date !== null) date else state.currentDate

        viewModelScope.launch(Dispatchers.IO) {
            val (dayData, mostRecentWeight) = async {
                try {
                    val row = repo.getDay(dateToUse)

                    return@async Pair(
                        row,
                        row.day.weight ?: repo.getMostRecentWeight()
                    )
                } catch (e: Exception) {
                    throw Error(e)
                }
            }.await()

            withContext(Dispatchers.Main) {
                state = state.copy(
                    dayData = dayData,
                    currentDate = dateToUse,
                    mostRecentWeight = mostRecentWeight
                )
            }
        }
    }

    fun refresh() {
        getAndSetDay(state.currentDate)
    }
}