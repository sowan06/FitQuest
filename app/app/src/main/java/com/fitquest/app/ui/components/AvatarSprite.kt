package com.fitquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBlue
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorPurple
import com.fitquest.app.ui.theme.ColorRed

private typealias Sprite = Array<IntArray>

/**
 * Pixel-art avatar sprites drawn entirely from Compose Canvas.
 * 16x16 grids; each cell scales to fill the requested size.
 *
 * Color palette index:
 *  0 transparent
 *  1 skin
 *  2 gold (helmet/aura)
 *  3 armor green
 *  4 armor blue
 *  5 armor red
 *  6 dark armor / outline
 *  7 white highlight
 *  8 purple cape
 */
@Composable
fun AvatarSprite(stage: Int, modifier: Modifier = Modifier, size: Dp = 64.dp) {
    val sprite = when (stage) {
        0 -> rookieSprite()
        1 -> fighterSprite()
        2 -> warriorSprite()
        else -> legendSprite()
    }
    val palette = listOf(
        Color.Transparent,
        Color(0xFFFFD8A8), // skin
        ColorAccent,        // gold
        ColorGreen,         // armor green
        ColorBlue,          // armor blue
        ColorRed,           // armor red
        Color(0xFF1F1F33),  // dark armor / outline
        Color.White,        // highlight
        ColorPurple,        // purple cape
    )
    Canvas(
        modifier = modifier
            .size(size)
            .background(ColorBgDeep)
            .border(2.dp, ColorBorder, RectangleShape),
    ) {
        val rows = sprite.size
        val cols = sprite[0].size
        val cellW = this.size.width / cols
        val cellH = this.size.height / rows
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val idx = sprite[r][c]
                if (idx == 0) continue
                drawRect(
                    color = palette[idx],
                    topLeft = Offset(c * cellW, r * cellH),
                    size = Size(cellW + 0.5f, cellH + 0.5f),
                )
            }
        }
    }
}

