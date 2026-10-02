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

private val WikiDarkSurface = Color(0xFF1C1C1E)          // near-black dark surface
private val WikiDarkSurfaceVariant = Color(0xFF2C2C2E)    // slightly lighter for cards
private val WikiDarkBackground = Color(0xFF000000)        // true black background

private val LightColorScheme = lightColorScheme(
    primary = WikiBlack,
    onPrimary = WikiWhite,
    primaryContainer = WikiShuttleGray,
    onPrimaryContainer = WikiWhite,
    secondary = WikiShuttleGray,
    onSecondary = WikiWhite,
    secondaryContainer = Color(0xFFEEEEEE),   // very light gray for selected state
    onSecondaryContainer = WikiBlack,
    tertiary = WikiOsloGray,
    onTertiary = WikiWhite,
    tertiaryContainer = WikiSilverSand,
    onTertiaryContainer = WikiBlack,
    error = Color(0xFFCC0000),                // proper red for errors in light mode
    onError = WikiWhite,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = WikiWhite,
    onBackground = WikiBlack,
    surface = WikiWhite,
    onSurface = WikiBlack,
    surfaceVariant = Color(0xFFF5F5F5),       // very light gray for surface variants
    onSurfaceVariant = WikiShuttleGray,
    outline = Color(0xFFD0D0D0),              // lighter borders for cleaner look
    outlineVariant = Color(0xFFE5E5E5)        // even lighter for subtle dividers
)

private val DarkColorScheme = darkColorScheme(
    primary = WikiWhite,
    onPrimary = WikiBlack,
    primaryContainer = WikiShuttleGray,
    onPrimaryContainer = WikiWhite,
    secondary = WikiSilverSand,
    onSecondary = WikiBlack,
    secondaryContainer = Color(0xFF3A3A3C),   // dark selected state
    onSecondaryContainer = WikiWhite,
    tertiary = WikiSilverSand,
    onTertiary = WikiBlack,
    tertiaryContainer = WikiShuttleGray,
    onTertiaryContainer = WikiWhite,
    error = Color(0xFFFF6B6B),               // bright red for dark mode
    onError = WikiBlack,
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color(0xFFFFDAD6),
    background = WikiDarkBackground,
    onBackground = WikiWhite,
    surface = WikiDarkSurface,               // fixed: near-black instead of mid-grey
    onSurface = WikiWhite,
    surfaceVariant = WikiDarkSurfaceVariant, // slightly lighter for cards/containers
    onSurfaceVariant = WikiSilverSand,
    outline = Color(0xFF48484A),             // subtle dark borders
    outlineVariant = Color(0xFF3A3A3C)       // very subtle dividers
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
