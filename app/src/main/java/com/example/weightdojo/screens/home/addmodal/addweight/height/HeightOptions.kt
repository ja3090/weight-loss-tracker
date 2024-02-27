package com.example.weightdojo.screens.home.addmodal.addweight.height

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.addmodal.addweight.height.HeightUnits

@Composable
fun HeightOptions(
    heightInputOptions: HeightInputOptions
) {

    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(modifier = Modifier
            .clickable { heightInputOptions.update(newMenuOpen = true) }
        ) {

            TextDefault(
                text = heightInputOptions.heightUnit.name,
                modifier = Modifier
                    .width(30.dp)
                    .wrapContentHeight(),
                textAlign = TextAlign.Center
            )
            IconBuilder(
                id = R.drawable.arrow_down,
                contentDescription = "Drop down icon",
                testTag = "DROP_DOWN"
            )
        }
        DropdownMenu(
            expanded = heightInputOptions.menuOpen,
            onDismissRequest = { heightInputOptions.update(newMenuOpen = false) },
            modifier = Modifier.width(30.dp)
        ) {
            when (heightInputOptions.heightUnit) {
                HeightUnits.IN -> TextDefault(text = "CM",
                    modifier = Modifier
                        .clickable {
                            heightInputOptions.update(
                                newMenuOpen = false,
                                newHeightUnit = HeightUnits.CM
                            )
                        }
                        .fillMaxSize()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center)

                HeightUnits.CM -> TextDefault(text = "IN",
                    modifier = Modifier
                        .clickable {
                            heightInputOptions.update(
                                newMenuOpen = false,
                                newHeightUnit = HeightUnits.IN
                            ) }
                        .fillMaxSize()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center)
            }
        }
    }
}