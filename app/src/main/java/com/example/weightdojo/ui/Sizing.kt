package com.example.weightdojo.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FontSizes(
    val heading: TextUnit = 25.sp,
    val default: TextUnit = 14.sp,
    val small: TextUnit = default * 0.8
)

data class Paddings(
    val medium: Dp = 25.dp,
    val small: Dp = 15.dp,
    val extraSmall: Dp = 7.5.dp
)

class Sizing {
    companion object {
        val cornerRounding = 25.dp
        val font = FontSizes()
        val paddings = Paddings()
    }
}