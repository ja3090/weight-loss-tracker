package com.example.weightdojo.screens.lock

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.screens.lockfirsttime.LockFirstTimeViewModel
import com.example.weightdojo.utils.Biometrics
import kotlinx.coroutines.launch

@Composable
fun BiometricDialog(
    lockFirstTimeVM: LockFirstTimeViewModel = viewModel(),
    submit: suspend (boolean: Boolean) -> Unit,
    context: FragmentActivity
) {


    if (lockFirstTimeVM.state.showBiometricDialog) {

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