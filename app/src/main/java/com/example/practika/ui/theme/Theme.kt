package com.example.practika.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = AccentStar,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    surfaceVariant = SurfaceVariantColor,
    onSurfaceVariant = OnSurfaceVariantColor,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor
)

@Composable
fun PRACTIKATheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}