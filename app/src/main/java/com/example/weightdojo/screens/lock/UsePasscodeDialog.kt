package com.example.weightdojo.screens.lock

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.screens.lockfirsttime.LockFirstTimeViewModel
import com.example.weightdojo.utils.Biometrics
import kotlinx.coroutines.launch

@Composable
fun UsePasscodeDialog(
    lockFirstTimeVM: LockFirstTimeViewModel = viewModel(),
    onSubmitRedirect: () -> Unit
) {

    if (lockFirstTimeVM.state.showUsePasscodeDialog) {

        AlertDialog(
            onDismissRequest = {
                lockFirstTimeVM.confirmUsingPasscode(false)
            },
            onReject = {
                lockFirstTimeVM.refusePasscode()
                onSubmitRedirect()
            },
            onConfirmation = {
                lockFirstTimeVM.confirmUsingPasscode(false)
            },
            dialogTitle = "Use a passcode?",
            dialogText = "Do you want to secure this app by using a passcode?"
        )
    }

}