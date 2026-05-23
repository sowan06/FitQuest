package com.fitquest.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Sharp 0px corners everywhere — non-negotiable for the pixel aesthetic.
 *
 * Material3.Shapes slots require a CornerBasedShape, so we use
 * RoundedCornerShape(0.dp). Visually identical to RectangleShape but type-
 * compatible with the Material API. Inside our own composables we still use
 * RectangleShape directly where Shape is acceptable.
 */
private val Sharp = RoundedCornerShape(0.dp)

val PixelShapes = Shapes(
    extraSmall = Sharp,
    small = Sharp,
    medium = Sharp,
    large = Sharp,
    extraLarge = Sharp,
)
