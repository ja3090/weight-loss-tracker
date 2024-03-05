package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.Date
import com.example.weightdojo.datatransferobjects.ChartData
import com.example.weightdojo.datatransferobjects.EarliestDate
import java.time.LocalDate

@Dao
interface WeightChartDao: ChartDao {

    @Insert
    override fun insertDateRange(dates: List<Date>)

    @Query("DELETE FROM date")
    override fun deleteAll()

    @Query(
        "WITH " +
                "  max_min_weights AS (" +
                "    SELECT" +
                "      MAX(weight) as max, " +
                "      MIN(weight) as min " +
                "    FROM" +
                "      day" +
                "    WHERE" +
                "      date BETWEEN :from AND :to" +
                "  ) " +
                "SELECT date.date, weight as value, max, min FROM date, max_min_weights mw " +
                "LEFT JOIN day ON date.date = day.date " +
                "WHERE date.date BETWEEN :from AND :to "
    )
    override fun getRangeByDay(from: LocalDate, to: LocalDate): List<ChartData>

    @Query(
        "WITH MonthlyAverages AS ( " +
                "        SELECT  " +
                "        AVG(day.weight) AS value, " +
                "        day.date " +
                "        FROM " +
                "        day  " +
                "        GROUP BY " +
                "        strftime('%m-%Y', day.date) " +
                "     ), " +
                "      MinMax as ( " +
                "        SELECT MAX(value) as max, MIN(value) as min FROM MonthlyAverages " +
                "        )  " +
                "        SELECT max, min, date.date, value FROM MinMax, date " +
                "LEFT JOIN MonthlyAverages ON strftime('%m-%Y', monthlyaverages.date) = strftime('%m-%Y', date.date)"
    )
    override fun getRangeByMonth(): List<ChartData>

    @Query("SELECT MIN(date) as date FROM day")
    override fun getEarliestWeightDate(): EarliestDate
}