package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.datatransferobjects.RepoResponse
import java.time.LocalDate

interface DayRepository {
    fun getDayData(date: LocalDate): RepoResponse<Pair<DayData, Float?>?>
}

class DayRepositoryImpl(
    private val dayDao: DayDao
) : DayRepository {
    override fun getDayData(date: LocalDate): RepoResponse<Pair<DayData, Float?>?> {
        return try {
            val row = dayDao.getDay(date)
            val mostRecentWeight = row.day.weight ?: dayDao.getMostRecentWeight()?.weight

            RepoResponse(
                success = true,
                data = Pair(row, mostRecentWeight)
            )
        } catch (e: Exception) {
            RepoResponse(
                success = false,
                data = null,
                errorMessage = e.message
            )
        }
    }
}