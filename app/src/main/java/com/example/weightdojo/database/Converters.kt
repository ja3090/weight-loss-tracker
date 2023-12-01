package com.example.weightdojo.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.weightdojo.database.models.CalorieUnit
import com.example.weightdojo.database.models.WeightUnit
import java.time.LocalDateTime

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
    fun fromDate(date: LocalDateTime): String {
        return date.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(date: String): LocalDateTime {
        return date.let { LocalDateTime.parse(it) }
    }
}