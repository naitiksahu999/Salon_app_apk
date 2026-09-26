package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuxuryDarkColorScheme =
  darkColorScheme(
    primary = SalonGoldPrimary,
    onPrimary = Color(0xFF141414),
    primaryContainer = Color(0xFF2A231C),
    onPrimaryContainer = SalonGoldLight,
    secondary = SalonGoldSecondary,
    onSecondary = Color(0xFF141414),
    secondaryContainer = Color(0xFF231E18),
    onSecondaryContainer = SalonGoldLight,
    tertiary = SalonRoseGold,
    background = DarkCanvasBackground,
    onBackground = LuxuryTextWhite,
    surface = DarkSurfaceCard,
    onSurface = LuxuryTextWhite,
    surfaceVariant = DarkSurfaceCardElevated,
    onSurfaceVariant = LuxuryTextMuted,
    outline = DarkBorderColor,
    outlineVariant = Color(0xFF333333)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = LuxuryDarkColorScheme,
    typography = Typography,
    content = content
  )
}

