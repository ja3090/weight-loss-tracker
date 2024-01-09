package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.DayWithMeals
import java.time.LocalDate

interface DayRepository {
    fun getDay(date: LocalDate): DayWithMeals
    fun getMostRecentWeight(): Float?
}

class DayRepositoryImpl(
    private val dayDao: DayDao
) : DayRepository {
    override fun getDay(date: LocalDate): DayWithMeals {
        return dayDao.getDay(date)
    }

    override fun getMostRecentWeight(): Float? {
        return dayDao.getMostRecentWeight()?.weight
    }
}