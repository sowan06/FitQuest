package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBlue
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRedSoft
import com.fitquest.app.ui.theme.ColorText

@Composable
fun MacroPieChart(
    proteinG: Float,
    carbsG: Float,
    fatG: Float,
    calories: Float,
    modifier: Modifier = Modifier,
) {
    val sum = (proteinG + carbsG + fatG).coerceAtLeast(0.0001f)
    val pPct = proteinG / sum
    val cPct = carbsG / sum
    val fPct = fatG / sum

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(140.dp)) {
                val stroke = 22f
                val pad = stroke / 2f + 2f
                val rectSize = Size(size.width - 2 * pad, size.height - 2 * pad)
                val topLeft = Offset(pad, pad)
                var start = -90f
                drawArc(ColorBlue, start, pPct * 360f, false, topLeft, rectSize, style = Stroke(stroke))
                start += pPct * 360f
                drawArc(ColorRedSoft, start, cPct * 360f, false, topLeft, rectSize, style = Stroke(stroke))
                start += cPct * 360f
                drawArc(ColorAccent, start, fPct * 360f, false, topLeft, rectSize, style = Stroke(stroke))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TOTAL", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                Text(
                    text = calories.toInt().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = ColorText,
                )
            }
        }
        Spacer(Modifier.size(10.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            LegendDot(ColorBlue, "PRO")
            Spacer(Modifier.width(12.dp))
            LegendDot(ColorRedSoft, "CARB")
            Spacer(Modifier.width(12.dp))
            LegendDot(ColorAccent, "FAT")
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(10.dp)
                .background(color),
        )
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = ColorText)
    }
}
