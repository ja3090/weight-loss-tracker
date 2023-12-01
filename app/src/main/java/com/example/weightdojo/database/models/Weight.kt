package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "weight")
data class Weight (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "unit") val unit: WeightUnit
)

enum class WeightUnit {
    KG, LBS
}