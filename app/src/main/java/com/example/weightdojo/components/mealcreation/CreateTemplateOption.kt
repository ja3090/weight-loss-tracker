package com.example.weightdojo.components.mealcreation

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.ConfirmModal
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.repositories.mealrepo.CreateTemplate
import com.example.weightdojo.ui.Sizing

@Composable
fun CreateTemplateOption(
    mealCreationVM: MealCreationVM = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val confirmModal = ConfirmModal(
        title = "Create Template",
        text = "Do you want to create a template form this?"
    ) {
        mealCreationVM.useCreateTemplate(context)
    }

    confirmModal.Modal()

    TextDefault(
        text = "Create Template",
        fontSize = Sizing.font.small,
        modifier = Modifier
            .clickable { confirmModal.openOrClose(true) }
            .then(modifier)
    )
}