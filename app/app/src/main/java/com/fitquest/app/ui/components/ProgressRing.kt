package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorText

/**
 * Circular progress ring drawn with drawArc.
 * Caller passes value/max and a label; we render the value in the center.
 */
@Composable
fun ProgressRing(
    value: Float,
    max: Float,
    centerLabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    bottomLabel: String? = null,
    diameter: androidx.compose.ui.unit.Dp = 80.dp,
    strokeWidthPx: Float = 16f,
) {
    val pct = if (max > 0f) (value / max).coerceIn(0f, 1f) else 0f
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(diameter)) {
                val pad = strokeWidthPx
                drawArc(
                    color = ColorBorder.copy(alpha = 0.5f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(pad, pad),
                    size = Size(size.width - 2 * pad, size.height - 2 * pad),
                    style = Stroke(width = strokeWidthPx),
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * pct,
                    useCenter = false,
                    topLeft = Offset(pad, pad),
                    size = Size(size.width - 2 * pad, size.height - 2 * pad),
                    style = Stroke(width = strokeWidthPx),
                )
            }
            Text(
                text = centerLabel,
                style = MaterialTheme.typography.titleMedium,
                color = ColorText,
            )
        }
        if (bottomLabel != null) {
            Text(
                text = bottomLabel,
                style = MaterialTheme.typography.labelSmall,
                color = ColorMuted,
            )
        }
    }
}
