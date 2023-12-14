package com.example.weightdojo.screens.lockfirsttime

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.Loading
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.components.keypad.KeypadButton
import com.example.weightdojo.utils.Biometrics
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Composable
fun LockFirstTime(
    onSubmitRedirect: () -> Unit,
    lockFirstTimeVM: LockFirstTimeViewModel = viewModel(
        factory = VMFactory.build {
            LockFirstTimeViewModel(MyApp.appModule.database)
        }
    ),
    context: FragmentActivity,
) {
    val state = lockFirstTimeVM.state
    fun getInputValue(): String {
        return if (state.enteringPasscode) state.firstEnter
        else state.secondEnter
    }

    fun getPromptText(): String {
        return if (state.enteringPasscode) "Create a 4-digit passcode"
        else "Confirm your passcode"
    }

    suspend fun submit(optInBio: Boolean) {
        lockFirstTimeVM.viewModelScope.launch {

            val submitted = async { lockFirstTimeVM.submitConfig(optInBio) }

            if (submitted.await()) onSubmitRedirect()
        }
    }

    fun onSubmit() {
        if (state.loading) return

        lockFirstTimeVM.viewModelScope.launch {
            val ok = lockFirstTimeVM.submit()

            if (ok) {
                val canAuth = Biometrics.canUseBiometrics(context)

                if (canAuth) lockFirstTimeVM.setShowBiometricDialog(true)
                else submit(false)
            }
        }
    }

    Keypad(
        keyClick = lockFirstTimeVM::addInput,
        delete = lockFirstTimeVM::delete,
        submit = ::onSubmit,
        inputValue = getInputValue(),
        promptText = getPromptText(),
        leftOfZeroCustomBtn = if (state.confirmingPasscode) {
            {
                KeypadButton(
                    text = "Back",
                    onClick = lockFirstTimeVM::goBack,
                    modifier = it
                )
            }
        } else {
            null
        },
        rightOfZeroCustomBtn = {
            KeypadButton(
                text = "Cancel",
                modifier = it,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primaryVariant
            ) {
                println("")
            }
        }
    )

    if (state.loading) {
        Loading()
    }

    if (state.showBiometricDialog) {

        AlertDialog(
            onDismissRequest = {
                lockFirstTimeVM.setShowBiometricDialog(false)
                lockFirstTimeVM.viewModelScope.launch {
                    submit(false)
                }
            },
            onConfirmation = {
                lockFirstTimeVM.setShowBiometricDialog(false)
                Biometrics.authenticateWithBiometric(context,
                    onSuccess = {
                        lockFirstTimeVM.viewModelScope.launch {
                            submit(true)
                        }
                    }, onFail = {}, onError = {})
            },
            dialogTitle = "Use Biometrics",
            dialogText = "Do you want to use biometrics to log in?"
        )
    }
}