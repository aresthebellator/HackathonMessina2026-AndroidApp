package com.hackaton.wikitrainer.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Wikipedia Brand and Logo Colors:
 * - Black: #000000 (RGB: 0, 0, 0)
 * - Shuttle Gray: #636466 (RGB: 99, 100, 102)
 * - Oslo Gray: #939598 (RGB: 147, 149, 152)
 * - Silver Sand: #C7C8CA (RGB: 199, 200, 202)
 * - White: #FFFFFF (RGB: 255, 255, 255)
 */
val WikiBlack = Color(0xFF000000)
val WikiShuttleGray = Color(0xFF636466)
val WikiOsloGray = Color(0xFF939598)
val WikiSilverSand = Color(0xFFC7C8CA)
val WikiWhite = Color(0xFFFFFFFF)

// Semantic UI Tokens mapped to Wikipedia Brand palette:
val WikiBackground = WikiWhite
val WikiSurface = WikiWhite
val WikiSurfaceAlt: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant
val WikiBorder = WikiSilverSand
val WikiBorderDark = WikiOsloGray
val WikiTextPrimary = WikiBlack
val WikiTextSecondary = WikiShuttleGray
val WikiTextMuted = WikiOsloGray

// App Theme Color mappings (also mapped to legacy Duo* tokens for complete compatibility):
val DuoGreen: Color
    @Composable get() = MaterialTheme.colorScheme.primary
val DuoGreenDark: Color
    @Composable get() = MaterialTheme.colorScheme.onPrimary
val DuoGreenLight: Color
    @Composable get() = MaterialTheme.colorScheme.primaryContainer
val DuoGreenBackground: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

val DuoRed: Color
    @Composable get() = MaterialTheme.colorScheme.error
val DuoRedDark: Color
    @Composable get() = MaterialTheme.colorScheme.onError
val DuoRedLight: Color
    @Composable get() = MaterialTheme.colorScheme.errorContainer

val DuoYellow: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary
val DuoYellowDark: Color
    @Composable get() = MaterialTheme.colorScheme.onTertiary
val DuoYellowLight: Color
    @Composable get() = MaterialTheme.colorScheme.tertiaryContainer

val DuoBlue: Color
    @Composable get() = MaterialTheme.colorScheme.secondary
val DuoBlueDark: Color
    @Composable get() = MaterialTheme.colorScheme.onSecondary
val DuoBlueLight: Color
    @Composable get() = MaterialTheme.colorScheme.secondaryContainer

val DuoPurple: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary
val DuoPurpleDark: Color
    @Composable get() = MaterialTheme.colorScheme.onTertiary
val DuoPurpleLight: Color
    @Composable get() = MaterialTheme.colorScheme.tertiaryContainer

val DuoOrange: Color
    @Composable get() = MaterialTheme.colorScheme.error
val DuoOrangeDark: Color
    @Composable get() = MaterialTheme.colorScheme.onError
val DuoOrangeLight: Color
    @Composable get() = MaterialTheme.colorScheme.errorContainer

val DuoTeal: Color
    @Composable get() = MaterialTheme.colorScheme.secondary
val DuoTealDark: Color
    @Composable get() = MaterialTheme.colorScheme.onSecondary
val DuoTealLight: Color
    @Composable get() = MaterialTheme.colorScheme.secondaryContainer

val DuoInk: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface
val DuoInkSecondary: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
val DuoSurface: Color
    @Composable get() = MaterialTheme.colorScheme.surface
val DuoBackground: Color
    @Composable get() = MaterialTheme.colorScheme.background
val DuoBorder: Color
    @Composable get() = MaterialTheme.colorScheme.outlineVariant
val DuoBorderDark: Color
    @Composable get() = MaterialTheme.colorScheme.outline
val DuoCardSelectedBg: Color
    @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val DuoCardSelectedBorder: Color
    @Composable get() = MaterialTheme.colorScheme.primary
