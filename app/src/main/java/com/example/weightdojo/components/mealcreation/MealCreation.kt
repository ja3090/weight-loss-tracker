package com.example.weightdojo.components.mealcreation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.dialogs.ErrorDialog
import com.example.weightdojo.components.toast
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.components.ModalFrame
import com.example.weightdojo.datatransferobjects.RepoResponse
import com.example.weightdojo.screens.home.HomeState
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun getColour(isError: Boolean, color: Color): Color {
    return if (isError) CustomColors.Red.copy(0.5f)
    else color
}

@Composable
fun <VM : ViewModel> MealCreation(
    detailedMeal: SingleMealDetailed? = null,
    mealCreationOptions: MealCreationOptions,
    viewModel: VM,
    onSubmit: suspend (SingleMealDetailed) -> RepoResponse<Unit?>,
    mealCreationVm: MealCreationVM = viewModel(
        factory = VMFactory.build {
            MealCreationVM(
                singleMealDetailed = detailedMeal,
                mealCreationOptions = mealCreationOptions
            )
        },
    ),
    state: MealCreationState = mealCreationVm.stateHandler.state,
    openModal: (Boolean) -> Unit,
    onSuccess: (() -> Unit)? = null
) {
    Log.d("mealCreationOptions", mealCreationOptions.toString())

    LaunchedEffect(detailedMeal, mealCreationOptions) {
        val subScreen = if (mealCreationOptions == MealCreationOptions.CREATING) {
            SubScreens.SEARCH_MEALS
        } else {
            SubScreens.CREATION
        }

        mealCreationVm.stateHandler.reset(detailedMeal, subScreen)
    }

    val context = LocalContext.current

    val modalTitle = when (state.subScreens) {
        SubScreens.SEARCH_MEALS -> "Search Meals"
        SubScreens.SEARCH_INGREDIENTS -> "Search Ingredients"
        SubScreens.CREATION -> "Create Entry"
    }

    val onBackClick = {
        when (state.subScreens) {
            SubScreens.SEARCH_MEALS -> openModal(false)
            SubScreens.SEARCH_INGREDIENTS -> {
                mealCreationVm.stateHandler.setSubScreen(SubScreens.CREATION)
            }

            SubScreens.CREATION -> {
                if (mealCreationOptions == MealCreationOptions.CREATING) {
                    mealCreationVm.stateHandler.setSubScreen(SubScreens.SEARCH_MEALS)
                } else {
                    openModal(false)
                }
            }
        }
    }

    fun submit() {
        val valid = mealCreationVm.validator.validate(state)

        if (!valid.success) return mealCreationVm.stateHandler.setError(valid.message)

        viewModel.viewModelScope.launch {
            val res = onSubmit(state.singleMealDetailed)

            if (res.success) {
                toast("Success", context)
                if (onSuccess != null) onSuccess()
                openModal(false)
                mealCreationVm.stateHandler.reset()
            } else mealCreationVm.stateHandler.setError(res.errorMessage)
        }
    }

    ErrorDialog(
        onConfirm = { mealCreationVm.stateHandler.setError(null) },
        title = "Error",
        text = mealCreationVm.stateHandler.state.errorMessage
    )

    Dialog(
        onDismissRequest = { openModal(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ModalFrame(
            title = modalTitle,
            onClose = {
                mealCreationVm.stateHandler.reset()
                openModal(false)
            },
            onBack = { onBackClick() }
        ) {
            Wizard(openModal = openModal) {
                submit()
            }
        }
    }
}