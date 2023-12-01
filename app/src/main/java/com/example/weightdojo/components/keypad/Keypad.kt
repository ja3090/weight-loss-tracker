package com.example.weightdojo.components.keypad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun Keypad(
    keyClick: (key: String) -> Unit,
    delete: () -> Unit,
    goBack: () -> Unit,
    submit: KSuspendFunction0<Boolean>,
    inputValue: String,
    navHostController: NavHostController,
    viewModel: ViewModel,
    promptText: String,
    isConfirming: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = promptText, color = Color.LightGray
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            ObscurePasscode(
                inputValue = inputValue, modifier = Modifier
                    .aspectRatio(3f)
                    .weight(3f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            var sequence = 1

            for (i in 1..3) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (y in 1..3) {
                        val num = sequence.toString()

                        KeypadButton(
                            text = num, modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                        ) {
                            keyClick(num)
                        }

                        sequence += 1
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    text = "DEL", modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                ) {
                    delete()
                }
                KeypadButton(
                    text = "0", modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                ) {
                    keyClick("0")
                }
                KeypadButton(
                    text = ">", modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                ) {
                    viewModel.viewModelScope.launch {
                        val ok = submit()

                        if (ok) navHostController.navigate("Home")
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                if (isConfirming) {
                    OutlinedButton(onClick = goBack) {
                        Text(text = "Back")
                    }
                } else {
                    Box {}
                }

                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}