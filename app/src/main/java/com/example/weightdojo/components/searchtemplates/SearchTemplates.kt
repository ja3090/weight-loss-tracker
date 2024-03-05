package com.example.weightdojo.components.searchtemplates

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.ConfirmModal
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.NutritionBreakdown
import com.example.weightdojo.components.search.Search
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.NutrimentSummary
import com.example.weightdojo.datatransferobjects.NutritionBreakdown
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun <K : NutrimentSummary, V : NutritionBreakdown<K>> SearchTemplates(
    template: Templates,
    key: String,
    searchTemplatesVM: SearchTemplatesVM<K, V> = viewModel(
        factory = VMFactory.build {
            SearchTemplatesVM<K, V>(templates = template)
        },
        key = key
    ),
    onUseTemplate: (V) -> Unit,
    onAddNew: () -> Unit
) {
    val context = LocalContext.current
    val confirmDelete = ConfirmModal(
        title = "Delete",
        text = "Are you sure you want to delete this?"
    ) {
    }

    confirmDelete.Modal()

    Search(
        onSearch = {
            searchTemplatesVM.searchTemplates(it)
        },
        items = searchTemplatesVM.state.templates
    ) { item, isActive, makeActive ->
        NutritionBreakdown(
            data = item,
            setter = makeActive,
            isActive = isActive
        )
        if (isActive) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(buttonName = "Use") {
                    onUseTemplate(item)
                }
                TextDefault(
                    text = "Delete",
                    fontSize = Sizing.font.small,
                    modifier = Modifier
                        .clickable {
                            confirmDelete.openOrClose(true) {
                                searchTemplatesVM.useDeleteTemplate(item.id, context)
                            }
                        }
                        .padding(Sizing.paddings.small)
                )
            }
        }
    }
    TextDefault(
        text = "Add New",
        modifier = Modifier
            .clickable { onAddNew() }
            .padding(Sizing.paddings.medium)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}