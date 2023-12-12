package com.example.weightdojo.components.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.R
import com.example.weightdojo.components.TextDefault
import com.example.weightdojo.components.icon.IconBuilder
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun Keypad(
    keyClick: (key: String) -> Unit,
    delete: () -> Unit,
    goBack: () -> Unit,
    submit: KSuspendFunction0<Boolean>,
    inputValue: String,
    viewModel: ViewModel,
    promptText: String,
    isConfirming: Boolean = false,
    onSubmitRedirect: KSuspendFunction0<Unit>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                TextDefault(text = promptText)
            }
            Row(modifier = Modifier.weight(1f)) {
                ObscurePasscode(
                    inputValue = inputValue
                )
            }
        }

        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            var sequence = 1

            for (i in 1..3) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    for (y in 1..3) {
                        val num = sequence.toString()

                        KeypadButton(
                            text = num, modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            keyClick(num)
                        }

                        sequence += 1
                    }
                }
            }

            Row(
                modifier = Modifier.weight(1f)
            ) {
                IconBuilder(
                    id = R.drawable.backspace,
                    contentDescription = "delete last character",
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = delete
                )
                KeypadButton(
                    text = "0", modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    keyClick("0")
                }
                IconBuilder(
                    id = R.drawable.check,
                    contentDescription = "submit",
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {
                        viewModel.viewModelScope.launch {
                            val ok = submit()

                            if (ok) onSubmitRedirect()
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {
                if (isConfirming) {
                    KeypadButton(
                        text = "Back",
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primaryVariant
                    ) {
                        goBack()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }

                KeypadButton(
                    text = "Cancel",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant
                ) {
                    println("")
                }
            }
        }
    }
}
