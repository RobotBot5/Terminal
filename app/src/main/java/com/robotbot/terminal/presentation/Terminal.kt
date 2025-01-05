package com.robotbot.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.robotbot.terminal.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {

    var terminalState by rememberTerminalState(bars)

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        val visibleBarCount = (terminalState.visibleBarCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)

        val scrolledBy = (terminalState.scrolledBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(bars.size * terminalState.barWidth - terminalState.terminalWidth)

        terminalState = terminalState.copy(
            visibleBarCount = visibleBarCount,
            scrolledBy = scrolledBy
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp, bottom = 64.dp)
            .transformable(transformableState)
            .onSizeChanged { terminalState = terminalState.copy(terminalWidth = it.width.toFloat()) }
    ) {
        val max = terminalState.visibleBars.maxOf { it.high }
        val min = terminalState.visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (max - min)
        translate(left = terminalState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - index * terminalState.barWidth
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                    strokeWidth = 1f
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - min) * pxPerPoint)),
                    strokeWidth = terminalState.barWidth / 2
                )
            }
        }
        bars.firstOrNull()?.let {
            drawPrices(
                min = min,
                pxPerPoint = pxPerPoint,
                lastPrice = it.close
            )
        }
    }
}

private fun DrawScope.drawPrices(
    min: Float,
    pxPerPoint: Float,
    lastPrice: Float
) {
    //max
    drawDashedLine(
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f)
    )

    // last price
    drawDashedLine(
        start = Offset(0f, size.height - ((lastPrice - min) * pxPerPoint)),
        end = Offset(size.width, size.height - ((lastPrice - min) * pxPerPoint))
    )

    //min
    drawDashedLine(
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height)
    )
}

private fun DrawScope.drawDashedLine(
    start: Offset,
    end: Offset,
    color: Color = Color.White,
    strokeWidth: Float = 1f
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                4.dp.toPx(), 4.dp.toPx()
            )
        )
    )
}