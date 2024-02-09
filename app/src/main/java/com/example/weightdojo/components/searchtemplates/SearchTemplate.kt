package com.example.weightdojo.components.searchtemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.example.weightdojo.components.search.Search
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Searchable
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates.SearchTemplatesBaseVM
import com.example.weightdojo.ui.Sizing
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

enum class Templates {
    MEAL_TEMPLATES,
    INGREDIENT_TEMPLATES
}

@Composable
fun <Template : Searchable> SearchTemplate(
    searchTemplatesVm: SearchTemplatesBaseVM<Template>,
    onDelete: KSuspendFunction1<Template, RepoResponse<Nothing?>>,
    onUseClick: KFunction1<Template?, Unit>,
    viewModel: ViewModel,
    per100: Boolean
) {
    Search(
        onSearch = {
                   searchTemplatesVm.searchTemplates(it)
        },
        items = searchTemplatesVm.state.templates
    ) {
        TemplateItem(
            it = it,
            isActive = searchTemplatesVm.state.activeTemplate == it,
            onDelete = onDelete,
            onUseClick = onUseClick,
            per100 = per100,
            viewModel = viewModel
        )
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
            modifier = Modifier.clickable { onUseClick(null) }
        )
    }
}