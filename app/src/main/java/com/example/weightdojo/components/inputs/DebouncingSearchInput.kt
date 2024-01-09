package com.example.weightdojo.components.inputs

import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncingSearchBar(
    onSearch: (String) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    var debounceJob by remember { mutableStateOf<Job?>(null) }

    TextField(
        value = textState,
        onValueChange = { newValue ->
            textState = newValue

            debounceJob?.cancel()

            debounceJob = coroutineScope.launch(dispatcher) {
                delay(500)
                onSearch(newValue.text)
            }
        }
    )
}
