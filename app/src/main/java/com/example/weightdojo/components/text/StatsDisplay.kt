package com.example.weightdojo.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weightdojo.ui.Sizing

@Composable
fun StatsDisplay() {

    Box(
        modifier = Modifier
            .padding(
                top = 0.dp,
                start = Sizing.paddings.small,
                end = Sizing.paddings.small,
                bottom = Sizing.paddings.small
            )
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        Sizing.cornerRounding
                    )
                )
                .background(color = MaterialTheme.colors.secondary)
                .fillMaxWidth(),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    TextDefault(text = "1500kcal")
                }
                Divider(
                    color = MaterialTheme.colors.primaryVariant,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = Sizing.paddings.small)
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .aspectRatio(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextDefault(text = "65kg")
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextDefault(text = "80kg")
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextDefault(text = "2400kcal", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}