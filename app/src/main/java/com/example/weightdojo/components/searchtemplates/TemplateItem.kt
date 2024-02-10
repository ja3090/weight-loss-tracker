package com.example.weightdojo.components.searchtemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.ConfirmDelete
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.components.toast
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.database.models.Searchable
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates.SearchTemplatesBaseVM
import com.example.weightdojo.ui.Sizing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

@Composable
fun <Template : Searchable> TemplateItem(
    it: Searchable,
    isActive: Boolean,
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    viewModel: ViewModel,
    per100: Boolean,
    onUseClick: KFunction1<Template?, Unit>
) {
    val searchTemplateVm: SearchTemplatesBaseVM<Template> = viewModel()
    val calorieUnit = config?.calorieUnit?.name ?: "KCAL"
    val context = LocalContext.current

    val confirmDelete = ConfirmDelete {
        viewModel.viewModelScope.launch(Dispatchers.IO) {

            val res = searchTemplateVm.deleteTemplate(it as Template)

            if (res.success) {
                toast("Success", context)
                searchTemplateVm.reset()
            } else {
                toast(res.errorMessage, context)
            }
        }
    }

    confirmDelete.DeleteModal()

    Box(
        modifier = Modifier
            .clickable { searchTemplateVm.makeActive(it) }
    ) {
        Column(
            modifier = Modifier
                .padding(Sizing.paddings.medium)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { searchTemplateVm.makeActive(it) }
            ) {
                TextDefault(text = it.name, color = MaterialTheme.colors.primaryVariant)

                TextDefault(text = "${it.cals.toInt()} $calorieUnit")
            }

            if (isActive) {
                SubListItem(title = "Protein", value = it.protein, per100 = per100)
                SubListItem(title = "Fat", value = it.fat, per100 = per100)
                SubListItem(title = "Carbs", value = it.carbs, per100 = per100)
                Column(
                    modifier = Modifier
                        .padding(top = Sizing.paddings.extraSmall)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomButton(buttonName = "Use") {
                        onUseClick(it as Template)
                    }
                    TextDefault(
                        text = "Delete",
                        modifier = Modifier
                            .clickable { confirmDelete.openOrClose(true) }
                            .padding(Sizing.paddings.small),
                        fontSize = Sizing.font.small
                    )
                }
            }
        }
    }

}

@Composable
fun SubListItem(title: String, value: Float?, per100: Boolean) {
    val textSize = Sizing.font.default * 0.85

    Row(
        modifier = Modifier
            .padding(vertical = Sizing.paddings.extraSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDefault(text = title, fontSize = textSize)
        TextDefault(
            text = "${(value ?: 0f).toInt()} ${if (per100) "/ 100G" else "G"}",
            fontSize = textSize
        )
    }
}