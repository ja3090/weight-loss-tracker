package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.database.models.DayWithMeals
import java.time.LocalDate

interface DayRepository {
    fun getDay(date: LocalDate): DayWithMeals

    //    suspend fun insertIntoDay(date: String): DayWithMeals
    fun getMostRecentDay(): Float?
}

class DayRepositoryImpl(
    private val dayDao: DayDao
) : DayRepository {
    override fun getDay(date: LocalDate): DayWithMeals {
        val day = dayDao.getDays(date)

        if (day == null) {
            dayDao.insert(date)

            return dayDao.getDays(date) as DayWithMeals
        }

        return day
    }

    override fun getMostRecentDay(): Float? {
        return dayDao.getMostRecentWeight()?.weight
    }
}