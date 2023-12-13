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

@Composable
fun ObscurePasscode(inputValue: String, length: Int, modifier: Modifier = Modifier) {
    var ind = 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        while (ind < length) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        color = if (inputValue.length > ind) MaterialTheme.colors.primaryVariant else Color.Transparent,
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