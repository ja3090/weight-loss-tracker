package com.example.weightdojo.screens.home.meallist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.components.dialogs.ErrorDialog
import com.example.weightdojo.components.mealcreation.MealCreation
import com.example.weightdojo.components.mealcreation.MealCreationOptions
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.MealWithNutrimentData
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.VMFactory

@Composable
fun MealList(
    meals: List<MealWithNutrimentData>,
    mealListVm: MealListViewModel = viewModel(
        factory = VMFactory.build {
            MealListViewModel()
        }
    ),
    mealListState: MealListVMState = mealListVm.state,
    homeViewModel: HomeViewModel = viewModel()
) {
    LaunchedEffect(meals) {
        mealListVm.reset()
    }

    ErrorDialog(
        onConfirm = { mealListVm.dismissErrorMessage() },
        title = "Error",
        text = mealListState.errorMessage
    )

    if (mealListState.mealCreationModalOpen) {
        MealCreation(
            detailedMeal = mealListState.singleMealDetailed,
            openModal = mealListVm::openModal,
            mealCreationOptions = MealCreationOptions.EDITING,
            onSuccess = { homeViewModel.refresh() }
        )
    }

    TextDefault(
        text = "Calories",
        color = MaterialTheme.colors.primary.copy(0.6f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Column(
        modifier = Modifier
            .padding(Sizing.paddings.small)
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    Sizing.cornerRounding
                )
            )
            .background(MaterialTheme.colors.background)
    ) {
        meals.mapIndexed { index, meal ->
            MealItem(
                index = index,
                meal = meal,
                activeMeal = mealListState.activeMeal,
                setter = { mealListVm.setActive(it) },
                arrSize = meals.size,
                onEditButtonClick = { mealListVm.editMeal(it) }
            )
        }
    }
}