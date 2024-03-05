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
    private var callbackValue by mutableStateOf<(() -> Unit)?>(null)

    @Composable
    fun Modal() {
        if (confirmDelete) {
            AlertDialog(
                onDismissRequest = {
                    confirmDelete = false
                    callbackValue = null
                },
                onConfirmation = {
                    onConfirm()
                    callbackValue?.let { it() }
                    confirmDelete = false
                    callbackValue = null
                },
                dialogTitle = title,
                dialogText = text,
            )
        }
    }

    fun openOrClose(boolean: Boolean, callback: (() -> Unit)? = null) {
        confirmDelete = boolean
        callbackValue = callback
    }
}
