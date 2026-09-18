package com.hackaton.wikitrainer.core.designsystem

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
val WikiSurfaceAlt = WikiSilverSand.copy(alpha = 0.15f)
val WikiBorder = WikiSilverSand
val WikiBorderDark = WikiOsloGray
val WikiTextPrimary = WikiBlack
val WikiTextSecondary = WikiShuttleGray
val WikiTextMuted = WikiOsloGray

// App Theme Color mappings (also mapped to legacy Duo* tokens for complete compatibility):
val DuoGreen = WikiBlack
val DuoGreenDark = WikiShuttleGray
val DuoGreenLight = WikiSilverSand
val DuoGreenBackground = WikiWhite

val DuoRed = WikiShuttleGray
val DuoRedDark = WikiBlack
val DuoRedLight = WikiSilverSand

val DuoYellow = WikiOsloGray
val DuoYellowDark = WikiBlack
val DuoYellowLight = WikiSilverSand

val DuoBlue = WikiBlack
val DuoBlueDark = WikiShuttleGray
val DuoBlueLight = WikiSilverSand.copy(alpha = 0.15f)

val DuoPurple = WikiShuttleGray
val DuoPurpleDark = WikiBlack
val DuoPurpleLight = WikiSilverSand

val DuoOrange = WikiShuttleGray
val DuoOrangeDark = WikiBlack
val DuoOrangeLight = WikiSilverSand

val DuoTeal = WikiBlack
val DuoTealDark = WikiShuttleGray
val DuoTealLight = WikiSilverSand

val DuoInk = WikiBlack
val DuoInkSecondary = WikiShuttleGray
val DuoSurface = WikiWhite
val DuoBackground = WikiWhite
val DuoBorder = WikiSilverSand
val DuoBorderDark = WikiOsloGray
val DuoCardSelectedBg = WikiSilverSand.copy(alpha = 0.15f)
val DuoCardSelectedBorder = WikiBlack

