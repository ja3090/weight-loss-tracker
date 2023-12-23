package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.database.dao.ChartDataDTO
import com.example.weightdojo.database.dao.WeightDao
import java.time.LocalDate

interface ChartRepo {
    fun getChartData(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ChartDataDTO>

    fun getLongChartData(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ChartDataDTO>
}

class ChartRepoImpl(
    val weightDao: WeightDao,
) : ChartRepo {


    override fun getChartData(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ChartDataDTO> {
        val data = weightDao.getChartData(endDate, startDate)

        Log.d("startDate", startDate.toString())
        Log.d("endDate", endDate.toString())

        return data
    }

    override fun getLongChartData(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ChartDataDTO> {
        val data = weightDao.getChartDataOverLongPeriod(endDate, startDate)

        Log.d("startDate", startDate.toString())
        Log.d("endDate", endDate.toString())

        return data
    }
}