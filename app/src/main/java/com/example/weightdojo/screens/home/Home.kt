package com.example.weightdojo.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.StatsDisplay
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import java.time.LocalDate
import kotlin.random.Random

const val TEST_TAG = "Home screen"

@SuppressLint("NewApi")
@Composable
fun Home() {
    val todayFullDate = LocalDate.now()

    Column {

        Heading(text = "Home", modifier = Modifier.padding(horizontal = Sizing.paddings.small))

        DayPicker(todayFullDate = todayFullDate)

        StatsDisplay()
        Box(
            modifier = Modifier
//                .padding(horizontal = Sizing.paddings.small)
                .clip(
                    shape = RoundedCornerShape(
                        Sizing.cornerRounding,
                        Sizing.cornerRounding,
                        0.dp,
                        0.dp
                    )
                )
                .background(color = MaterialTheme.colors.secondary)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Heading(text = "Meals")
                Divider(
                    color = MaterialTheme.colors.secondaryVariant,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
                )
                repeat(3) {
                    val randomKcal = (it + 1) * Random.nextInt(500)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Sizing.paddings.medium),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextDefault(text = "Meal #$it")
                        TextDefault(text = randomKcal.toString() + "kcal")
                    }
                }
            }
        }
    }
}
