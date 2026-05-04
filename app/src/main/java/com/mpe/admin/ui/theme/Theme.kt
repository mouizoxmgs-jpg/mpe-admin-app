package com.mpe.admin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MPEDarkColors = darkColorScheme(
    primary = Gold,
    onPrimary = Obsidian,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldPale,
    secondary = GoldDark,
    onSecondary = OffWhite,
    tertiary = GoldLight,
    background = Obsidian,
    onBackground = OffWhite,
    surface = ObsidianSurface,
    onSurface = OffWhite,
    surfaceVariant = ObsidianLight,
    onSurfaceVariant = TextSecondary,
    outline = GoldDark,
    error = ErrorRed,
    onError = OffWhite,
)

@Composable
fun MPEAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MPEDarkColors,
        typography = MPETypography,
        content = content
    )
}
