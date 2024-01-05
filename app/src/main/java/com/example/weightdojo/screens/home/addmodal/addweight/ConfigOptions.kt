package com.example.weightdojo.screens.home.addmodal.addweight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.screens.home.addmodal.addweight.height.HeightInput


@Composable
fun ConfigOptions(extraOptions: ExtraOptions) {

    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 50.dp)
                .padding(Sizing.paddings.medium)
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(
                    if (extraOptions.sex == Sex.Male) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        MaterialTheme.colors.background
                    }
                )
                .border(
                    width = 2.dp,
                    color = if (extraOptions.sex == Sex.Male) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        Color.Transparent
                    }, shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    if (extraOptions.sex == Sex.Male) {
                        extraOptions.sex = null
                    } else {
                        extraOptions.sex = Sex.Male
                    }
                },
        ) {

            TextDefault(
                text = "Male",
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .widthIn(min = 80.dp),
                color = if (extraOptions.sex == Sex.Male) {
                    MaterialTheme.colors.background
                } else {
                    MaterialTheme.colors.primary
                }
            )
        }
        Box(
            modifier = Modifier
                .padding(Sizing.paddings.medium)
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(
                    if (extraOptions.sex == Sex.Female) {
                        MaterialTheme.colors.primaryVariant
                    } else {
                        MaterialTheme.colors.background
                    }
                )
                .border(
                    width = 2.dp,
                    color = if (extraOptions.sex == Sex.Female) {
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
        ) {
            TextDefault(
                text = "Female",
                modifier = Modifier
                    .padding(Sizing.paddings.medium)
                    .widthIn(min = 80.dp),
                color = if (extraOptions.sex == Sex.Female) {
                    MaterialTheme.colors.background
                } else {
                    MaterialTheme.colors.primary
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(bottom = Sizing.paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = Sizing.paddings.medium)
                .clip(RoundedCornerShape(Sizing.cornerRounding))
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            TextDefault(
                text = "Age",
                modifier = Modifier
                    .padding(top = Sizing.paddings.small)
            )
            TextField(
                value = if (extraOptions.age == null) "" else extraOptions.age.toString(),
                onValueChange = {
                    if ((it.isDigitsOnly() && it.count() <= 2) || it.isEmpty()) {
                        extraOptions.age = it
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary, backgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(Sizing.paddings.small)
                    .wrapContentWidth(),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

        }

        HeightInput(extraOptions = extraOptions)
    }
}