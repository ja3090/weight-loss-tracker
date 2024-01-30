package com.example.weightdojo.screens.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.ui.Sizing
import com.example.weightdojo.utils.returnNeatDate
import java.time.LocalDate
import kotlin.math.floor

data class ChartDimensions(
    val width: Int = 0, val height: Int = 0
)

@Composable
fun LineChart(
    chartViewModel: BaseChartVM,
    upperLowerBound: Int,
    title: String,
    unit: String,
    chartState: ChartState = chartViewModel.chartState,
    lineColor: Color = MaterialTheme.colors.primaryVariant,
    convertValues: (value: Float) -> Float
) {
    if (chartState.data == null || chartState.data.size <= 1) return

    var highlightedDataPoint by remember {
        mutableStateOf<HighlightablePoint?>(null)
    }

    var dimensions by remember {
        mutableStateOf(ChartDimensions())
    }

    val points = remember(chartState.data.size) {
        List(chartState.data.size) { mutableStateOf<HighlightablePoint?>(null) }
    }

    val (line, shape, min, max) = remember(chartState.data, dimensions, points) {
        plotPoints(chartState.data, dimensions, points, upperLowerBound, convertValues)
    }

    val textMeasurer = rememberTextMeasurer()

    val textStyle = TextStyle(
        color = MaterialTheme.colors.primary.copy(0.85f),
        fontSize = 12.sp
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Sizing.paddings.small)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading(
                text = if (highlightedDataPoint?.value != null) {
                    highlightedDataPoint?.value + " " + unit
                } else title
            )
            TextDefault(
                text = highlightedDataPoint?.date ?: returnNeatDate(LocalDate.now()),
                modifier = Modifier.padding(bottom = Sizing.paddings.medium)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(top = Sizing.paddings.medium)
        ) {
            Canvas(modifier = Modifier
                .weight(0.9f)
                .fillMaxSize()
                .pointerInput(points) {
                    detectDragGesturesAfterLongPress(onDragStart = { offset ->
                        var index = floor((offset.x / dimensions.width) * points.size)

                        if (index < 0) index = 0F
                        if (index >= points.size) index = (points.size - 1).toFloat()

                        highlightedDataPoint = points[index.toInt()].value
                    }, onDragEnd = {
                        highlightedDataPoint = null
                    }, onDrag = { change, _ ->
                        var index = floor((change.position.x / dimensions.width) * points.size)

                        if (index < 0) index = 0F
                        if (index >= points.size) index = (points.size - 1).toFloat()

                        highlightedDataPoint = points[index.toInt()].value
                    })
                }
                .onGloballyPositioned {
                    dimensions = dimensions.copy(
                        height = it.size.height, width = it.size.width
                    )
                }, onDraw = {
                if (highlightedDataPoint != null) {

                    drawCircle(
                        color = lineColor,
                        radius = 20f,
                        center = highlightedDataPoint?.offset ?: Offset(-15F, -15F)
                    )

                    drawLine(
                        start = Offset(
                            highlightedDataPoint?.offset?.x ?: -25F, 0F
                        ),
                        end = Offset(
                            highlightedDataPoint?.offset?.x ?: -25F, dimensions.height.toFloat()
                        ),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0f),
                        color = lineColor
                    )
                }
                val gradient = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(0.35f), lineColor.copy(0f)),
                    startY = 0F,
                    endY = dimensions.height.toFloat()
                )

                drawPath(path = shape, brush = gradient)
                drawPath(path = line, color = lineColor, style = Stroke(3f))

                xAxes(
                    data = chartState.data,
                    textMeasurer = textMeasurer,
                    dimensions = dimensions,
                    textStyle = textStyle
                )

                plotYAxes(min, max, textMeasurer, dimensions, textStyle, unit)

            })
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            PeriodPicker(chartViewModel = chartViewModel)
        }
    }
}

data class HighlightablePoint(
    val offset: Offset, val value: String, val date: String
)

