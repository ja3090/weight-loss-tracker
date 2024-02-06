package com.example.weightdojo.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ConfirmDelete(
    val onConfirmDelete: () -> Unit
) {
    private var confirmDelete by mutableStateOf(false)

    @Composable
    fun DeleteModal() {
        if (confirmDelete) {
            AlertDialog(
                onDismissRequest = { confirmDelete = false },
                onConfirmation = {
                    onConfirmDelete()
                    confirmDelete = false
                },
                dialogTitle = "Delete",
                dialogText = "Are you sure you want to delete this?",
            )
        }
    }

    fun openOrClose(boolean: Boolean) {
        confirmDelete = boolean
    }
}