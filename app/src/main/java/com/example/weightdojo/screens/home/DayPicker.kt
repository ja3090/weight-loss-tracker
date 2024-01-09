package com.example.weightdojo.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun DayPicker(
    todayFullDate: LocalDate,
    dateSetter: (date: LocalDate) -> Unit
) {
    val offset = rememberScrollState()

    val year = todayFullDate.format(DateTimeFormatter.ofPattern("yy"))
    val month = todayFullDate.format(DateTimeFormatter.ofPattern("MMMM"))

    Box {

        Column(
            Modifier
                .horizontalScroll(offset),
        ) {
            LaunchedEffect(todayFullDate) {
                launch {
                    offset.animateScrollTo(
                        offset.maxValue / 2
                    )
                }
            }

            Row {
                var date = todayFullDate.minusDays(3L)
                var compareDates = date.compareTo(todayFullDate.plusDays(4L))

                while (compareDates < 0) {
                    val thisButtonDate = date
                    val dayOfMonth = date.dayOfMonth.toString()
                    val day = date.format(DateTimeFormatter.ofPattern("EEE"))
                    val today = dayOfMonth == todayFullDate.dayOfMonth.toString()

                    Column(
                        Modifier
                            .clickable { dateSetter(thisButtonDate) }
                            .padding(horizontal = 10.dp)
                            .clip(shape = RoundedCornerShape(Sizing.cornerRounding))
                            .background(
                                if (today) {
                                    MaterialTheme.colors.primaryVariant
                                } else {
                                    MaterialTheme.colors.background
                                }
                            )
                            .width(50.dp)
                            .height(100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        if (today) {
                            TextDefault(color = MaterialTheme.colors.secondary, text = dayOfMonth)
                            TextDefault(color = MaterialTheme.colors.secondary, text = day)
                        } else {
                            TextDefault(text = dayOfMonth, modifier = Modifier.alpha(0.6f))
                            TextDefault(text = day, modifier = Modifier.alpha(0.6f))
                        }
                    }

                    date = date.plusDays(1L)
                    compareDates = date.compareTo(todayFullDate.plusDays(4L))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        TextDefault(
            text = "$month $year",
            modifier = Modifier
                .padding(vertical = Sizing.paddings.medium)
                .alpha(0.6f)
        )
    }
}

