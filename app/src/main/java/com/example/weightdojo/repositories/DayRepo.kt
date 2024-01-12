package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import java.time.LocalDate

interface DayRepository {
    fun getDay(date: LocalDate): DayData
    fun getMostRecentWeight(): Float?
}

class DayRepositoryImpl(
    private val dayDao: DayDao
) : DayRepository {
    override fun getDay(date: LocalDate): DayData {
        return dayDao.getDay(date)
    }

    override fun getMostRecentWeight(): Float? {
        return dayDao.getMostRecentWeight()?.weight
    }
}