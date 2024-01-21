package com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.search.Search
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.addcalories.AddCaloriesVm
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory

@Composable
fun SearchMealTemplates(
    searchMealTemplatesVM: SearchMealTemplatesVM = viewModel(factory = VMFactory.build {
        SearchMealTemplatesVM(database = MyApp.appModule.database)
    }),
) {
    val addCaloriesVm: AddCaloriesVm = viewModel()

    Search(
        onSearch = {
            searchMealTemplatesVM.setSearchResults(it)
        },
        items = searchMealTemplatesVM.state.mealTemplates
    ) {
        MealTemplateItem(it)
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
            modifier = Modifier.clickable { addCaloriesVm.moveToAddNewMeal() }
        )
    }
}