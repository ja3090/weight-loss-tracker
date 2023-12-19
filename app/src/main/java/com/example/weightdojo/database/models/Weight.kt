package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "weight")
data class Weight (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "day_id") val dayId: Long,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "unit") val unit: WeightUnit
)

enum class WeightUnit {
    KG, LBS
}