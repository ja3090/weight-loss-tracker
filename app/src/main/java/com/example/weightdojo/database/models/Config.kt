package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.WeightUnits


data class Config(
    val weightUnit: WeightUnits = WeightUnits.KG,
    val calorieUnit: CalorieUnits = CalorieUnits.KCAL,
    val goalWeight: Float? = null,
    val passcodeEnabled: Boolean,
    val passwordHash: ByteArray? = null,
    val salt: ByteArray? = null,
    val bioEnabled: Boolean,
    val age: Int? = null,
    val sex: Sex? = null,
    val height: Float? = null,
    val showExtraOptions: Boolean = true
) {
//    override fun equals(other: Any?): Boolean {
//        if (other !is ByteArray) return false
//        return this.passwordHash.contentEquals(other)
//    }
//
//    override fun hashCode(): Int {
//        var result = passwordHash.contentHashCode()
//        result = 31 * result + salt.contentHashCode()
//        return result
//    }
}

enum class Sex {
    Male, Female
}