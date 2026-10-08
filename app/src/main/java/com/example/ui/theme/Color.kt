package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Dynamic Theme Surface & Backgrounds
val DarkBackground: Color get() = AppThemeManager.colors.background
val DarkSurface: Color get() = AppThemeManager.colors.surface
val DarkSurfaceVariant: Color get() = AppThemeManager.colors.surfaceVariant
val DarkSurfaceElevated: Color get() = AppThemeManager.colors.surfaceElevated
val DarkSurfaceElevatedEnd: Color get() = AppThemeManager.colors.surfaceElevatedEnd
val DarkCardBackground: Color get() = AppThemeManager.colors.surfaceElevated
val DarkBorder: Color get() = AppThemeManager.colors.border
val DarkBorderAccent: Color get() = AppThemeManager.colors.borderAccent
val DarkNavBackground: Color get() = AppThemeManager.colors.navBackground
val DarkNavPill: Color get() = AppThemeManager.colors.navPill

// Dynamic Theme Primary Accents
val TacticalGreen: Color get() = AppThemeManager.colors.primary
val TacticalGreenDark: Color get() = AppThemeManager.colors.primaryDark
val TacticalGreenContainer: Color get() = AppThemeManager.colors.primaryContainer
val TacticalGreenBright: Color get() = AppThemeManager.colors.primaryBright
val TacticalGreenButtonText: Color get() = AppThemeManager.colors.primaryButtonText

// Dynamic Behavioral Indicators
val DisciplineGreen: Color get() = AppThemeManager.colors.disciplineGreen
val DisciplineBlue: Color get() = AppThemeManager.colors.disciplineBlue
val OldYouRed: Color get() = AppThemeManager.colors.accentRed
val OldYouRedContainer: Color get() = AppThemeManager.colors.accentRedContainer
val OldYouRedBorder: Color get() = AppThemeManager.colors.accentRedBorder
val WarningOrange: Color get() = AppThemeManager.colors.accentOrange
val WarningOrangeContainer: Color get() = AppThemeManager.colors.accentOrangeContainer

// Dynamic Typography & Neutrals
val TextPrimary: Color get() = AppThemeManager.colors.textPrimary
val TextBody: Color get() = AppThemeManager.colors.textBody
val TextSecondary: Color get() = AppThemeManager.colors.textSecondary
val TextTertiary: Color get() = AppThemeManager.colors.textTertiary
val TextMuted: Color get() = AppThemeManager.colors.textMuted

// High-Contrast Semantic Colors for "Eski Sen vs Yeni Sen"
val ComparisonOldYouText: Color get() = Color(0xFF94A3B8)
val ComparisonOldYouSurface: Color get() = Color(0xFF161B22)
val ComparisonOldYouBorder: Color get() = Color(0xFF334155)
val ComparisonNewYouAccent: Color get() = DisciplineGreen
val ComparisonNewYouSurface: Color get() = DarkSurfaceElevated
val ComparisonNewYouBorder: Color get() = TacticalGreen.copy(alpha = 0.8f)

