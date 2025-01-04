package com.robotbot.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.robotbot.terminal.data.Bar

@Composable
fun Terminal(bars: List<Bar>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val max = bars.maxOf { it.high }
        val min = bars.minOf { it.low }
        val barWidth = size.width / bars.size
        val pxPerPoint = size.height / (max - min)
        bars.forEachIndexed { index, bar ->
            val offsetX = index * barWidth
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                strokeWidth = 1f
            )
        }
    }
}