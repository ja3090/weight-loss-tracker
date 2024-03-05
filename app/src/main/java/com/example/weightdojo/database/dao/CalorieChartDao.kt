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
        "WITH calorieTotals AS ( " +
 "SELECT SUM(meal.total_calories) as value, date FROM meal " +
 "JOIN day ON meal.day_id = day.id "+
 "WHERE date BETWEEN :from AND :to " +
 "GROUP BY date " +
 "), minMax AS ( " +
 " SELECT MAX(value) as max, MIN(value) as min FROM calorieTotals " +
 " )" +
 "SELECT date.date, value, max, min FROM date, minMax mw " +
 "LEFT JOIN calorieTotals ON date.date = calorieTotals.date "+
 "WHERE date.date BETWEEN :from AND :to "
    )
    override fun getRangeByDay(from: LocalDate, to: LocalDate): List<ChartData>

    @Query(
        "WITH dailySums AS ( " +
         "SELECT SUM(meal.total_calories) AS value," +
         " day.date " +
         "FROM meal " +
         "JOIN day ON day.id = meal.day_id" +
         " GROUP BY day.date" +
         "), monthlyAverages AS ( " +
         "SELECT AVG(value) as value, strftime('%m-%Y', dailySums.date) as date FROM dailySums " +
         "GROUP BY strftime('%m-%Y', dailySums.date)" +
         "), minMax AS (SELECT MAX(value) as max, MIN(value) as min FROM monthlyAverages)" +
         "" +
         "SELECT date.date, value, min, max FROM date, minMax " +
         "LEFT JOIN monthlyAverages ON strftime('%m-%Y', monthlyAverages.date) = strftime('%m-%Y', date.date)"
    )
    override fun getRangeByMonth(): List<ChartData>

    @Query("SELECT MIN(date) as date FROM day ")
    override fun getEarliestWeightDate(): EarliestDate
}