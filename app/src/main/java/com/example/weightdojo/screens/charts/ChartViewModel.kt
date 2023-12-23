package com.example.weightdojo.screens.charts

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.RangeDataByDay
import com.example.weightdojo.repositories.DateRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.*
import com.example.weightdojo.database.models.Date
import kotlinx.coroutines.withContext

data class ChartState(
    val data: List<RangeDataByDay>? = null
)

enum class TimePeriods { ONE_WEEK, ONE_MONTH, }

class ChartViewModel(
    val database: AppDatabase = MyApp.appModule.database,
    val dateRepo: DateRepositoryImpl = DateRepositoryImpl(database.dateDao()),
    val timePeriods: TimePeriods = TimePeriods.ONE_MONTH
) : ViewModel() {

    var chartState by mutableStateOf(ChartState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getTimeRangeData(timePeriods)

                withContext(Dispatchers.Main) {
                    chartState = chartState.copy(data = data)
                }

            } catch (e: Exception) {
                throw Error(e)
            }
        }
    }

    fun getTimeRangeData(timePeriods: TimePeriods): List<RangeDataByDay> {
        val endDate = LocalDate.now()

        when (timePeriods) {
            TimePeriods.ONE_WEEK -> {
                val dateRange = getRangeParams(
                    startDate = endDate.minusDays(6L),
                    increment = { date -> date.plusDays(1L) })

                return dateRepo.getData(dateRange)
            }

            TimePeriods.ONE_MONTH -> {
                val dateRange = getRangeParams(
                    startDate = endDate.minusDays(28L),
                    increment = { date -> date.plusDays(1L) })

                return dateRepo.getData(dateRange)
            }
        }
    }

    fun getRangeParams(
        startDate: LocalDate,
        endDate: LocalDate = LocalDate.now(),
        increment: (date: LocalDate) -> LocalDate
    ): List<Date> {
        var currentDate = startDate
        var comparison = currentDate.compareTo(endDate)
        val dateList = mutableListOf<Date>()

        while (comparison <= 0) {
            dateList.add(Date(date = currentDate))
            currentDate = increment(currentDate)
            comparison = currentDate.compareTo(endDate)
        }

        return dateList
    }
}