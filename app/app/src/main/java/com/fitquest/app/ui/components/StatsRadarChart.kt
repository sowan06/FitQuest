package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorMuted
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StatsRadarChart(
    str: Int,
    vit: Int,
    end: Int,
    wis: Int,
    con: Int,
    maxValue: Int = 50,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 10.sp, color = ColorMuted)
    val values = listOf(str, vit, end, wis, con).map {
        (it.coerceAtLeast(0).toFloat() / maxValue.coerceAtLeast(1)).coerceAtMost(1f)
    }
    val labels = listOf("STR", "VIT", "END", "WIS", "CON")
    Canvas(modifier = modifier.size(180.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = (minOf(size.width, size.height) / 2f) - 22f
        val n = 5

        // grid rings
        for (ring in 1..3) {
            val r = radius * ring / 3f
            val path = Path()
            for (i in 0 until n) {
                val theta = -Math.PI / 2 + 2 * Math.PI * i / n
                val x = cx + (r * cos(theta)).toFloat()
                val y = cy + (r * sin(theta)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = ColorBorder.copy(alpha = 0.4f), style = Stroke(1.5f))
        }

        // axes
        for (i in 0 until n) {
            val theta = -Math.PI / 2 + 2 * Math.PI * i / n
            val ex = cx + (radius * cos(theta)).toFloat()
            val ey = cy + (radius * sin(theta)).toFloat()
            drawLine(ColorBorder.copy(alpha = 0.5f), Offset(cx, cy), Offset(ex, ey), strokeWidth = 1.5f)
        }

        // value polygon
        val valuePath = Path()
        for (i in 0 until n) {
            val theta = -Math.PI / 2 + 2 * Math.PI * i / n
            val r = radius * values[i]
            val x = cx + (r * cos(theta)).toFloat()
            val y = cy + (r * sin(theta)).toFloat()
            if (i == 0) valuePath.moveTo(x, y) else valuePath.lineTo(x, y)
        }
        valuePath.close()
        drawPath(valuePath, color = ColorAccent.copy(alpha = 0.5f))
        drawPath(valuePath, color = ColorAccent, style = Stroke(2f))

        // labels
        for (i in 0 until n) {
            val theta = -Math.PI / 2 + 2 * Math.PI * i / n
            val lr = radius + 14f
            val lx = cx + (lr * cos(theta)).toFloat()
            val ly = cy + (lr * sin(theta)).toFloat()
            val layout = measurer.measure(AnnotatedString(labels[i]), labelStyle)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(lx - layout.size.width / 2f, ly - layout.size.height / 2f),
            )
        }
    }
}
