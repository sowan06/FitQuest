package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitquest.app.data.local.dao.DailyVolume
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorMuted

@Composable
fun VolumeBarChart(
    data: List<DailyVolume>,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "NO DATA YET, HERO!",
                style = MaterialTheme.typography.titleSmall,
                color = ColorMuted,
            )
        }
        return
    }
    val measurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 9.sp, color = ColorMuted)
    val maxVal = data.maxOf { it.volumeKg }.coerceAtLeast(1.0)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
    ) {
        val padTop = 16f
        val padBottom = 28f
        val padX = 12f
        val plotW = size.width - padX * 2
        val plotH = size.height - padTop - padBottom
        val gap = 6f
        val barW = ((plotW - gap * (data.size - 1)) / data.size).coerceAtLeast(2f)

        // baseline
        drawRect(
            color = ColorMuted.copy(alpha = 0.5f),
            topLeft = Offset(padX, padTop + plotH),
            size = Size(plotW, 1f),
        )

        data.forEachIndexed { i, d ->
            val frac = (d.volumeKg / maxVal).toFloat()
            val h = plotH * frac
            val x = padX + i * (barW + gap)
            val y = padTop + plotH - h
            drawRect(color = ColorAccent, topLeft = Offset(x, y), size = Size(barW, h))
            // label: last 3 chars of YYYY-MM-DD = day
            val dayLabel = d.day.takeLast(2)
            drawTextLabel(measurer, dayLabel, Offset(x, padTop + plotH + 6f), labelStyle, barW)
        }
    }
}

private fun DrawScope.drawTextLabel(
    measurer: androidx.compose.ui.text.TextMeasurer,
    text: String,
    topLeft: Offset,
    style: TextStyle,
    width: Float,
) {
    val layout = measurer.measure(AnnotatedString(text), style)
    drawText(
        textLayoutResult = layout,
        topLeft = Offset(
            topLeft.x + (width - layout.size.width) / 2f,
            topLeft.y,
        ),
    )
}
