package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutriment")
data class Nutriment(
    @PrimaryKey(autoGenerate = true) val nutrimentId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "daily_intake_target") val dailyIntakeTarget: Float? = null,
)
