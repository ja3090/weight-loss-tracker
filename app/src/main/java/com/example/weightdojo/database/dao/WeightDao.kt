package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weightdojo.database.models.Weight

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight " +
    "WHERE date BETWEEN :from and :to")
    fun getWeightEntries(from: String, to: String): List<Weight>
}