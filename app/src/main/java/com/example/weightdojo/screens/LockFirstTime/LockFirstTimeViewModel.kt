package com.example.weightdojo.screens.lockfirsttime

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.repositories.ConfigRepository
import com.example.weightdojo.repositories.ConfigRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

data class LockFirstTimeState(
    var firstEnter: String = "",
    var secondEnter: String = "",
    var enteringPasscode: Boolean = true,
    var confirmingPasscode: Boolean = false,
    var loading: Boolean = false,
    var showBiometricDialog: Boolean = false
)

open class LockFirstTimeViewModel(
    private val database: AppDatabase,
    private val repo: ConfigRepository = ConfigRepositoryImpl(database.configDao())
) : ViewModel() {
    var state by mutableStateOf(LockFirstTimeState())

    val passcodeLength = 4

    fun setShowBiometricDialog(bool: Boolean) {
        state = state.copy(showBiometricDialog = bool)
    }

    fun addInput(text: String) {
        val enterFirst = state.enteringPasscode && state.firstEnter.length != passcodeLength

        if (enterFirst) state = state.copy(firstEnter = state.firstEnter + text)

        val enterSecond = state.confirmingPasscode && state.secondEnter.length != passcodeLength

        if (enterSecond) state = state.copy(secondEnter = state.secondEnter + text)
    }

    fun delete() {
        val enterFirst = state.enteringPasscode && state.firstEnter.isNotEmpty()

        if (enterFirst) state = state.copy(firstEnter = state.firstEnter.dropLast(1))

        val enterSecond = state.confirmingPasscode && state.secondEnter.isNotEmpty()

        if (enterSecond) state = state.copy(secondEnter = state.secondEnter.dropLast(1))
    }

    fun submit(): Boolean {
        val enterFinished = state.enteringPasscode && state.firstEnter.length == passcodeLength

        if (enterFinished) {
            state = state.copy(enteringPasscode = false, confirmingPasscode = true)
        }

        val confirmFinished = state.confirmingPasscode && state.secondEnter.length == passcodeLength

        val matches = state.firstEnter == state.secondEnter

        Log.d("confirmFinished", confirmFinished.toString())
        Log.d("matches", matches.toString())

        return confirmFinished && matches
    }

    fun goBack() {
        state = state.copy(confirmingPasscode = false, enteringPasscode = true, secondEnter = "")
    }

     suspend fun submitConfig(bioEnabled: Boolean): Boolean {
        withContext(Dispatchers.Main) {
            state = state.copy(loading = true)
        }

        val success = viewModelScope.async(Dispatchers.IO) {
            return@async repo.submitConfig(state.secondEnter, bioEnabled)
        }.await()

        withContext(Dispatchers.Main) {
            state = state.copy(loading = true)
        }

        return success
    }
}