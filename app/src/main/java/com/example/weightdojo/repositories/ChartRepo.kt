package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.CalorieChartDao
import com.example.weightdojo.database.dao.ChartDao
import com.example.weightdojo.database.dao.WeightChartDao
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.datatransferobjects.ChartData
import java.time.LocalDate

interface ChartRepository {
    fun getData(dates: List<Date>): List<ChartData>
    fun getDataByMonth(dates: List<Date>): List<ChartData>
    fun getEarliestDate(): LocalDate
}

open class ChartRepoImpl(
    open val chartDao: ChartDao
) : ChartRepository {
    override fun getData(dates: List<Date>): List<ChartData> {
        val from = dates[0].date
        val to = dates[dates.size - 1].date

        chartDao.insertDateRange(dates)

        val range = chartDao.getRangeByDay(from, to)

        chartDao.deleteAll()

        return range
    }
    override fun getEarliestDate(): LocalDate {
        val row = chartDao.getEarliestWeightDate()

        return row.date ?: LocalDate.now().minusDays(6L)
    }
    override fun getDataByMonth(dates: List<Date>): List<ChartData> {
        chartDao.insertDateRange(dates)

        val range = chartDao.getRangeByMonth()

        chartDao.deleteAll()

        return range
    }
}

class WeightDateRepoImpl(
    override val chartDao: WeightChartDao
) : ChartRepoImpl(chartDao)

class CalorieDateRepoImpl(
    override val chartDao: CalorieChartDao
) : ChartRepoImpl(chartDao)