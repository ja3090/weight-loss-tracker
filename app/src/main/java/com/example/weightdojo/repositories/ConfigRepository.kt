package com.example.weightdojo.repositories

import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.utils.Hashing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface ConfigRepository {
    suspend fun submitConfig(passcode: String): Boolean
    suspend fun getConfig(): Config?
}

class ConfigRepositoryImpl(
    private val configDao: ConfigDao
) : ConfigRepository {
    override suspend fun submitConfig(passcode: String): Boolean {
        val success = CoroutineScope(Dispatchers.IO).async {
            try {

                val hashDetails = Hashing.generateHashDetails(passcode)

                configDao.createConfig(
                    passcodeEnabled = true,
                    passwordHash = hashDetails.passwordHash,
                    salt = hashDetails.salt
                )

                return@async true
            } catch (e: Exception) {
                return@async false
            }
        }

        return success.await()
    }

    override suspend fun getConfig(): Config? {
        return configDao.getConfig()
    }
}