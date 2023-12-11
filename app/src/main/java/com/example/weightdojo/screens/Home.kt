package com.example.weightdojo.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

const val TEST_TAG = "Home screen"

@Composable
fun Home() {
    Text(modifier = Modifier.testTag(TEST_TAG), text = "Home")
}