package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.weightdojo.database.dao.RangeDataByDay
import kotlin.math.ceil
import kotlin.math.floor

data class ChartDimensions(
    val width: Int = 0,
    val height: Int = 0
)

@Composable
fun Chart(
    chartState: ChartState,
    lineColor: Color = MaterialTheme.colors.primaryVariant,
) {
    if (chartState.data == null || chartState.data.size <= 1) return

    var highlightedDataPoint by remember {
        mutableStateOf<Offset?>(null)
    }

    var dimensions by remember {
        mutableStateOf(ChartDimensions())
    }

    val points = remember { List(chartState.data.size) { mutableStateOf<Offset?>(null) } }

    val (line, shape) = remember(chartState.data, dimensions) {
        plotPoints(chartState.data, dimensions, points)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
//                .padding(vertical = 50.dp)
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            var index =
                                floor((offset.x / dimensions.width!!) * points.size)

                            if (index < 0) index = 0F
                            if (index >= points.size) index = (points.size - 1).toFloat()

                            highlightedDataPoint = points[index.toInt()].value
                        },
                        onDragEnd = {
                            highlightedDataPoint = null
                        },
                        onDrag = { change, _ ->
                            var index =
                                floor((change.position.x / dimensions.width!!) * points.size)

                            if (index < 0) index = 0F
                            if (index >= points.size) index = (points.size - 1).toFloat()

                            highlightedDataPoint = points[index.toInt()].value
                        }
                    )
                }
                .onGloballyPositioned {
                    dimensions = dimensions.copy(
                        height = it.size.height,
                        width = it.size.width
                    )
                },
                onDraw = {
                    if (highlightedDataPoint != null) {

                        drawCircle(
                            color = lineColor,
                            radius = 20f,
                            center = highlightedDataPoint ?: Offset(-15F, -15F)
                        )

                        drawLine(
                            start = Offset(
                                highlightedDataPoint?.x ?: -25F,
                                0F
                            ), end = Offset(
                                highlightedDataPoint?.x ?: -25F,
                                dimensions.height?.toFloat() ?: -25F
                            ), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0f),
                            color = lineColor
                        )
                    }
                    val gradient = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to lineColor,
                            0.1f to lineColor.copy(0.5f),
                            1f to lineColor.copy(0f)
                        ),
                        startY = 0F,
                        endY = dimensions.height.toFloat()
                    )

                    drawPath(path = shape, brush = gradient)
                    drawPath(path = line, color = lineColor)
                })
        }
        Box(modifier = Modifier.weight(1f)) {
            Text("Filler")
        }
    }
}

private fun plotPoints(
    data: List<RangeDataByDay>,
    chartDimensions: ChartDimensions,
    points: List<MutableState<Offset?>>
): Pair<Path, Path> {

    var lastPoint: Offset? = null
    val line = Path()
    val shape = Path()

    if (chartDimensions.height == 0 || chartDimensions.width == 0) {
        return Pair(line, shape)
    }

    for (index in data.indices) {
        val row = data[index]

        if (row.minWeight == null || row.weight == null || row.maxWeight == null) {
            continue
        }

        val max = ceil((row.maxWeight + 10) / 10) * 10
        var min = floor((row.minWeight - 10) / 10) * 10

        if (min < 0) min = 0F

        val total = max - min
        val x = ((chartDimensions.width / (data.size - 1)) * index).toFloat()

        val y =
            chartDimensions.height - (chartDimensions.height * ((row.weight - min) / total))

        if (lastPoint != null) {
            buildCurveLine(line, lastPoint, Offset(x, y))
            buildCurveLine(shape, lastPoint, Offset(x, y))
        }

        if (lastPoint == null) {
            shape.moveTo(0F, 0F)
            shape.lineTo(x, y)
            line.moveTo(x, y)
        }

        if (index == data.size - 1) {
            shape.lineTo(chartDimensions.width.toFloat(), chartDimensions.height.toFloat())
            shape.lineTo(0F, chartDimensions.height.toFloat())
        }

        lastPoint = Offset(x, y)

        points[index].value = Offset(x, y)
    }

    return Pair(line, shape)
}

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

