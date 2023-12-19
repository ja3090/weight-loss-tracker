package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.DayWithWeightAndMeals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate

interface DayRepository {
    fun getDay(date: LocalDate): DayWithWeightAndMeals
//    suspend fun insertIntoDay(date: String): DayWithWeightAndMeals
}

class DayRepositoryImpl(
    private val dayDao: DayDao
) : DayRepository {
    override fun getDay(date: LocalDate): DayWithWeightAndMeals {
        val day = dayDao.getDays(date)

        if (day == null) {
            val id = dayDao.insert(date)

            return dayDao.getDays(date) as DayWithWeightAndMeals
        }

        return day

    }
}