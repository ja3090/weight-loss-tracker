package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import com.example.weightdojo.database.dao.ChartData
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.math.round

fun DrawScope.xAxes(
    data: List<ChartData>,
    textMeasurer: TextMeasurer,
    dimensions: ChartDimensions,
    textStyle: TextStyle
) {

    val length = data.size

    val increment = length.toFloat() / 4F

    var counter = increment

    while (counter < length) {

        val index = floor(counter)

        val x = (dimensions.width / (data.size - 1)) * index

        val formatter = DateTimeFormatter.ofPattern("MMM d")

        val text = data[index.toInt()].date.format(formatter)

        val size = textMeasurer.measure(text = text, style = textStyle).size

        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = Offset(x - (size.width / 2), 0f),
            style = textStyle
        )

        counter += increment
    }
}