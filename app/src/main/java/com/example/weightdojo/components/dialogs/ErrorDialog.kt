package com.example.weightdojo.components.dialogs

import androidx.compose.runtime.Composable
import com.example.weightdojo.components.dialogs.AlertDialog

@Composable
fun ErrorDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onConfirm,
    title: String,
    text: String?
) {
    if (text != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            onConfirmation = onConfirm,
            dialogTitle = title,
            dialogText = text,
            cancelButtonText = "",
            confirmButtonText = "Ok"
        )
    }
}