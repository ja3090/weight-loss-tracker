package com.example.weightdojo.components.addingredients.searchingredienttemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.search.Search
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory

@Composable
fun SearchIngredientTemplates(
    searchIngredientTemplatesVM: SearchIngredientTemplatesVM = viewModel(factory = VMFactory.build {
        SearchIngredientTemplatesVM(database = MyApp.appModule.database)
    }),
    isOpen: Boolean,
    onAddNew: () -> Unit,
    useTemplate: (templ: IngredientTemplate) -> Unit
) {
    LaunchedEffect(isOpen) {
        searchIngredientTemplatesVM.reset()
    }

    Search(
        onSearch = {
            searchIngredientTemplatesVM.setSearchResults(it)
        },
        items = searchIngredientTemplatesVM.state.ingredientTemplates
    ) {
        IngredientTemplateItem(it, useTemplate)
    }
    Row(
        modifier = Modifier
            .padding(
                vertical = Sizing.paddings.medium,
                horizontal = Sizing.paddings.small
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextDefault(
            text = "Add New",
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.clickable { onAddNew() }
        )
    }
}