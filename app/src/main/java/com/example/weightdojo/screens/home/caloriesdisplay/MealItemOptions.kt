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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.ui.Sizing

@Composable
fun MealItemOptions(ingredientList: List<Ingredient>?, weightUnit: String) {
    Column(
        modifier = Modifier
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

        ingredientList?.map {
            Row(
                modifier = Modifier
                    .clickable { }
                    .fillMaxWidth()
                    .padding(
                        horizontal = Sizing.paddings.medium,
                        vertical = Sizing.paddings.extraSmall
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextDefault(
                    text = it.name,
                    color = MaterialTheme.colors.primaryVariant,
                    fontStyle = FontStyle.Italic
                )
                TextDefault(
                    text = it.calories.toInt().toString() + " " + weightUnit,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconBuilder(
                id = R.drawable.edit,
                contentDescription = "edit",
                testTag = "EDIT_BUTTON",
                modifier = Modifier
                    .clickable { }
                    .weight(1f)
                    .fillMaxSize()
                    .padding(Sizing.paddings.medium),
            )
            IconBuilder(
                id = R.drawable.delete,
                contentDescription = "delete",
                testTag = "DELETE_BUTTON",
                modifier = Modifier
                    .clickable { }
                    .weight(1f)
                    .fillMaxSize()
                    .padding(Sizing.paddings.medium),
            )
        }
    }
}