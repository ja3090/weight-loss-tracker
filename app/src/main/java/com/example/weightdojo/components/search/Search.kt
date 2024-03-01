package com.example.weightdojo.components.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weightdojo.database.models.Searchable

@Composable
fun <T> Search(
    onSearch: (term: String) -> Unit,
    items: List<T>,
    listMapper: @Composable (T, Boolean, (T) -> Unit) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp, max = 500.dp)
    ) {
        DebouncingSearchBar(onSearch = { onSearch(it) })
        SearchResults(items = items) { item, isActive, setter ->
            listMapper(
                item,
                isActive,
                setter
            )
        }
    }
}