// 16x16 grids — keep readable spacing.
private fun rookieSprite(): Sprite = arrayOf(
    intArrayOf(0,0,0,0,0,6,6,6,6,6,0,0,0,0,0,0),
    intArrayOf(0,0,0,0,6,1,1,1,1,1,6,0,0,0,0,0),
    intArrayOf(0,0,0,6,1,1,1,1,1,1,1,6,0,0,0,0),
    intArrayOf(0,0,0,6,1,6,1,1,1,6,1,6,0,0,0,0),
    intArrayOf(0,0,0,6,1,1,1,1,1,1,1,6,0,0,0,0),
    intArrayOf(0,0,0,0,6,1,1,1,1,1,6,0,0,0,0,0),
    intArrayOf(0,0,0,0,0,6,6,6,6,6,0,0,0,0,0,0),
    intArrayOf(0,0,0,0,3,3,3,3,3,3,3,0,0,0,0,0),
    intArrayOf(0,0,0,3,3,3,3,3,3,3,3,3,0,0,0,0),
    intArrayOf(0,0,3,3,3,3,3,3,3,3,3,3,3,0,0,0),
    intArrayOf(0,0,3,3,3,3,3,3,3,3,3,3,3,0,0,0),
    intArrayOf(0,0,3,3,3,3,3,3,3,3,3,3,3,0,0,0),
    intArrayOf(0,0,3,3,3,0,0,0,0,0,3,3,3,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
)

private fun fighterSprite(): Sprite = arrayOf(
    intArrayOf(0,0,0,0,2,2,2,2,2,2,0,0,0,0,0,0),
    intArrayOf(0,0,0,2,2,1,1,1,1,2,2,0,0,0,0,0),
    intArrayOf(0,0,0,2,1,1,1,1,1,1,2,0,0,0,0,0),
    intArrayOf(0,0,0,6,1,6,1,1,1,6,1,6,0,0,0,0),
    intArrayOf(0,0,0,6,1,1,1,1,1,1,1,6,0,0,0,0),
    intArrayOf(0,0,0,0,6,1,1,1,1,1,6,0,0,0,0,0),
    intArrayOf(0,0,0,0,0,6,6,6,6,6,0,0,0,0,0,0),
    intArrayOf(0,0,0,4,4,4,4,4,4,4,4,0,0,0,0,0),
    intArrayOf(0,0,4,4,4,4,4,4,4,4,4,4,0,0,0,0),
    intArrayOf(0,2,4,4,4,4,4,4,4,4,4,4,2,0,0,0),
    intArrayOf(0,2,4,4,4,2,4,4,4,2,4,4,2,0,0,0),
    intArrayOf(0,2,4,4,4,4,4,4,4,4,4,4,2,0,0,0),
    intArrayOf(0,0,4,4,4,0,0,0,0,0,4,4,4,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
)

private fun warriorSprite(): Sprite = arrayOf(
    intArrayOf(0,0,0,2,2,2,2,2,2,2,2,0,0,0,0,0),
    intArrayOf(0,0,2,2,2,2,2,2,2,2,2,2,0,0,0,0),
    intArrayOf(0,0,2,2,1,1,1,1,1,1,2,2,0,0,0,0),
    intArrayOf(0,0,6,1,1,6,1,1,1,6,1,6,0,0,0,0),
    intArrayOf(0,0,6,1,1,1,1,1,1,1,1,6,0,0,0,0),
    intArrayOf(0,0,0,6,1,1,1,1,1,1,6,0,0,0,0,0),
    intArrayOf(0,0,0,0,6,6,6,6,6,6,0,0,0,0,0,0),
    intArrayOf(0,2,5,5,5,5,5,5,5,5,5,5,2,0,0,0),
    intArrayOf(0,2,5,5,5,5,5,5,5,5,5,5,2,0,0,0),
    intArrayOf(0,2,5,5,5,5,2,2,5,5,5,5,2,0,0,0),
    intArrayOf(0,2,5,5,5,2,2,2,2,5,5,5,2,0,0,0),
    intArrayOf(0,0,5,5,5,5,5,5,5,5,5,5,0,0,0,0),
    intArrayOf(0,0,5,5,5,0,0,0,0,0,5,5,5,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
)

private fun legendSprite(): Sprite = arrayOf(
    intArrayOf(2,0,0,2,2,2,2,2,2,2,2,0,0,2,0,0),
    intArrayOf(0,2,2,2,2,2,7,7,2,2,2,2,2,0,0,0),
    intArrayOf(0,0,2,2,1,1,1,1,1,1,2,2,0,0,0,0),
    intArrayOf(0,0,2,1,7,6,1,1,1,6,7,2,0,0,0,0),
    intArrayOf(0,0,6,1,1,1,1,1,1,1,1,6,0,0,0,0),
    intArrayOf(0,0,0,6,1,1,1,1,1,1,6,0,0,0,0,0),
    intArrayOf(0,0,0,0,6,6,6,6,6,6,0,0,0,0,0,0),
    intArrayOf(8,2,5,2,5,2,5,5,2,5,2,5,2,8,0,0),
    intArrayOf(8,2,5,5,5,5,5,5,5,5,5,5,2,8,0,0),
    intArrayOf(8,2,5,5,5,5,2,2,5,5,5,5,2,8,0,0),
    intArrayOf(8,2,5,5,5,2,7,7,2,5,5,5,2,8,0,0),
    intArrayOf(8,0,5,5,5,5,5,5,5,5,5,5,0,8,0,0),
    intArrayOf(0,0,5,5,5,0,0,0,0,0,5,5,5,0,0,0),
    intArrayOf(0,0,2,6,2,0,0,0,0,0,2,6,2,0,0,0),
    intArrayOf(0,0,6,6,6,0,0,0,0,0,6,6,6,0,0,0),
    intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
)
