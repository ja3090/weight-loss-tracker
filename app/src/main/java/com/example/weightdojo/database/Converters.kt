package com.example.weightdojo.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.weightdojo.database.models.CalorieUnit
import com.example.weightdojo.database.models.WeightUnit
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun toWeightUnit(unit: String) = enumValueOf<WeightUnit>(unit)

    @TypeConverter
    fun fromWeightUnit(unit: WeightUnit) = unit.name

    @TypeConverter
    fun toCalorieUnit(unit: String) = enumValueOf<CalorieUnit>(unit)

    @TypeConverter
    fun fromCalorieUnit(unit: CalorieUnit) = unit.name

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(dateFormat)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(date: String): LocalDate {
        return date.let { LocalDate.parse(it) }
    }
}