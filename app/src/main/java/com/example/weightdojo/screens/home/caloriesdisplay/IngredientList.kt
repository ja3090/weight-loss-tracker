package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.ConfirmDelete
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.addingredients.AddIngredient
import com.example.weightdojo.components.addingredients.searchingredienttemplates.SearchIngredientTemplates
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.IngredientAsInput
import com.example.weightdojo.components.successToast
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.components.toast
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.totalGrams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun IngredientList(
    calorieUnit: CalorieUnits,
    mealListVM: MealListVM = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    state: MealListState = mealListVM.state,
) {
    if (state.addIngredientModalOpen) {
        AddIngredientModal()
    }

    val context = LocalContext.current

    val confirmDelete = ConfirmDelete {
        mealListVM.viewModelScope.launch(Dispatchers.IO) {
            val result = mealListVM.deleteHandler()

            if (result.success) {
                toast("Success", context)
                mealListVM.removeActive()
                homeViewModel.refresh()
            } else {
                toast(result.errorMessage, context)
            }
        }
    }

    confirmDelete.DeleteModal()

    Column(
        modifier = Modifier
            .padding(bottom = Sizing.paddings.medium)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = Sizing.cornerRounding,
                    bottomStart = Sizing.cornerRounding
                )
            )
            .background(MaterialTheme.colors.secondary)
    ) {

        if (state.isEditing) {
            state.ingredientList?.map { ingredientAsState ->
                IngredientAsInput(
                    ingredientState = ingredientAsState,
                    onValueChange = {
                        mealListVM.changeIngredient(it)
                    },
                    onConfirmDelete = mealListVM::deleteIngredient,
                    activeIngredientId = mealListVM.state.activeIngredientId,
                    setActiveIngredient = mealListVM::setActiveIngredient
                )
            }
        } else {
            state.ingredientList?.map {
                Ingredient(
                    name = it.name,
                    weightUnit = calorieUnit.name,
                    totalCalories = CalorieUnit.convert(
                        to = calorieUnit,
                        value = totalGrams(it.grams, it.caloriesPer100)
                    )
                )
            }
        }

        if (!state.isEditing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconBuilder(
                    id = R.drawable.edit,
                    contentDescription = "edit",
                    testTag = "EDIT_BUTTON",
                    modifier = Modifier
                        .clickable { mealListVM.showIngredientListAsState() }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
                IconBuilder(
                    id = R.drawable.delete,
                    contentDescription = "delete",
                    testTag = "DELETE_BUTTON",
                    modifier = Modifier
                        .clickable { confirmDelete.openOrClose(true) }
                        .weight(1f)
                        .fillMaxSize()
                        .padding(Sizing.paddings.medium),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Sizing.paddings.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddIngredient {
                    mealListVM.openAddIngModal(true)
                }
                CustomButton(buttonName = "Save") {
                    mealListVM.makeEdits()
                }
                TextDefault(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(
                            vertical = Sizing.paddings.small,
                            horizontal = Sizing.paddings.medium
                        )
                        .clickable { mealListVM.removeActive() },
                    fontSize = Sizing.font.small
                )
            }
        }
    }
}