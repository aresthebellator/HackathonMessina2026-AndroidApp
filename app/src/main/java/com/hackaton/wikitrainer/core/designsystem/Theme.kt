package com.hackaton.wikitrainer.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WikiBlack,
    onPrimary = WikiWhite,
    primaryContainer = WikiShuttleGray,
    onPrimaryContainer = WikiWhite,
    secondary = WikiShuttleGray,
    onSecondary = WikiWhite,
    secondaryContainer = WikiSilverSand,
    onSecondaryContainer = WikiBlack,
    tertiary = WikiOsloGray,
    onTertiary = WikiWhite,
    tertiaryContainer = WikiSilverSand,
    onTertiaryContainer = WikiBlack,
    error = WikiShuttleGray,
    onError = WikiWhite,
    errorContainer = WikiSilverSand,
    onErrorContainer = WikiBlack,
    background = WikiWhite,
    onBackground = WikiBlack,
    surface = WikiSurface,
    onSurface = WikiBlack,
    outline = WikiSilverSand,
    outlineVariant = WikiOsloGray
)

private val DarkColorScheme = darkColorScheme(
    primary = WikiWhite,
    onPrimary = WikiBlack,
    primaryContainer = WikiShuttleGray,
    onPrimaryContainer = WikiWhite,
    secondary = WikiSilverSand,
    onSecondary = WikiBlack,
    background = WikiBlack,
    onBackground = WikiWhite,
    surface = WikiShuttleGray,
    onSurface = WikiWhite,
    outline = WikiOsloGray,
    outlineVariant = WikiSilverSand
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
            window.statusBarColor = (if (darkTheme) WikiBlack else WikiWhite).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DuolingoTypography,
        content = content
    )
}
