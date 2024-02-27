package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weightdojo.database.models.Nutriment

@Dao
interface NutrimentDao {

    @Insert
    fun insertNutriment(nutriment: Nutriment): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM nutriment")
    fun _DELETE_ALL()
}