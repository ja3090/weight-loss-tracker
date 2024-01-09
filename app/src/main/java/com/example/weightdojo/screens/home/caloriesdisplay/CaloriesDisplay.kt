package com.example.weightdojo.screens.home.caloriesdisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.ui.Sizing

@Composable
fun CaloriesDisplay(meals: List<Meal>?, weightUnit: String) {
    Box(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(
                    Sizing.cornerRounding,
                    Sizing.cornerRounding,
                    0.dp,
                    0.dp
                )
            )
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Heading(text = "Calories")
            CustomDivider(
                modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
            )

            MealList(meals, weightUnit)
        }
    }
}
