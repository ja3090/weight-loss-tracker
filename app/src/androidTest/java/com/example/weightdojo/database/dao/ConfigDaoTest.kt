package com.example.weightdojo.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.utils.Hashing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ConfigRepoTest {
    private lateinit var configDao: ConfigDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Database.buildDb(context, true)
        configDao = db.configDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun passcodeEntersCorrectly() {
        val passcode = "1234"

        val details = Hashing.generateHashDetails(passcode)

        runBlocking {
            configDao.createConfig(
                passcodeEnabled = true,
                passwordHash = details.hash,
                salt = details.salt
            )

            val config = configDao.getConfig()

            assert(config !== null)
        }
    }
}