package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = RoyalBlueDark,
    primaryContainer = RoyalBluePrimary,
    onPrimaryContainer = GoldBright,
    secondary = AquaBright,
    onSecondary = SurfaceDark,
    tertiary = GoldBright,
    background = SurfaceDark,
    onBackground = Color.White,
    surface = RoyalBlueDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF112240),
    onSurfaceVariant = Color(0xFF8892B0),
    outline = GoldAccent
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueLight,
    onPrimaryContainer = GoldBright,
    secondary = AquaLight,
    onSecondary = Color.White,
    tertiary = GoldAccent,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = AquaSoft,
    onSurfaceVariant = TextMuted,
    outline = GoldAccent
)

@Composable
fun WealthVedaTheme(
    darkTheme: Boolean = false, // Corporate clean light theme default with glass cards
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
