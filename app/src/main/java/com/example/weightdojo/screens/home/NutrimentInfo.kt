package com.example.weightdojo.screens.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.database.dao.NutrimentCalorieTotals
import com.example.weightdojo.datatransferobjects.NutrimentTotalsByDay
import com.example.weightdojo.screens.charts.ChartDimensions
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.toTwoDecimalPlaces
import kotlin.random.Random

@Composable
fun NutrimentInfo(nutrimentTotals: List<NutrimentTotalsByDay>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(bottom = Sizing.paddings.medium, start = Sizing.paddings.small)
    ) {
        nutrimentTotals.mapIndexed { index, it -> NutrimentTotalBar(index, it) }
    }
}

@Composable
fun NutrimentTotalBar(
    index: Int,
    total: NutrimentTotalsByDay,
) {
    val getColour = { index: Int ->
        val colourIndex = index % CustomColors.colorList.size
        CustomColors.colorList[colourIndex]
    }

    val width = 100.dp
    val colour = getColour(index)

    val percentOfTarget = when (total.dailyIntakeTarget) {
        is Float -> (total.totalGrams / total.dailyIntakeTarget)
        else -> 1f
    }

    val height = 130.dp

    Box(
        modifier = Modifier
            .padding(start = if (index == 0) 0.dp else Sizing.paddings.medium)
            .clip(RoundedCornerShape(5.dp))
            .height(height)
            .background(colour.copy(0.2f))
            .width(width),
    ) {
        TextDefault(
            text = total.nutrimentName,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    rotationZ = -90f,
                    transformOrigin = TransformOrigin(0f, 0f),
                )
                .offset(x = -height, y = 0.dp),
            textAlign = TextAlign.Left,
        )
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            TextDefault(text = "${total.totalGrams.toInt()}g", fontSize = Sizing.font.small)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxHeight()
                .width(width * 0.05f)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            Pair(1f - percentOfTarget, Color.Transparent),
                            Pair(1f - percentOfTarget, colour.copy(0.8f)),
                        ),
                    )
                )
        )
    }
}