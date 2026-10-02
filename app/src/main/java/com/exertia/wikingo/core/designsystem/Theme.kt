package com.exertia.wikingo.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat

val LocalReduceMotion = staticCompositionLocalOf { false }

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
    secondaryContainer = WikiShuttleGray,
    onSecondaryContainer = WikiWhite,
    tertiary = WikiSilverSand,
    onTertiary = WikiBlack,
    tertiaryContainer = WikiShuttleGray,
    onTertiaryContainer = WikiWhite,
    error = WikiSilverSand,
    onError = WikiBlack,
    errorContainer = WikiShuttleGray,
    onErrorContainer = WikiWhite,
    background = WikiBlack,
    onBackground = WikiWhite,
    surface = WikiShuttleGray,
    onSurface = WikiWhite,
    outline = WikiOsloGray,
    outlineVariant = WikiOsloGray,
    surfaceVariant = WikiShuttleGray,
    onSurfaceVariant = WikiSilverSand
)

@Composable
fun WikiTrainerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    largeText: Boolean = false,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val baseColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = if (highContrast) {
        baseColorScheme.copy(
            primary = if (darkTheme) Color.White else Color.Black,
            onPrimary = if (darkTheme) Color.Black else Color.White,
            background = if (darkTheme) Color.Black else Color.White,
            onBackground = if (darkTheme) Color.White else Color.Black,
            surface = if (darkTheme) Color.Black else Color.White,
            onSurface = if (darkTheme) Color.White else Color.Black,
            outline = if (darkTheme) Color.White else Color.Black
        )
    } else {
        baseColorScheme
    }

    val view = LocalView.current
    val density = LocalDensity.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = (if (darkTheme) WikiBlack else WikiWhite).toArgb()
            window.navigationBarColor = (if (darkTheme) WikiBlack else WikiWhite).toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalReduceMotion provides reduceMotion,
        LocalDensity provides androidx.compose.ui.unit.Density(
        density = density.density,
        fontScale = if (largeText) maxOf(density.fontScale, 1.2f) else density.fontScale
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = DuolingoTypography,
            content = content
        )
    }
}
