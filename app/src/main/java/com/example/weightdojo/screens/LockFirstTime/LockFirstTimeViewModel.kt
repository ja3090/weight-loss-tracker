package com.example.weightdojo.screens.LockFirstTime

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weightdojo.database.Database
import com.example.weightdojo.utils.Hashing
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

data class LockFirstTimeState(
    var firstEnter: String = "",
    var secondEnter: String = "",
    var enteringPasscode: Boolean = true,
    var confirmingPasscode: Boolean = false,
    var loading: Boolean = false
)

class LockFirstTimeViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var state by mutableStateOf(LockFirstTimeState())

    fun addInput(text: String) {
        val enterFirst = state.enteringPasscode && state.firstEnter.length != 4

        if (enterFirst) state = state.copy(firstEnter = state.firstEnter + text)

        val enterSecond = state.confirmingPasscode && state.secondEnter.length != 4

        if (enterSecond) state = state.copy(secondEnter = state.secondEnter + text)
    }

    fun delete() {
        val enterFirst = state.enteringPasscode && state.firstEnter.isNotEmpty()

        if (enterFirst) state = state.copy(firstEnter = state.firstEnter.dropLast(1))

        val enterSecond = state.confirmingPasscode && state.secondEnter.isNotEmpty()

        if (enterSecond) state = state.copy(secondEnter = state.secondEnter.dropLast(1))
    }

    suspend fun submit(): Boolean {
        val enterFinished = state.enteringPasscode && state.firstEnter.length == 4

        if (enterFinished) {
            state = state.copy(enteringPasscode = false, confirmingPasscode = true)
        }

        val confirmFinished = state.confirmingPasscode && state.secondEnter.length == 4

        val matches = state.firstEnter == state.secondEnter

        if (confirmFinished && matches) {
            return submitConfig()
        }

        return false
    }

    fun goBack() {
        state = state.copy(confirmingPasscode = false, enteringPasscode = true, secondEnter = "")
    }

    private suspend fun submitConfig(): Boolean {
        state = state.copy(loading = true)

        val success = CoroutineScope(Dispatchers.IO).async {
            try {

                val hashDetails = Hashing.generateHashDetails(state.secondEnter)

                database.getDb().configDao().createConfig(
                    passcodeEnabled = true,
                    passwordHash = hashDetails.hash,
                    salt = hashDetails.salt
                )

                return@async true
            } catch (e: Exception) {
                return@async false
            }
        }

        withContext(Dispatchers.Main) {
            state = state.copy(loading = false)
        }

        return success.await()
    }
}