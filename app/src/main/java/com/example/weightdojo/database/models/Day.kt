package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity("day")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "date") val date: LocalDate,
)

data class DayWithWeightAndMeals(
    @Embedded val day: Day,
    @Relation(
        parentColumn = "id",
        entityColumn = "day_id"
    )
    val meals: List<Meal>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "day_id"
    )
    val weight: Weight?
)