package com.example.weightdojo.components.searchtemplates

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun <K : NutrimentSummary, V : NutritionBreakdown<K>> SearchTemplates(
    template: Templates,
    searchTemplatesVM: SearchTemplatesVM<K, V> = viewModel(
        factory = VMFactory.build {
            SearchTemplatesVM<K, V>(templates = template)
        }
    ),
    onUseTemplate: (V) -> Unit,
    onAddNew: () -> Unit
) {
    Search(
        onSearch = {
            searchTemplatesVM.viewModelScope.launch(Dispatchers.IO) {
                searchTemplatesVM.searchTemplates(it)
            }
        },
        items = searchTemplatesVM.state.templates
    ) { item, isActive, makeActive ->
        NutritionBreakdown(
            data = item,
            setter = makeActive,
            isActive = isActive
        )
        if (isActive) {
            CustomButton(buttonName = "Use") {
                onUseTemplate(item)
            }
        }
    }
    TextDefault(
        text = "Add New",
        fontSize = Sizing.font.small,
        modifier = Modifier.clickable { onAddNew() }
    )
}