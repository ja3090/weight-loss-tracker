package com.example.weightdojo.screens.lockfirsttime

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.repositories.ConfigRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class TestConfigRepo : ConfigRepository {
    override suspend fun getConfig(): Config? {
        TODO("Not yet implemented")
    }

    override suspend fun submitConfig(passcode: String, bioEnabled: Boolean): Boolean {
        return false
    }
}

class LockFirstTimeTest {
    private lateinit var db: AppDatabase
    private var showText: Boolean = false
    private lateinit var context: Context

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        db = Database.buildDb(context, true)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private suspend fun mockOnSubmit() {
        showText = true
    }

    @Test
    @Throws(Exception::class)
    fun mismatchingCodesAreNotSubmitted() {
        val testConfigRepo = TestConfigRepo()
        val viewModel = LockFirstTimeViewModel(
            db,
            testConfigRepo
        )

        composeTestRule.setContent {
            LockFirstTime(
                onSubmitRedirect = ::mockOnSubmit, viewModel = viewModel,
                context = FragmentActivity()
            )
        }

        val firstPass = "1234"
        val secondPass = "1111"

        for (i in firstPass.indices) {
            composeTestRule.onNodeWithText(firstPass[i].toString()).performClick()

            if (i == firstPass.length - 1) {
                composeTestRule.onNodeWithTag("Submit").performClick()
            }
        }

        for (i in secondPass.indices) {
            composeTestRule.onNodeWithText(secondPass[i].toString()).performClick()
        }

        assertTrue(
            "First passcode entered correctly",
            viewModel.state.firstEnter == firstPass
        )
        assertTrue(
            "Second passcode entered correctly",
            viewModel.state.secondEnter == secondPass
        )

        val passes = viewModel.submit()

        assertTrue("Mismatching passcodes are rejected", !passes)
    }
}