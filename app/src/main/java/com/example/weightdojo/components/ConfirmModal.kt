package com.example.weightdojo.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.weightdojo.components.dialogs.AlertDialog

class ConfirmModal(
    val title: String,
    val text: String,
    val onConfirm: () -> Unit,
) {
    private var confirmDelete by mutableStateOf(false)

    @Composable
    fun Modal() {
        if (confirmDelete) {
            AlertDialog(
                onDismissRequest = { confirmDelete = false },
                onConfirmation = {
                    onConfirm()
                    confirmDelete = false
                },
                dialogTitle = title,
                dialogText = text,
            )
        }
    }

    fun openOrClose(boolean: Boolean) {
        confirmDelete = boolean
    }
}