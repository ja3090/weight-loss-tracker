package com.example.weightdojo.components.mealcreation

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.Field
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing
import java.util.UUID

@Composable
fun IngredientInput(
    ingredient: SingleMealDetailedIngredient,
    mealCreationVM: MealCreationVM = viewModel(),
    stateHandler: MealCreationStateHandler = mealCreationVM.stateHandler,
    isActive: Boolean = stateHandler.state.activeIngredient?.internalId == ingredient.internalId,
) {

    Column(
        modifier = Modifier
            .padding(
                start = Sizing.paddings.small,
                end = Sizing.paddings.small,
                bottom = Sizing.paddings.small
            )
            .animateContentSize()
    ) {
        Field(
            value = ingredient.ingredientName,
            onValueChange = {
                mealCreationVM.stateHandler.changeIngredientName(it, ingredient.internalId)
            },
            placeholder = "Ingredient Name",
            trailingIcon = {
                IconBuilder(
                    id = R.drawable.arrow_down,
                    contentDescription = "Close detailed list",
                    testTag = "DETAILED_LIST_CLOSE",
                    modifier = Modifier
                        .graphicsLayer(rotationZ = if (isActive) 180f else 0f)
                        .offset(x = if (isActive) (-6).dp else 6.dp)
                        .clickable { mealCreationVM.stateHandler.setActiveIngredient(ingredient) }
                        .padding(
                            start = if (isActive) 0.dp else Sizing.paddings.medium,
                            end = if (isActive) Sizing.paddings.medium else 0.dp
                        ),
                    color = it
                )
            },
            modifier = Modifier.fillMaxWidth(),
            inputTextColour = MaterialTheme.colors.primaryVariant,
        )
        Field(
            value = if (ingredient.caloriesPer100 == 0f) "" else {
                "${ingredient.caloriesPer100.toInt()}"
            },
            onValueChange = {
                mealCreationVM.stateHandler.changeCaloriesPer100(
                    it,
                    ingredient.internalId
                )
            },
            placeholder = "Calories*",
            trailingIcon = {
                TextDefault(
                    text = MyApp.appModule.calorieUnit.name,
                    color = MaterialTheme.colors.primary.copy(0.5f),
                    fontSize = Sizing.font.small
                )
            },
            fontSize = Sizing.font.small,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            inputTextColour = MaterialTheme.colors.primary
        )
        Field(
            value = if (ingredient.grams == 0f) "" else {
                "${ingredient.grams.toInt()}"
            },
            onValueChange = {
                mealCreationVM.stateHandler.changeIngredientGrams(
                    it,
                    ingredient.internalId
                )
            },
            placeholder = "Total Grams",
            trailingIcon = {
                TextDefault(
                    text = "Total Grams",
                    color = it,
                    fontSize = Sizing.font.small,
                )
            },
            fontSize = Sizing.font.small,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            inputTextColour = MaterialTheme.colors.primary
        )
        if (isActive) {
            ingredient.nutriments.map {
                NutrimentInput(
                    nutriment = it,
                    ingredientUUID = ingredient.internalId
                )
            }
        }
    }
}