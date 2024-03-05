package com.example.weightdojo.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.weightdojo.database.AppDatabase
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.repositories.DayRepositoryImpl
import com.example.weightdojo.repositories.mealrepo.MealRepository
import com.example.weightdojo.repositories.mealrepo.MealRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val mealRepository: MealRepository = MealRepositoryImpl(database),
) : ViewModel() {
    var state by mutableStateOf(
        HomeState()
    )

    suspend fun submitEdit(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?> {
        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.updateMeal(singleMealDetailed)
        }

        return job.await()
    }

    suspend fun submitMeal(singleMealDetailed: SingleMealDetailed): RepoResponse<Unit?> {
        val job = viewModelScope.async(Dispatchers.IO) {
            mealRepository.enterMeal(singleMealDetailed, getDayId())
        }

        return job.await()
    }

    init {
        getAndSetDay()
    }

    fun getDayData(): DayData? {
        return state.dayData
    }

    fun getDayId(): Long {
        return state.dayData?.day?.id ?: 0
    }

    fun showModal(show: Boolean) {
        state = state.copy(showAddModal = show)
    }

    fun getAndSetDay(date: LocalDate? = null) {
        val dateToUse = if (date !== null) date else state.currentDate

        viewModelScope.launch {
            val res = viewModelScope.async(Dispatchers.IO) {
                repo.getDayData(dateToUse)
            }.await()

            state = state.copy(
                dayData = res.data?.first,
                currentDate = dateToUse,
                mostRecentWeight = res.data?.second
            )
        }
    }

    fun refresh() {
        getAndSetDay(state.currentDate)
    }
}