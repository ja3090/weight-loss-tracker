package com.example.weightdojo.screens.LockFirstTime

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.dialogs.AlertDialog
import com.example.weightdojo.screens.lockfirsttime.LockFirstTimeViewModel

@Composable
fun UsePasscodeDialog(
    lockFirstTimeVM: LockFirstTimeViewModel = viewModel(),
    onSubmitRedirect: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            lockFirstTimeVM.confirmUsingPasscode(false)
        },
        onReject = {
            lockFirstTimeVM.confirmUsingPasscode(false)
        },
        onConfirmation = {
            lockFirstTimeVM.refusePasscode()
            onSubmitRedirect()
        },
        dialogTitle = "Cancel passcode setup",
        dialogText = "Cancel securing this app with a passcode?",
        confirmButtonText = "Yes",
        cancelButtonText = "No"
    )
}