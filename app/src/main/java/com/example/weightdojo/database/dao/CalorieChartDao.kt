package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.datatransferobjects.ChartData
import com.example.weightdojo.datatransferobjects.EarliestDate
import java.time.LocalDate

@Dao
interface CalorieChartDao: ChartDao {

    @Insert
    override fun insertDateRange(dates: List<Date>)

    @Query("DELETE FROM date")
    override fun deleteAll()

    @Query(
        "WITH " +
                "  max_min_weights AS (" +
                "    SELECT" +
                "      MAX(total_calories) as max, " +
                "      MIN(total_calories) as min " +
                "    FROM" +
                "      day " +
                "    WHERE" +
                "      date BETWEEN :from AND :to" +
                "  ) " +
                "SELECT date.date, total_calories as value, max, min FROM date, max_min_weights mw " +
                "LEFT JOIN day ON date.date = day.date " +
                "WHERE date.date BETWEEN :from AND :to "
    )
    override fun getRangeByDay(from: LocalDate, to: LocalDate): List<ChartData>

    @Query(
        "WITH MonthlyAverages AS ( " +
                "        SELECT  " +
                "        AVG(m.total_calories) AS value, " +
                "        d.date " +
                "        FROM " +
                "        date d " +
                "        LEFT JOIN " +
                "        day m ON strftime('%m-%Y', d.date) = strftime('%m-%Y', m.date) " +
                "        GROUP BY " +
                "        strftime('%m-%Y', d.date) " +
                "     ), " +
                "      MinMax as ( " +
                "        SELECT MAX(value) as max, MIN(value) as min FROM MonthlyAverages " +
                "        )  " +
                "        SELECT max, min, date, value FROM MinMax, monthlyaverages"
    )
    override fun getRangeByMonth(): List<ChartData>

    @Query("SELECT MIN(date) as date FROM day ")
    override fun getEarliestWeightDate(): EarliestDate
}