package com.example.weightdojo.screens.charts

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints.Companion.Infinity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weightdojo.database.dao.ChartDataDTO
import com.example.weightdojo.database.dao.RangeDataByDay
import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

data class ChartDimensions(
    val width: Int? = null,
    val height: Int? = null
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

    val points = Array<Offset?>(chartState.data.size) { null }


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
//                .pointerInput(Unit) {
//                    detectTransformGestures { centroid, _, _, _ ->
//                        var index = floor((centroid.x / dimensions.width!!) * points.size)
//                        Log.d("index", index.toString())
//                        Log.d("centroid.x", centroid.x.toString())
//
//                        if (index < 0) index = 0F
//                        if (index >= points.size) index = (points.size - 1).toFloat()
//
//                        highlightedDataPoint = points[index.toInt()]
//                    }
//                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragEnd = {
                            highlightedDataPoint = null
                        },
                        onDrag = { change, _ ->
                            var index =
                                floor((change.position.x / dimensions.width!!) * points.size)
                            Log.d("index", index.toString())
                            Log.d("centroid.x", change.position.x.toString())

                            if (index < 0) index = 0F
                            if (index >= points.size) index = (points.size - 1).toFloat()

                            highlightedDataPoint = points[index.toInt()]
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
                    }
                    plotPoints(
                        data = chartState.data,
                        chartDimensions = dimensions,
                        color = lineColor,
                        points = points,
                    )
                })
        }
        Box(modifier = Modifier.weight(1f)) {
            Text("Filler")
        }
    }
}

private fun DrawScope.plotPoints(
    data: List<RangeDataByDay>,
    chartDimensions: ChartDimensions,
    color: Color,
    points: Array<Offset?>
) {
    if (chartDimensions.height == null || chartDimensions.width == null) return

    var lastPoint: Offset? = null
    val path = Path()

    for (index in data.indices) {
        val row = data[index]

        if (row.minWeight == null || row.weight == null || row.maxWeight == null) continue

        val max = ceil((row.maxWeight + 10) / 10) * 10
        var min = floor((row.minWeight - 10) / 10) * 10

        if (min < 0) min = 0F
//        val max = row.maxWeight
//        val min = row.minWeight

        Log.d("max", max.toString())
        Log.d("min", min.toString())

        val total = max - min
        val x = ((chartDimensions.width / data.size) * index).toFloat()

        val y =
            chartDimensions.height - (chartDimensions.height * ((row.weight - min) / total))

        if (lastPoint != null) {
            buildCurveLine(path, lastPoint, Offset(x, y))
        }

        if (lastPoint == null) {
            path.moveTo(x, y)
        }

        lastPoint = Offset(x, y)

        points[index] = Offset(x, y)
    }

    drawPath(path = path, color = color, style = Stroke(3f))
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

