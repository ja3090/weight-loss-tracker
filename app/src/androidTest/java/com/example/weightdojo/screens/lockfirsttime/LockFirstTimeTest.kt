package com.example.weightdojo.screens.lockfirsttime

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ApplicationProvider
import com.example.weightdojo.PASSCODE_LENGTH
import com.example.weightdojo.TestTags
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.Database
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.utils.SessionCache
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class TestConfigSession : SessionCache<Config> {
    override fun saveSession(session: Config?) {
        TODO("Not yet implemented")
    }

    override fun getActiveSession(): Config? {
        TODO("Not yet implemented")
    }

    override fun clearSession() {
        TODO("Not yet implemented")
    }

}

class LockFirstTimeTest {
    private lateinit var db: AppDatabase
    private lateinit var context: Context
    private lateinit var viewModel: LockFirstTimeViewModel
    private val firstPass = "1234"
    private val secondPass = "4572"

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        db = Database.buildDb(context, true)
        viewModel = LockFirstTimeViewModel(
            configSessionCache = TestConfigSession(),
        )

        composeTestRule.setContent {
            LockFirstTime(
                onSubmitRedirect = ::mockOnSubmit,
                lockFirstTimeVM = viewModel,
                context = FragmentActivity()
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private fun mockOnSubmit() {}

    @Test
    @Throws(Exception::class)
    fun mismatchingCodesAreNotSubmitted() {
        enterAndSubmitCode(firstPass, true)
        enterAndSubmitCode(secondPass)

        val passes = viewModel.submit()

        assertTrue("Mismatching passcodes are rejected", !passes)
    }

    @Test
    @Throws(Exception::class)
    fun shortPassRejected() {
        val firstPassOneLess = firstPass.slice(0..firstPass.length - 2)
        enterAndSubmitCode(firstPassOneLess, true)

        viewModel.submit()

        assertTrue("Still entering", viewModel.state.enteringPasscode)
        assertTrue("Not confirming", !viewModel.state.confirmingPasscode)

        deleteAllCharactersWithExtraClicks()

        enterAndSubmitCode(firstPass, true)

        viewModel.submit()

        assertTrue("Not entering", !viewModel.state.enteringPasscode)
        assertTrue("Is confirming", viewModel.state.confirmingPasscode)

        enterAndSubmitCode(firstPassOneLess)

        val passes = viewModel.submit()

        assertTrue("Should not pass", !passes)
    }

    @Test
    @Throws(Exception::class)
    fun deleteFunction() {
        enterAndSubmitCode(firstPass)

        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()

        val oneLessChar = firstPass.slice(0..firstPass.length - 2)

        assertTrue(
            "Deleting last character works for first enter",
            oneLessChar == viewModel.state.firstEnter
        )

        deleteAllCharactersWithExtraClicks()

        assertTrue(
            "First enter string should be empty",
            viewModel.state.firstEnter == ""
        )

        enterAndSubmitCode(firstPass, true)
        enterAndSubmitCode(secondPass)

        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()

        val oneLessCharSecond = secondPass.slice(0..secondPass.length - 2)

        assertTrue(
            "Deleting last character works for second enter",
            oneLessCharSecond == viewModel.state.secondEnter
        )

        deleteAllCharactersWithExtraClicks()

        assertTrue(
            "Second enter string should be empty",
            viewModel.state.secondEnter == ""
        )
    }

    private fun enterAndSubmitCode(passcode: String, submit: Boolean = false) {
        for (i in passcode.indices) {
            composeTestRule.onNodeWithText(passcode[i].toString()).performClick()

            if (i == firstPass.length - 1 && submit) {
                composeTestRule.onNodeWithTag("Submit").performClick()
            }
        }
    }

    private fun deleteAllCharactersWithExtraClicks() {
        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()
        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()
        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()
        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()
        composeTestRule.onNodeWithTag(TestTags.DELETE_BUTTON.name).performClick()
    }

    @Test
    @Throws(Exception::class)
    fun addInputWorks() {
        enterAndSubmitCode(firstPass)
        enterAndSubmitCode(firstPass)

        assertTrue(
            "Password length doesn't exceed max length",
            viewModel.state.firstEnter.length == PASSCODE_LENGTH
        )

        assertTrue("firstPassMatches", viewModel.state.firstEnter == firstPass)

        composeTestRule.onNodeWithTag("Submit").performClick()

        enterAndSubmitCode(secondPass)

        assertTrue("secondPassMatches", viewModel.state.secondEnter == secondPass)
    }
}