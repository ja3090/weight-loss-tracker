package com.example.weightdojo.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.utils.Hashing
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.security.MessageDigest

class ConfigDaoTest {
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

        val uponCreation = Hashing.generateHashDetails(passcode)

        runBlocking {
            configDao.createConfig(
                passcodeEnabled = true,
                passwordHash = uponCreation.passwordHash,
                salt = uponCreation.salt,
                bioEnabled = false
            )

            val config = configDao.getConfig()

            assertNotNull("Config has been entered", config !== null)
            config as Config

            val onLoggingIn = Hashing.generateHashDetails(passcode, config.salt)

            assertTrue(
                "Password hashes match",
                MessageDigest.isEqual(onLoggingIn.passwordHash, config.passwordHash)
            )
        }
    }
}