package com.example.weightdojo.screens.LockFirstTime

import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weightdojo.components.keypad.Keypad

@Composable
fun LockFirstTime(
    navHostController: NavHostController
) {
    val viewModel = viewModel<LockFirstTimeViewModel>()
    val state = viewModel.state
    fun getInputValue(): String {
        return if (state.enteringPasscode) state.firstEnter
        else state.secondEnter
    }

    fun getPromptText(): String {
        return if (state.enteringPasscode) "Create a 4-digit passcode"
        else "Confirm your passcode"
    }
    
    if (state.confirmingPasscode) {
        Text(text = "< Back")
    }

    Keypad(
        keyClick = viewModel::addInput,
        delete = viewModel::delete,
        submit = viewModel::submit,
        goBack = viewModel::goBack,
        inputValue = getInputValue(),
        navHostController = navHostController,
        viewModel = viewModel,
        promptText = getPromptText(),
        isConfirming = state.confirmingPasscode
    )

    if (state.loading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.White,
        )
    }
}