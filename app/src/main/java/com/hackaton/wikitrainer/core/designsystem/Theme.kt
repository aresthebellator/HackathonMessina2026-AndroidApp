package com.hackaton.wikitrainer.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DuoGreen,
    onPrimary = DuoBackground,
    primaryContainer = DuoGreenLight,
    onPrimaryContainer = DuoGreenDark,
    secondary = DuoBlue,
    onSecondary = DuoBackground,
    secondaryContainer = DuoBlueLight,
    onSecondaryContainer = DuoBlueDark,
    tertiary = DuoYellow,
    onTertiary = DuoInk,
    tertiaryContainer = DuoYellowLight,
    onTertiaryContainer = DuoYellowDark,
    error = DuoRed,
    onError = DuoBackground,
    errorContainer = DuoRedLight,
    onErrorContainer = DuoRedDark,
    background = DuoBackground,
    onBackground = DuoInk,
    surface = DuoSurface,
    onSurface = DuoInk,
    outline = DuoBorder,
    outlineVariant = DuoBorderDark
)

private val DarkColorScheme = darkColorScheme(
    primary = DuoGreen,
    onPrimary = DuoInk,
    primaryContainer = Color(0xFF245A12),
    onPrimaryContainer = DuoGreenLight,
    secondary = DuoBlue,
    onSecondary = DuoInk,
    background = Color(0xFF131F24),
    onBackground = Color(0xFFF7F7F7),
    surface = Color(0xFF1E2D34),
    onSurface = Color(0xFFF7F7F7),
    outline = Color(0xFF37464F)
)

@Composable
fun WikiTrainerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DuoBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DuolingoTypography,
        content = content
    )
}
