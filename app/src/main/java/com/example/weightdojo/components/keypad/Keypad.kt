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
import com.example.weightdojo.PASSCODE_LENGTH
import com.example.weightdojo.R
import com.example.weightdojo.components.TextDefault
import com.example.weightdojo.components.icon.IconBuilder

@Composable
fun Keypad(
    keyClick: (key: String) -> Unit,
    delete: () -> Unit,
    submit: () -> Unit,
    inputValue: String,
    promptText: String,
    length: Int = PASSCODE_LENGTH,
    leftOfZeroCustomBtn: @Composable ((
            modifier: Modifier
            ) -> Unit)? = null,
    rightOfZeroCustomBtn: @Composable ((
        modifier: Modifier
    ) -> Unit)? = null,
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
                    length = length,
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
                    onClick = delete,
                    testTag = "Delete"
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
                    onClick = { submit() },
                    testTag = "Submit",
                )
            }

            if (leftOfZeroCustomBtn == null && rightOfZeroCustomBtn == null) return

            Row(
                modifier = Modifier.weight(1f)
            ) {
                if (leftOfZeroCustomBtn !== null) {
                    leftOfZeroCustomBtn(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f))
                } else {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
                if (rightOfZeroCustomBtn !== null) {
                    rightOfZeroCustomBtn(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f))
                } else {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}
