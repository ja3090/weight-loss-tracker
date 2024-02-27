package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

fun DrawScope.plotYAxes(
    min: Float,
    max: Float,
    textMeasurer: TextMeasurer,
    dimensions: ChartDimensions,
    textStyle: TextStyle,
    unit: String
) {
    val total = max - min

    if (dimensions.width == 0 || dimensions.height == 0 || total == 0F) return

    val increment = total / 4

    for (i in 1..3) {
        val yValue = dimensions.height - (((i * increment) / total) * dimensions.height)

        val text = ((increment * i) + min).toInt().toString() + " $unit"

        val size = textMeasurer.measure(text = text, style = textStyle).size

        drawText(
            text = text,
            textMeasurer = textMeasurer,
            topLeft = Offset(dimensions.width.toFloat() - size.width, yValue - (size.height / 2)),
            maxLines = 1,
            style = textStyle
        )
    }
}