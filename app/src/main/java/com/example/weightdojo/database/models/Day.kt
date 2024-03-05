package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity(tableName = "day")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "weight") val weight: Float? = null,
    @ColumnInfo(name = "total_calories") val totalCalories: Float? = null,
)