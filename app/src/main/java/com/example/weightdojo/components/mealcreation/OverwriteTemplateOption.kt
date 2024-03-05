package com.example.weightdojo.components.mealcreation

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.dialogs.AlertDialog
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing

@Composable
fun OverwriteTemplateOption(
    mealCreationVM: MealCreationVM = viewModel(),
    modifier: Modifier = Modifier
) {
    var confirmModal by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (confirmModal) {
        AlertDialog(
            onDismissRequest = { confirmModal = false },
            onConfirmation = { mealCreationVM.useOverwriteTemplate(context) },
            onReject = { mealCreationVM.useCreateTemplate(context) },
            dialogTitle = "Overwrite Template?",
            dialogText = "Do you want to overwrite this?",
            cancelButtonText = "Create New"
        )
    }

    TextDefault(
        text = "Overwrite Template",
        fontSize = Sizing.font.small,
        modifier = Modifier
            .clickable { confirmModal = true }
            .then(modifier)
    )
}