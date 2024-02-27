package com.example.weightdojo.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import kotlin.enums.EnumEntries

@Composable
fun <T : Enum<T>> SelectUnit(
    setter: (arg: Enum<T>) -> Unit,
    options: EnumEntries<T>,
    title: String,
    open: (bool: Boolean) -> Unit,
    isOpen: Boolean,
    currentSelected: Enum<T>,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable { open(true) }
            .padding(vertical = Sizing.paddings.medium)
            .fillMaxWidth()
            .then(modifier)
    ) {
        TextDefault(text = title)
        Column {
            TextDefault(
                text = currentSelected.name,
                color = MaterialTheme.colors.primary.copy(0.75f)
            )
            DropdownMenu(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxWidth(),
                expanded = isOpen,
                onDismissRequest = {
                    open(false)
                }
            ) {
                for (option in options) {
                    if (option == currentSelected) continue
                    TextDefault(
                        text = option.name,
                        modifier = Modifier
                            .clickable {
                                setter(option)
                                open(false)
                            }
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}