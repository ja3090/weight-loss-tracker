package com.example.weightdojo.components.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.ui.Sizing

@Composable
fun <T> SearchResults(items: List<T>?, content: @Composable (T, Boolean, (T) -> Unit) -> Unit) {
    var index = 0
    var active by remember { mutableStateOf<T?>(null) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        items?.map { item ->

            content(item, item == active) { active = if (it == active) null else it }


            if (index != items.size - 1) {
                CustomDivider(tinted = true)
            }

            index++
        }
    }
}