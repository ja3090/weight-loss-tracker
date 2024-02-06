package com.example.weightdojo.screens.lock

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Config
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.PASSCODE_LENGTH
import com.example.weightdojo.utils.Hashing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.security.MessageDigest

data class LockState(
    val passcode: String = "",
    val declineBiometrics: Boolean = false,
    val loading: Boolean = false
)

class LockViewModel(
    private val config: Config,
) : ViewModel() {
    var state by mutableStateOf(LockState())

    fun addInput(button: String) {
        if (state.passcode.length < PASSCODE_LENGTH) {
            state = state.copy(passcode = state.passcode + button)
        }
    }

    fun delete() {
        if (state.passcode.isNotEmpty()) {
            state = state.copy(passcode = state.passcode.dropLast(1))
        }
    }

    fun setIsLoading(boolean: Boolean) {
        state = state.copy(loading = true)
    }

    suspend fun submit(): Boolean {
        if (state.passcode.length < PASSCODE_LENGTH) return false

        setIsLoading(true)

        val salt = config.salt ?: return false

        val passCorrect = viewModelScope.async(Dispatchers.IO) {

            val loginAttempt =
                Hashing.generateHashDetails(passcode = state.passcode, salt = config.salt)

            val isEqual = MessageDigest.isEqual(loginAttempt.passwordHash, config.passwordHash)

            withContext(Dispatchers.Main) {
                setIsLoading(false)
            }

            return@async isEqual
        }

        return passCorrect.await()
    }

    fun declineBiometrics(boolean: Boolean) {
        state = state.copy(declineBiometrics = boolean)
    }
}