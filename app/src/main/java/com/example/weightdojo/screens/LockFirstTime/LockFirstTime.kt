package com.example.weightdojo.screens.lockfirsttime

import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.utils.VMFactory
import kotlin.reflect.KSuspendFunction0

@Composable
fun LockFirstTime(
    onSubmitRedirect: KSuspendFunction0<Unit>,
    viewModel: LockFirstTimeViewModel = viewModel(
        factory = VMFactory.build {
            LockFirstTimeViewModel(MyApp.appModule.database)
        }
    )
) {

    val state = viewModel.state
    fun getInputValue(): String {
        return if (state.enteringPasscode) state.firstEnter
        else state.secondEnter
    }

    fun getPromptText(): String {
        return if (state.enteringPasscode) "Create a 4-digit passcode"
        else "Confirm your passcode"
    }

    Keypad(
        keyClick = viewModel::addInput,
        delete = viewModel::delete,
        submit = viewModel::submit,
        goBack = viewModel::goBack,
        inputValue = getInputValue(),
        viewModel = viewModel,
        promptText = getPromptText(),
        isConfirming = state.confirmingPasscode,
        onSubmitRedirect = onSubmitRedirect,
        length = viewModel.passcodeLength
    )

    if (state.loading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.White,
        )
    }
}