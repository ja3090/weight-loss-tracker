package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity("day")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "weight") val weight: Float? = null,
    @ColumnInfo(name = "total_carbohydrates") val totalCarbohydrates: Float? = null,
    @ColumnInfo(name = "total_protein") val totalProtein: Float? = null,
    @ColumnInfo(name = "total_fat") val totalFat: Float? = null,
    @ColumnInfo(name = "total_calories") val totalCalories: Float? = null,
)