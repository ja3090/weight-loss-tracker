package com.example.weightdojo.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

const val TEST_TAG = "Home screen"

data class Data(
    var nameList: List<String> = listOf(
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
        "Bob",
    )
)
@Composable
fun Home() {
    // actual composable state
    var offset = rememberScrollState()
    var initialMaxValue by remember {
        mutableIntStateOf(0)
    }

    var state by remember {
        mutableStateOf(Data())
    }

    Column {
        LaunchedEffect(Unit) {
            launch {
                initialMaxValue = offset.maxValue
                offset.animateScrollTo(
                    offset.maxValue / 2
                )
            }
        }

        Text(
            text = "Add",
            modifier = Modifier
                .background(Color.Cyan)
                .clickable {
                    state = state.copy(nameList = state.nameList + "dsads")
                })

        Box(
            Modifier
//                .height(50.dp)
//                .width(150.dp)
                .horizontalScroll(offset)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Row {
                state.nameList.map {
                    Box(
                        Modifier
                            .padding(5.dp)
                            .background(Color.Red)
                            .width(100.dp)
                    ) {
                        Text(text = it)
                    }
                }
            }
        }
    }

}