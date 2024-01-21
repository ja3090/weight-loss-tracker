package com.example.weightdojo.components.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.ui.Sizing

@Composable
fun <T> SearchResults(items: List<T>?, content: @Composable (item: T) -> Unit) {
    var index = 0

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        items?.map {
            Box(modifier = Modifier.wrapContentHeight()) {
                content(it)
            }

            if (index != items.size - 1) {
                CustomDivider(tinted = true)
            }

            index++
        }
    }
}