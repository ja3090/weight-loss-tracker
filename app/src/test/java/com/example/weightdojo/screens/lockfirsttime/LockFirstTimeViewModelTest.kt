package com.example.weightdojo.screens.lockfirsttime

import android.content.Context
import android.content.SharedPreferences
import com.example.weightdojo.PASSCODE_LENGTH
import com.example.weightdojo.utils.ConfigSessionCache
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test


class LockFirstTimeViewModelTest {
    private lateinit var viewModel: LockFirstTimeViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val firstPass = "1234"
    private val secondPass = "4572"

    @Before
    fun init() {
        sharedPreferences = mockk<SharedPreferences>()
        viewModel = LockFirstTimeViewModel(
            ConfigSessionCache(sharedPreferences)
        )
    }

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

        viewModel.delete()

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

        viewModel.delete()

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
            viewModel.addInput(passcode[i].toString())

            if (i == firstPass.length - 1 && submit) {
                viewModel.submit()
            }
        }
    }

    private fun deleteAllCharactersWithExtraClicks() {
        viewModel.delete()
        viewModel.delete()
        viewModel.delete()
        viewModel.delete()
        viewModel.delete()
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

        viewModel.submit()

        enterAndSubmitCode(secondPass)

        assertTrue("secondPassMatches", viewModel.state.secondEnter == secondPass)
    }
}