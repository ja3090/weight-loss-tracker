package com.example.weightdojo.screens.lockfirsttime

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.components.keypad.KeypadButton
import com.example.weightdojo.screens.lock.BiometricDialog
import com.example.weightdojo.screens.lock.UsePasscodeDialog
import com.example.weightdojo.utils.Biometrics
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun LockFirstTime(
    onSubmitRedirect: () -> Unit,
    lockFirstTimeVM: LockFirstTimeViewModel = viewModel(
        factory = VMFactory.build {
            LockFirstTimeViewModel(MyApp.appModule.configSessionCache)
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
        lockFirstTimeVM.viewModelScope.launch(Dispatchers.IO) {

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
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
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
                        modifier = it,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primaryVariant
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
                    lockFirstTimeVM.setShowUsePasscodeDialog(true)
                }
            },
            isLoading = state.loading
        )
    }

    BiometricDialog(submit = ::submit, context = context)
    if (lockFirstTimeVM.state.showUsePasscodeDialog) {
        UsePasscodeDialog {
            onSubmitRedirect()
        }
    }
}