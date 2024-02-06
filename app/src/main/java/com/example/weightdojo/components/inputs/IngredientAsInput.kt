package com.example.weightdojo.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.AlertDialog
import com.example.weightdojo.components.ConfirmDelete
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.datatransferobjects.IngredientState
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.CalorieUnit
import com.example.weightdojo.utils.CalorieUnits
import com.example.weightdojo.utils.WeightUnit
import java.util.UUID

@Composable
fun IngredientAsInput(
    ingredientState: IngredientState,
    config: Config? = MyApp.appModule.configSessionCache.getActiveSession(),
    calorieUnit: CalorieUnits = config?.calorieUnit ?: AppConfig.internalDefaultCalorieUnit,
    onConfirmDelete: (newState: IngredientState) -> Unit,
    onValueChange: (ingredientState: IngredientState) -> Unit,
    activeIngredientId: UUID?,
    setActiveIngredient: (ingredient: IngredientState) -> Unit
) {
    if (ingredientState.markedFor == Marked.DELETE) return

    val confirmDelete = ConfirmDelete {
        onConfirmDelete(ingredientState)
    }

    confirmDelete.DeleteModal()

    Column {
        NameField(
            value = ingredientState.name,
            onValueChange = {
                val newState = ingredientState.copy(name = it)

                onValueChange(newState)
            },
            modifier = Modifier
                .clickable { setActiveIngredient(ingredientState) },
            showDetailedList = activeIngredientId == ingredientState.internalId,
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        CalorieBreakdown(
            calorieInfo = CalorieUnit.convert(
                to = calorieUnit,
                value = ingredientState.caloriesPer100
            ),
            nutrimentName = calorieUnit.name,
            setter = {
                val newState = ingredientState.copy(
                    caloriesPer100 = CalorieUnit.convert(
                        from = calorieUnit,
                        value = it
                    )
                )

                onValueChange(newState)
            },
            per100 = true,
            placeholderText = "Calories"
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        if (activeIngredientId == ingredientState.internalId) {
            CalorieBreakdown(
                calorieInfo = ingredientState.proteinPer100,
                nutrimentName = "Protein",
                setter = {
                    val newState = ingredientState.copy(proteinPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Protein"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )

            CalorieBreakdown(
                calorieInfo = ingredientState.fatPer100,
                nutrimentName = "Fat",
                setter = {
                    val newState = ingredientState.copy(fatPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Fat"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )

            CalorieBreakdown(
                calorieInfo = ingredientState.carbohydratesPer100,
                nutrimentName = "Carbs",
                setter = {
                    val newState = ingredientState.copy(carbohydratesPer100 = it)

                    onValueChange(newState)
                },
                per100 = true,
                placeholderText = "Carbs"
            )

            CustomDivider(
                tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )
        }

        CalorieBreakdown(
            calorieInfo = ingredientState.grams,
            setter = {
                val newState = ingredientState.copy(grams = it)

                onValueChange(newState)
            },
            nutrimentName = "G",
            placeholderText = "Grams"
        )

        CustomDivider(
            tinted = true, modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )

        if (activeIngredientId == ingredientState.internalId) {
            IconBuilder(
                id = R.drawable.delete,
                contentDescription = "Delete ingredient",
                testTag = "DELETE_INGREDIENT",
                modifier = Modifier
                    .padding(vertical = Sizing.paddings.small, horizontal = Sizing.paddings.medium)
                    .fillMaxWidth()
                    .clickable { confirmDelete.openOrClose(true) },
            )
        }
    }
}