package com.example.weightdojo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "config")
data class Config (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "weight_unit", defaultValue = "KG") val weightUnit: WeightUnit,
    @ColumnInfo(name = "calorie_unit", defaultValue = "KCAL") val calorieUnit: CalorieUnit,
    @ColumnInfo(name = "goal_weight") val goalWeight: Float? = null,
    @ColumnInfo(name = "passcode_enabled") val passcodeEnabled: Boolean,
    @ColumnInfo(name = "password_hash") val passwordHash: ByteArray,
    @ColumnInfo(name = "salt") val salt: ByteArray,
    @ColumnInfo(name = "bio_enabled") val bioEnabled: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ByteArray) return false
        return this.passwordHash.contentEquals(other)
    }

    override fun hashCode(): Int {
        var result = passwordHash.contentHashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

enum class CalorieUnit {
    KCAL, KJ
}