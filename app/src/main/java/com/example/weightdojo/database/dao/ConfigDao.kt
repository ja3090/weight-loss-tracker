package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Sex

@Dao
interface ConfigDao {
    @Query(
        "SELECT * FROM config " +
                "ORDER BY id " +
                "LIMIT 1"
    )
    fun getConfig(): Config?

    @Query(
        "INSERT INTO config " +
                "('passcode_enabled', 'password_hash', 'salt', 'bio_enabled') " +
                "VALUES (:passcodeEnabled, :passwordHash, :salt, :bioEnabled)"
    )
    suspend fun createConfig(
        passcodeEnabled: Boolean,
        passwordHash: ByteArray,
        salt: ByteArray,
        bioEnabled: Boolean
    )

    @Query(
        "UPDATE config " +
        "SET age = :age, height = :height, sex = :sex " +
        "WHERE id = :id "
    )
    fun setConfigExtraOptions(age: Int?, height: Float?, sex: Sex?, id: Long)
}