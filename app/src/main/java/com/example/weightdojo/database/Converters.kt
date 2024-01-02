package com.example.weightdojo.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.WeightUnits
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun toWeightUnit(unit: String) = enumValueOf<WeightUnits>(unit)

    @TypeConverter
    fun fromWeightUnit(unit: WeightUnits) = unit.name

    @TypeConverter
    fun toCalorieUnit(unit: String) = enumValueOf<CalorieUnits>(unit)

    @TypeConverter
    fun fromCalorieUnit(unit: CalorieUnits) = unit.name
    @TypeConverter
    fun fromSex(sex: Sex) = sex.name
    @TypeConverter
    fun toSex(sex: String) = enumValueOf<Sex>(sex)

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