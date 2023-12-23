package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.DEPRECATED_MESSAGE
import com.example.weightdojo.database.models.Weight
import com.example.weightdojo.utils.WeightUnits
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface WeightDao {
    @Query(
        "SELECT * FROM weight " +
                "WHERE date BETWEEN :from and :to"
    )
    fun getWeightEntries(from: String, to: String): List<Weight>

    @Query(
        "INSERT INTO weight ('day_id', 'weight', 'unit', 'date')" +
                "VALUES (:dayId, :weight, :weightUnit, :date)"
    )
    fun insertWeightEntry(dayId: Long, weight: Float, weightUnit: WeightUnits, date: LocalDate)

    @Deprecated(DEPRECATED_MESSAGE)
    @Query("DELETE FROM weight")
    fun _DELETE_ALL()

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
                "  )" +
                "SELECT " +
                "    mw.maxWeight, " +
                "    mw.minWeight, " +
                "    w.weight, " +
                "    w.unit, " +
                "    w.date " +
                "FROM " +
                "    weight w, " +
                "    max_min_weights mw " +
                "WHERE " +
                "    w.date BETWEEN :from AND :to " +
                "GROUP BY " +
                "    w.date"
    )
    fun getChartData(from: LocalDate, to: LocalDate): List<ChartDataDTO>

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
                "  )" +
                "SELECT  " +
                "mw.maxWeight, " +
                "mw.minWeight, " +
                "    strftime('%Y-%m', date) AS month, " +
                "    date, " +
                "    AVG(weight) AS weight " +
                "FROM  " +
                "max_min_weights mw, " +
                "    weight " +
                "WHERE date BETWEEN :from AND :to " +
                "GROUP BY  " +
                "    month"
    )
    fun getChartDataOverLongPeriod(from: LocalDate, to: LocalDate): List<ChartDataDTO>
}

class ChartDataDTO(
    val maxWeight: Float,
    val minWeight: Float,
    val weight: Float,
    val date: LocalDate
)