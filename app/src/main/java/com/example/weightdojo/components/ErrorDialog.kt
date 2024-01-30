package com.example.weightdojo.components

import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onConfirm,
    title: String,
    text: String?
) {
    if (text == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        onConfirmation = onConfirm,
        dialogTitle = title,
        dialogText = text,
        cancelButtonText = "",
        confirmButtonText = "Ok"
    )
}