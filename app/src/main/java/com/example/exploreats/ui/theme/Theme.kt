package com.example.exploreats.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = WhiteCard,
    secondary = MintGreenDark,
    onSecondary = WhiteCard,
    background = WarmBackground,
    onBackground = TextPrimary,
    surface = WhiteCard,
    onSurface = TextPrimary,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary
)

@Composable
fun ExplorEatsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
