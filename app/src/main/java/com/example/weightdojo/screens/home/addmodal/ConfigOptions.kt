package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.ui.Sizing

@Composable
fun ConfigOptions(extraOptions: ExtraOptions) {

    Row {
        TextDefault(
            modifier = Modifier
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(MaterialTheme.colors.background)
                .padding(Sizing.paddings.medium)
                .border(
                    width = 2.dp,
                    color = if (extraOptions.sex == Sex.Male) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    if (extraOptions.sex == Sex.Male) {
                        extraOptions.sex = null
                    } else {
                        extraOptions.sex = Sex.Male
                    }
                },
            text = "Male"
        )
        TextDefault(
            modifier = Modifier
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(MaterialTheme.colors.background)
                .padding(Sizing.paddings.medium)
                .border(
                    width = 2.dp,
                    color = if (extraOptions.sex == Sex.Male) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    if (extraOptions.sex == Sex.Female) {
                        extraOptions.sex = null
                    } else {
                        extraOptions.sex = Sex.Female
                    }
                },
            text = "Female"
        )
    }

    Row {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(MaterialTheme.colors.background)
                .padding(Sizing.paddings.medium),
        ) {

            TextDefault(
                text = "Age"
            )
            TextField(
                value = if (extraOptions.age == null) "" else extraOptions.age.toString(),
                onValueChange = {
                    if ((it.isDigitsOnly() && it.count() <= 2) || it.isEmpty()) {
                        extraOptions.age = it.toInt()
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    backgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(min = 10.dp),
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(MaterialTheme.colors.background)
                .padding(Sizing.paddings.medium),
        ) {

            TextDefault(
                text = "Height"
            )
            TextField(
                value = if (extraOptions.height == null) "" else extraOptions.height.toString(),
                onValueChange = {
                    val passes = validateInput(it)

                    if (passes) extraOptions.height = it.toFloat()
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    backgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(min = 10.dp),
            )
        }
    }
}