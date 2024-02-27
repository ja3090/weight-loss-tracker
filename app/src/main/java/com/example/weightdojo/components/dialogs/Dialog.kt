package com.example.weightdojo.components.dialogs

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.weightdojo.components.text.TextDefault

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onReject: () -> Unit = onDismissRequest,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmButtonText: String = "Confirm",
    cancelButtonText: String = "Cancel"
) {
    AlertDialog(
        title = {
            TextDefault(text = dialogTitle)
        },
        text = {
            TextDefault(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                TextDefault(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onReject()
                }
            ) {
                TextDefault(cancelButtonText)
            }
        },
        containerColor = MaterialTheme.colors.background
    )
}