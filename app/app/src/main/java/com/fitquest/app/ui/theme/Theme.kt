package com.fitquest.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import android.app.Activity

private val FitQuestColorScheme = darkColorScheme(
    primary = ColorAccent,
    onPrimary = Color(0xFF3D2F00),
    primaryContainer = ColorAccent,
    onPrimaryContainer = Color(0xFF695200),
    secondary = ColorGreen,
    onSecondary = Color(0xFF00390A),
    secondaryContainer = Color(0xFF00761F),
    onSecondaryContainer = Color(0xFF95FB92),
    tertiary = ColorRedSoft,
    onTertiary = Color(0xFF690002),
    error = ColorRed,
    onError = Color(0xFFFFFFFF),
    background = ColorBgDeep,
    onBackground = ColorText,
    surface = ColorSurface,
    onSurface = ColorText,
    surfaceVariant = ColorSurfaceHigh,
    onSurfaceVariant = ColorTextDim,
    outline = ColorBorder,
    outlineVariant = ColorBorderDim,
)

@Composable
fun FitQuestTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as? Activity)?.window?.let { window ->
                window.statusBarColor = ColorBgDeep.toArgb()
                window.navigationBarColor = ColorBgDeep.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = FitQuestColorScheme,
        typography = FitQuestTypography,
        shapes = PixelShapes,
        content = content,
    )
}
