package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DateDao
import com.example.weightdojo.database.dao.RangeDataByDay
import com.example.weightdojo.database.models.Date
import java.time.LocalDate

interface DateRepository {
    fun getData(dates: List<Date>): List<RangeDataByDay>
}

class DateRepositoryImpl(
    private val dateDao: DateDao
) : DateRepository {
    override fun getData(dates: List<Date>): List<RangeDataByDay> {
        val from = dates[0].date
        val to = dates[dates.size - 1].date

        dateDao.insertDateRange(dates)

        val range = dateDao.getRangeByDay(from, to)

        dateDao.deleteAll()

        return range
    }
}