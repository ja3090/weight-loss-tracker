package com.example.weightdojo.screens.lockfirsttime

import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.utils.Biometrics
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun LockFirstTime(
    onSubmitRedirect: KSuspendFunction0<Unit>,
    viewModel: LockFirstTimeViewModel = viewModel(
        factory = VMFactory.build {
            LockFirstTimeViewModel(MyApp.appModule.database)
        }
    )
) {
    val context = LocalContext.current as FragmentActivity

    val state = viewModel.state
    fun getInputValue(): String {
        return if (state.enteringPasscode) state.firstEnter
        else state.secondEnter
    }

    fun getPromptText(): String {
        return if (state.enteringPasscode) "Create a 4-digit passcode"
        else "Confirm your passcode"
    }

    suspend fun optOutBio() {
        viewModel.viewModelScope.launch {

            val submitted = async { viewModel.submitConfig(false) }

            if (submitted.await()) onSubmitRedirect()
        }
    }

    fun onSubmit() {

        viewModel.viewModelScope.launch {
            val ok = viewModel.submit()

            if (ok) {
                val canAuth = Biometrics.canUseBiometrics(context)

                if (canAuth) viewModel.setShowBiometricDialog(true)
                else optOutBio()
            }
        }
    }

    Keypad(
        keyClick = viewModel::addInput,
        delete = viewModel::delete,
        submit = ::onSubmit,
        goBack = viewModel::goBack,
        inputValue = getInputValue(),
        promptText = getPromptText(),
        isConfirming = state.confirmingPasscode,
        length = viewModel.passcodeLength
    )

    if (state.loading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.White,
        )
    }

    if (state.showBiometricDialog) {

        AlertDialog(
            onDismissRequest = {
                viewModel.setShowBiometricDialog(false)
                viewModel.viewModelScope.launch {
                    optOutBio()
                }
            },
            onConfirmation = {
                viewModel.setShowBiometricDialog(false)
                Biometrics.authenticateWithBiometric(context,
                    onSuccess = {
                        viewModel.viewModelScope.launch {
                            val submitted = async { viewModel.submitConfig(true) }

                            if (submitted.await()) onSubmitRedirect()
                        }
                    }, onFail = {}, onError = {})
            },
            dialogTitle = "Use Biometrics",
            dialogText = "Do you want to use biometrics to log in?"
        )
    }
}