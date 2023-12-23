package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.Date
import java.time.LocalDate

@Dao
interface DateDao {

    @Insert
    fun insertDateRange(dates: List<Date>)

    @Query("DELETE FROM date")
    fun deleteAll()

    @Query(
        "WITH " +
                "  max_min_weights AS (" +
                "    SELECT" +
                "      MAX(weight) as maxWeight," +
                "      MIN(weight) as minWeight" +
                "    FROM" +
                "      weight" +
                "    WHERE" +
                "      date BETWEEN :from AND :to" +
                "  ) " +
        "SELECT date.date, weight, mw.maxWeight, minWeight FROM date, max_min_weights mw " +
                "LEFT JOIN weight ON date.date = weight.date " +
                "WHERE date.date BETWEEN :from AND :to "
    )
    fun getRangeByDay(from: LocalDate, to: LocalDate): List<RangeDataByDay>
}

data class RangeDataByDay(
    val maxWeight: Float?,
    val minWeight: Float?,
    val date: LocalDate,
    val weight: Float?,
)