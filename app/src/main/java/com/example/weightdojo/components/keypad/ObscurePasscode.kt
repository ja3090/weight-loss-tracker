package com.example.weightdojo.components.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ObscurePasscode(inputValue: String, modifier: Modifier = Modifier) {
    var ind = 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        while (ind < 4) {
            if (inputValue.length > ind) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = MaterialTheme.colors.primaryVariant,
                            shape = CircleShape
                        )
                        .border(
                            width = 5.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        .size(25.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 5.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        .size(25.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            ind += 1
        }
    }
}