package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorPurple
import com.fitquest.app.ui.theme.ColorText

/**
 * Chunked pixel stat bar: [NAME] [segments] [value].
 * Segment color shifts green (0-33) → gold (34-66) → purple (67-100) of max.
 */
@Composable
fun StatBar(
    name: String,
    value: Int,
    maxValue: Int = 50,
    modifier: Modifier = Modifier,
    color: Color? = null,
) {
    val pct = (value.coerceAtLeast(0).toFloat() / maxValue.coerceAtLeast(1)).coerceAtMost(1f)
    val segColor = color ?: when {
        pct < 0.34f -> ColorGreen
        pct < 0.67f -> ColorAccent
        else -> ColorPurple
    }
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name.uppercase(),
            color = ColorText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(40.dp),
        )
        Spacer(Modifier.width(8.dp))
        ChunkedBar(
            progress = pct,
            color = segColor,
            modifier = Modifier
                .weight(1f)
                .height(14.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value.toString(),
            color = ColorText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(32.dp),
        )
    }
}

@Composable
fun ChunkedBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
    segmentCount: Int = 12,
    background: Color = ColorBgDeep,
    border: Color = ColorBorder,
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().height(14.dp)) {
            // background
            drawRect(color = background, size = size)
            // border
            val bw = 2f
            drawRect(color = border, topLeft = Offset(0f, 0f), size = Size(size.width, bw))
            drawRect(color = border, topLeft = Offset(0f, size.height - bw), size = Size(size.width, bw))
            drawRect(color = border, topLeft = Offset(0f, 0f), size = Size(bw, size.height))
            drawRect(
                color = border,
                topLeft = Offset(size.width - bw, 0f),
                size = Size(bw, size.height),
            )
            // segments
            val padding = 3f
            val gap = 2f
            val innerW = size.width - padding * 2
            val segW = (innerW - gap * (segmentCount - 1)) / segmentCount
            val filled = (progress.coerceIn(0f, 1f) * segmentCount).toInt()
            for (i in 0 until filled) {
                drawRect(
                    color = color,
                    topLeft = Offset(padding + i * (segW + gap), padding),
                    size = Size(segW, size.height - padding * 2),
                )
            }
        }
    }
}
