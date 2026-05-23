package com.fitquest.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorRed
import com.fitquest.app.ui.theme.ColorShadow
import com.fitquest.app.ui.theme.ColorText

enum class PixelButtonVariant { Primary, Success, Danger, Ghost }

/**
 * Pixel button. On press: shifts +2dp,+2dp and shrinks shadow by 2dp.
 * Min height 48dp for accessibility.
 */
@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PixelButtonVariant = PixelButtonVariant.Primary,
    fillWidth: Boolean = true,
) {
    val (bg, fg, border) = when (variant) {
        PixelButtonVariant.Primary -> Triple(ColorAccent, Color(0xFF3D2F00), ColorBorder)
        PixelButtonVariant.Success -> Triple(ColorGreen, Color.White, ColorBorder)
        PixelButtonVariant.Danger -> Triple(ColorRed, Color.White, ColorBorder)
        PixelButtonVariant.Ghost -> Triple(Color.Transparent, ColorText, ColorBorder)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shadowSize = if (isPressed) 2.dp else 4.dp
    val translate = if (isPressed) 2.dp else 0.dp
    val effectiveBg = if (!enabled) bg.copy(alpha = 0.4f) else bg

    val baseModifier = (if (fillWidth) modifier.fillMaxWidth() else modifier)
        .padding(end = 4.dp, bottom = 4.dp)
        .defaultMinSize(minHeight = 48.dp)

    Box(
        modifier = baseModifier
            .drawBehind {
                val o = shadowSize.toPx()
                drawRect(
                    color = ColorShadow,
                    topLeft = Offset(o, o),
                    size = Size(size.width, size.height),
                )
            }
            .padding(start = translate, top = translate)
            .background(effectiveBg)
            .border(BorderStroke(2.dp, border), RectangleShape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides fg) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = fg,
            )
        }
    }
}
