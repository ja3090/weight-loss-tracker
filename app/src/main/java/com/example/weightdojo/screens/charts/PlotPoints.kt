package com.example.weightdojo.screens.charts

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.example.weightdojo.datatransferobjects.ChartData
import com.example.weightdojo.utils.returnNeatDate
import kotlin.math.ceil
import kotlin.math.floor

fun plotPoints(
    data: List<ChartData>,
    chartDimensions: ChartDimensions,
    points: List<MutableState<HighlightablePoint?>>,
    upperLowerBound: Int
): PlotPointsDTO {

    var lastPoint: Offset? = null
    var firstPoint: Offset? = null
    val line = Path()
    val shape = Path()
    var min = 0F
    var max = 0F

    if (chartDimensions.height == 0 || chartDimensions.width == 0) {
        return PlotPointsDTO(line, shape, min, max)
    }

    for (index in data.indices) {
        val row = data[index]

        if (row.min == null || row.value == null || row.max == null) {
            continue
        }

        max = ceil((row.max + upperLowerBound) / upperLowerBound) * upperLowerBound
        min = floor((row.min - upperLowerBound) / upperLowerBound) * upperLowerBound

        if (min < 0) min = 0F

        val total = max - min
        val x = ((chartDimensions.width / (data.size - 1)) * index).toFloat()

        val y =
            chartDimensions.height - (chartDimensions.height * ((row.value - min) / total))

        if (lastPoint != null) {
            buildCurveLine(line, lastPoint, Offset(x, y))
            buildCurveLine(shape, lastPoint, Offset(x, y))
        }

        if (lastPoint == null) {
            firstPoint = Offset(x, y)
            shape.moveTo(x, y)
            shape.lineTo(x, y)
            line.moveTo(x, y)
        }

        lastPoint = Offset(x, y)

        points[index].value =
            HighlightablePoint(
                offset = Offset(x, y),
                value = row.value.toInt().toString(),
                date = returnNeatDate(row.date)
            )
    }

    shape.lineTo(lastPoint?.x ?: 0f, chartDimensions.height.toFloat())
    shape.lineTo(firstPoint?.x ?: 0f, chartDimensions.height.toFloat())
    shape.lineTo(firstPoint?.x ?: 0f, firstPoint?.y ?: 0f)

    return PlotPointsDTO(line, shape, min, max)
}

data class PlotPointsDTO(
    val line: Path,
    val shape: Path,
    val min: Float,
    val max: Float
)

private fun buildCurveLine(path: Path, startPoint: Offset, endPoint: Offset) {
    val firstControlPoint = Offset(
        x = startPoint.x + (endPoint.x - startPoint.x) / 2F,
        y = startPoint.y,
    )
    val secondControlPoint = Offset(
        x = startPoint.x + (endPoint.x - startPoint.x) / 2F,
        y = endPoint.y,
    )
    path.cubicTo(
        x1 = firstControlPoint.x,
        y1 = firstControlPoint.y,
        x2 = secondControlPoint.x,
        y2 = secondControlPoint.y,
        x3 = endPoint.x,
        y3 = endPoint.y,
    )
}