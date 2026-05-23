package com.fitquest.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorShadow
import com.fitquest.app.ui.theme.ColorSurface

/**
 * Pixel card: solid surface, hard 2dp border, 4px-offset hard shadow.
 * No corner radius. The shadow is drawn behind the content via drawBehind so
 * layout dimensions stay clean.
 */
@Composable
fun PixelCard(
    modifier: Modifier = Modifier,
    background: Color = ColorSurface,
    border: Color = ColorBorder,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = ColorShadow,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = shadowOffset, bottom = shadowOffset)
            .drawBehind {
                val offsetPx = shadowOffset.toPx()
                drawRect(
                    color = shadowColor,
                    topLeft = Offset(offsetPx, offsetPx),
                    size = Size(size.width, size.height),
                )
            }
            .background(background)
            .border(BorderStroke(borderWidth, border), RectangleShape)
            .padding(contentPadding),
    ) {
        content()
    }
}
