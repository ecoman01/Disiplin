package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun getDisciplineColorScheme(): ColorScheme {
    val colors = AppThemeManager.colors
    return darkColorScheme(
        primary = colors.primary,
        onPrimary = colors.background,
        primaryContainer = colors.primaryContainer,
        onPrimaryContainer = colors.primaryBright,
        secondary = colors.disciplineGreen,
        onSecondary = colors.background,
        secondaryContainer = colors.surfaceElevated,
        onSecondaryContainer = colors.textPrimary,
        tertiary = colors.accentOrange,
        onTertiary = colors.background,
        tertiaryContainer = colors.accentOrangeContainer,
        onTertiaryContainer = colors.accentOrange,
        background = colors.background,
        onBackground = colors.textPrimary,
        surface = colors.surface,
        onSurface = colors.textPrimary,
        surfaceVariant = colors.surfaceVariant,
        onSurfaceVariant = colors.textSecondary,
        outline = colors.border,
        error = colors.accentRed,
        onError = colors.textPrimary,
        errorContainer = colors.accentRedContainer,
        onErrorContainer = colors.accentRed
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getDisciplineColorScheme(),
        typography = Typography,
        content = content
    )
}


