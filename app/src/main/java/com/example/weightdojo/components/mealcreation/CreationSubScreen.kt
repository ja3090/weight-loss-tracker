package com.example.weightdojo.components.mealcreation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.ConfirmModal
import com.example.weightdojo.components.CustomButton
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.Field
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.ui.Sizing

@Composable
fun CreationSubScreen(
    openModal: (Boolean) -> Unit,
    mealCreationVm: MealCreationVM = viewModel(),
    state: MealCreationState = mealCreationVm.stateHandler.state,
    submit: () -> Unit
) {

    Field(
        value = state.singleMealDetailed.mealName,
        onValueChange = { mealCreationVm.stateHandler.changeMealName(it) },
        placeholder = "Meal Name",
        trailingIcon = {},
        modifier = Modifier
            .padding(Sizing.paddings.medium)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    CustomDivider()
    TextDefault(
        textAlign = TextAlign.Center,
        text = "Values per 100g*",
        color = MaterialTheme.colors.primary.copy(0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Sizing.paddings.small),
        fontSize = Sizing.font.small
    )
    Column(
        modifier = Modifier
            .heightIn(max = 400.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (ingredient in state.singleMealDetailed.ingredients) {
            if (ingredient.markedFor == Marked.DELETE) continue
            key(ingredient.internalId) { IngredientInput(ingredient = ingredient) }
        }
        IconBuilder(
            id = R.drawable.add,
            contentDescription = "Add ingredient",
            testTag = "ADD_INGREDIENT",
            modifier = Modifier
                .clickable {
                    mealCreationVm.stateHandler.setSubScreen(SubScreens.SEARCH_INGREDIENTS)
                }
                .padding(vertical = Sizing.paddings.medium)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(buttonName = "Save") {
            submit()
        }
        TextDefault(
            text = "Cancel",
            fontSize = Sizing.font.small,
            modifier = Modifier
                .padding(Sizing.paddings.small)
                .clickable {
                    openModal(false)
                    mealCreationVm.stateHandler.reset()
                }
        )
    }
}