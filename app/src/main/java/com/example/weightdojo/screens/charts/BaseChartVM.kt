package com.example.weightdojo.screens.charts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.repositories.WeightDateRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.*
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.datatransferobjects.ChartData
import com.example.weightdojo.repositories.CalorieDateRepoImpl
import com.example.weightdojo.repositories.ChartRepository
import kotlinx.coroutines.withContext
import java.time.Period

data class ChartState(
    val data: List<ChartData>? = null,
    val timePeriod: TimePeriods
)

enum class TimePeriods {
    ONE_WEEK {
        override fun toString(): String {
            return "1W"
        }
    },
    ONE_MONTH {
        override fun toString(): String {
            return "1M"
        }
    },
    SIX_MONTHS {
        override fun toString(): String {
            return "6M"
        }
    },
    ALL {
        override fun toString(): String {
            return "All"
        }
    }
}

abstract class BaseChartVM(
    open val database: AppDatabase,
    open val dateRepo: ChartRepository,
    private val timePeriod: TimePeriods = TimePeriods.ONE_WEEK
) : ViewModel() {

    var chartState by mutableStateOf(ChartState(timePeriod = timePeriod))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAndSetData(timePeriod)

            } catch (e: Exception) {
                throw Error(e)
            }
        }
    }

    suspend fun getAndSetData(timePeriod: TimePeriods) {
        val data = getTimeRangeData(timePeriod)

        withContext(Dispatchers.Main) {
            chartState = chartState.copy(data = data, timePeriod = timePeriod)
        }
    }

    private fun getTimeRangeData(timePeriods: TimePeriods): List<ChartData> {
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
                    startDate = endDate.minusDays(27L),
                    increment = { date -> date.plusDays(1L) })

                return dateRepo.getData(dateRange)
            }

            TimePeriods.SIX_MONTHS -> {
                val dateRange = getRangeParams(
                    startDate = endDate.minusMonths(6L),
                    increment = { date -> date.plusMonths(1L) })

                return dateRepo.getDataByMonth(dateRange)
            }

            TimePeriods.ALL -> {
                val earliestDate = dateRepo.getEarliestDate()

                val dateDifference = Period.between(earliestDate, LocalDate.now())

                val largerDifference = dateDifference.months > 2 || dateDifference.years > 0

                val increment: (date: LocalDate) -> LocalDate =
                    if (largerDifference) {
                        { date -> date.plusMonths(1L) }
                    } else {
                        { date -> date.plusDays(1L) }
                    }

                val dateRange = getRangeParams(
                    startDate = earliestDate,
                    increment = increment
                )

                return if (largerDifference) {
                    dateRepo.getDataByMonth(dateRange)
                } else {
                    dateRepo.getData(dateRange)
                }
            }
        }
    }

    private fun getRangeParams(
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

class WeightChartViewModel(
    override val database: AppDatabase,
    override val dateRepo: ChartRepository = WeightDateRepoImpl(database.weightChartDao())
) : BaseChartVM(database = database, dateRepo = dateRepo)

class CalorieChartViewModel(
    override val database: AppDatabase,
    override val dateRepo: ChartRepository = CalorieDateRepoImpl(database.calorieChartDao())
) : BaseChartVM(database = database, dateRepo = dateRepo)