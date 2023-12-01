package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weightdojo.database.models.Config

@Dao
interface ConfigDao {
    @Query("SELECT * FROM config " +
    "ORDER BY id " +
    "LIMIT 1")
    suspend fun getConfig(): Config?

    @Query("INSERT INTO config " +
    "('passcode_enabled', 'password_hash', 'salt') " +
    "VALUES (:passcodeEnabled, :passwordHash, :salt)")
    suspend fun createConfig(passcodeEnabled: Boolean, passwordHash: ByteArray, salt: ByteArray)
}