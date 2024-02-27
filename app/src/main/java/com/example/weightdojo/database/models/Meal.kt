package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.utils.CalorieUnits
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "total_calories") val totalCalories: Float = 0f,
    @ColumnInfo(name = "day_id") val dayId: Long? = null,
    @ColumnInfo(name = "is_template", defaultValue = "0") val isTemplate: Boolean = false
)