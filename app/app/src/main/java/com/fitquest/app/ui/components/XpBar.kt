package com.fitquest.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorAccentSoft
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorText

/**
 * Animated chunked XP bar. When `leveledUp` is true the bar pulses for ~1.5s
 * by alternating between gold and pale-gold segment colors.
 */
@Composable
fun XpBar(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier,
    leveledUp: Boolean = false,
) {
    val pct = if (max > 0) current.toFloat() / max else 0f
    val color = if (leveledUp) {
        val transition = rememberInfiniteTransition(label = "xp-pulse")
        val t by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 350, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "xp-pulse-t",
        )
        if (t > 0.5f) ColorAccentSoft else ColorAccent
    } else {
        ColorAccent
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ChunkedBar(
            progress = pct,
            color = color,
            segmentCount = 16,
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "XP",
                style = MaterialTheme.typography.labelSmall,
                color = ColorMuted,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "$current / $max",
                style = MaterialTheme.typography.labelMedium,
                color = ColorText,
            )
        }
    }
}
