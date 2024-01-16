package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float? = null,
    @ColumnInfo(name = "total_protein") val totalProtein: Float? = null,
    @ColumnInfo(name = "total_fat") val totalFat: Float? = null,
    @ColumnInfo(name = "total_calories") val totalCalories: Float? = null,
    @ColumnInfo(name = "day_id") val dayId: Long,
)