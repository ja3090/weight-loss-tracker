package com.example.weightdojo.components.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> Search(
    onSearch: (term: String) -> Unit,
    items: List<T>?,
    listMapper: @Composable (item: T) ->  Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().heightIn(min = 300.dp, max = 500.dp)
    ) {
        DebouncingSearchBar(onSearch = { onSearch(it) })
        SearchResults(items = items) { listMapper(it) }
    }
}