package com.example.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class DisciplineColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,
    val surfaceElevatedEnd: Color,
    val border: Color,
    val borderAccent: Color,
    val navBackground: Color,
    val navPill: Color,
    val primary: Color,
    val primaryDark: Color,
    val primaryContainer: Color,
    val primaryBright: Color,
    val primaryButtonText: Color,
    val disciplineGreen: Color,
    val disciplineBlue: Color,
    val accentRed: Color,
    val accentRedContainer: Color,
    val accentRedBorder: Color,
    val accentOrange: Color,
    val accentOrangeContainer: Color,
    val textPrimary: Color,
    val textBody: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val isMonochrome: Boolean = false
)

enum class AppTheme(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val previewPrimary: Color,
    val previewBackground: Color,
    val previewSurface: Color
) {
    TURQUOISE_BLACK(
        id = "turquoise_black",
        title = "Turkuaz & Siyah",
        subtitle = "Elektrik turkuaz parlaklığı ve derin obsidyen siyah",
        icon = "🩵",
        previewPrimary = Color(0xFF00F5D4),
        previewBackground = Color(0xFF05090B),
        previewSurface = Color(0xFF0C1418)
    ),
    MONOCHROME(
        id = "monochrome",
        title = "Siyah & Beyaz (Noir)",
        subtitle = "Her yer tamamen saf siyah ve beyaz, sıfır renk",
        icon = "⚪",
        previewPrimary = Color(0xFFFFFFFF),
        previewBackground = Color(0xFF000000),
        previewSurface = Color(0xFF141414)
    ),
    NEON(
        id = "neon",
        title = "Siber Neon",
        subtitle = "Elektrik mavi, neon fuşya ve sibernetik parıltı",
        icon = "⚡",
        previewPrimary = Color(0xFF00F0FF),
        previewBackground = Color(0xFF08070F),
        previewSurface = Color(0xFF17122A)
    ),
    TACTICAL(
        id = "tactical",
        title = "Taktik Yeşil",
        subtitle = "Klasik askeri disiplin, koyu karbon ve neon zümrüt",
        icon = "🟢",
        previewPrimary = Color(0xFF4ADE80),
        previewBackground = Color(0xFF0A0C0A),
        previewSurface = Color(0xFF161A16)
    ),
    BLOODLINE(
        id = "bloodline",
        title = "Kızıl İrade",
        subtitle = "Obsidyen çelik ve tavizsiz alev kırmızısı",
        icon = "🔴",
        previewPrimary = Color(0xFFFF3B30),
        previewBackground = Color(0xFF0D0707),
        previewSurface = Color(0xFF200E0E)
    );

    companion object {
        fun fromId(id: String): AppTheme {
            return entries.find { it.id == id } ?: TURQUOISE_BLACK
        }
    }
}

// TURKUAZ & SİYAH (OBSIDIAN CYAN / TURQUOISE BLACK) - VARSAYILAN
val TurquoiseBlackColors = DisciplineColors(
    background = Color(0xFF05090B), // Derin Obsidyen Siyah
    surface = Color(0xFF0C1518), // Koyu Turkuaz Gölgeli Yüzey
    surfaceVariant = Color(0xFF111D22),
    surfaceElevated = Color(0xFF17282F),
    surfaceElevatedEnd = Color(0xFF0E1A1F),
    border = Color(0xFF1C343D), // Şık Turkuaz Koyu Çerçeve
    borderAccent = Color(0xFF00F5D4),
    navBackground = Color(0xFF060C0E),
    navPill = Color(0xFF132A32),
    primary = Color(0xFF00F5D4), // Elektrik Turkuaz
    primaryDark = Color(0xFF004D43),
    primaryContainer = Color(0xFF0A332C),
    primaryBright = Color(0xFF00F5D4),
    primaryButtonText = Color(0xFF011A16), // Kontrast Koyu Metin
    disciplineGreen = Color(0xFF00F5D4),
    disciplineBlue = Color(0xFF00D8F6),
    accentRed = Color(0xFFFF3366),
    accentRedContainer = Color(0xFF330B14),
    accentRedBorder = Color(0x66FF3366),
    accentOrange = Color(0xFFFF9F1C),
    accentOrangeContainer = Color(0xFF381F05),
    textPrimary = Color(0xFFFFFFFF),
    textBody = Color(0xFFE2F4F3),
    textSecondary = Color(0xFF86A5A3),
    textTertiary = Color(0xFF5A7B7A),
    textMuted = Color(0xFF3D5857),
    isMonochrome = false
)

val TacticalColors = DisciplineColors(
    background = Color(0xFF0A0C0A),
    surface = Color(0xFF161A16),
    surfaceVariant = Color(0xFF1C221C),
    surfaceElevated = Color(0xFF242C24),
    surfaceElevatedEnd = Color(0xFF182018),
    border = Color(0xFF2E362E),
    borderAccent = Color(0xFF3B483B),
    navBackground = Color(0xFF0F120F),
    navPill = Color(0xFF273427),
    primary = Color(0xFF4ADE80),
    primaryDark = Color(0xFF1E3A24),
    primaryContainer = Color(0xFF203522),
    primaryBright = Color(0xFF4ADE80),
    primaryButtonText = Color(0xFF072410),
    disciplineGreen = Color(0xFF4ADE80),
    disciplineBlue = Color(0xFF60A5FA),
    accentRed = Color(0xFFF87171),
    accentRedContainer = Color(0xFF2B1414),
    accentRedBorder = Color(0x4DF87171),
    accentOrange = Color(0xFFFB923C),
    accentOrangeContainer = Color(0xFF381F10),
    textPrimary = Color(0xFFFFFFFF),
    textBody = Color(0xFFE2E3DE),
    textSecondary = Color(0xFFA1A39E),
    textTertiary = Color(0xFF8E918E),
    textMuted = Color(0xFF5E625E),
    isMonochrome = false
)

// HER YER TAMAMEN SİYAH-BEYAZ VE GRİ TONLAR (SIFIR RENK)
val MonochromeColors = DisciplineColors(
    background = Color(0xFF000000), // Saf Siyah
    surface = Color(0xFF121212), // Koyu Noir Gri
    surfaceVariant = Color(0xFF1A1A1A),
    surfaceElevated = Color(0xFF242424),
    surfaceElevatedEnd = Color(0xFF181818),
    border = Color(0xFF383838), // Net Gri Çizgi
    borderAccent = Color(0xFF666666),
    navBackground = Color(0xFF080808),
    navPill = Color(0xFF2A2A2A),
    primary = Color(0xFFFFFFFF), // Saf Beyaz
    primaryDark = Color(0xFF303030),
    primaryContainer = Color(0xFF262626),
    primaryBright = Color(0xFFFFFFFF), // Parlak Beyaz
    primaryButtonText = Color(0xFF000000), // Beyaz butonun içine saf siyah metin
    disciplineGreen = Color(0xFFFFFFFF), // Yeşil yerine Beyaz
    disciplineBlue = Color(0xFFD4D4D4), // Mavi yerine Açık Gri
    accentRed = Color(0xFFE5E5E5), // Kırmızı yerine Beyaz/Açık Gri
    accentRedContainer = Color(0xFF1C1C1C),
    accentRedBorder = Color(0x4DFFFFFF),
    accentOrange = Color(0xFFCCCCCC), // Turuncu yerine Gri
    accentOrangeContainer = Color(0xFF202020),
    textPrimary = Color(0xFFFFFFFF),
    textBody = Color(0xFFE5E5E5),
    textSecondary = Color(0xFFA3A3A3),
    textTertiary = Color(0xFF737373),
    textMuted = Color(0xFF525252),
    isMonochrome = true
)

// SİBER NEON (CYBERPUNK ELECTRIC)
val NeonColors = DisciplineColors(
    background = Color(0xFF08070F), // Derin Siber Uzay
    surface = Color(0xFF120E22), // Koyu Neon İndigo
    surfaceVariant = Color(0xFF1B1433),
    surfaceElevated = Color(0xFF261A47),
    surfaceElevatedEnd = Color(0xFF160E2A),
    border = Color(0xFF3E276E), // Parlak Mor Çizgi
    borderAccent = Color(0xFF7B2CBF),
    navBackground = Color(0xFF0A0714),
    navPill = Color(0xFF2C1952),
    primary = Color(0xFF00F0FF), // Elektrik Neon Cyan
    primaryDark = Color(0xFF0D3342),
    primaryContainer = Color(0xFF113D4E),
    primaryBright = Color(0xFF00F0FF),
    primaryButtonText = Color(0xFF03161C),
    disciplineGreen = Color(0xFF00FF9D), // Acid Neon Green
    disciplineBlue = Color(0xFF00F0FF),
    accentRed = Color(0xFFFF007F), // Neon Fuşya / Magenta
    accentRedContainer = Color(0xFF38001C),
    accentRedBorder = Color(0x66FF007F),
    accentOrange = Color(0xFFFF9E00), // Neon Amber
    accentOrangeContainer = Color(0xFF402200),
    textPrimary = Color(0xFFFFFFFF),
    textBody = Color(0xFFECE7FE),
    textSecondary = Color(0xFFA78BFA),
    textTertiary = Color(0xFF8B5CF6),
    textMuted = Color(0xFF6D28D9),
    isMonochrome = false
)

// KIZIL İRADE (BLOODLINE OBSIDIAN)
val BloodlineColors = DisciplineColors(
    background = Color(0xFF0C0708),
    surface = Color(0xFF160D0F),
    surfaceVariant = Color(0xFF221417),
    surfaceElevated = Color(0xFF2D181C),
    surfaceElevatedEnd = Color(0xFF1C0E11),
    border = Color(0xFF451A20),
    borderAccent = Color(0xFFFF3B30),
    navBackground = Color(0xFF10080A),
    navPill = Color(0xFF331418),
    primary = Color(0xFFFF3B30), // Ateş Kırmızısı
    primaryDark = Color(0xFF4A0E14),
    primaryContainer = Color(0xFF5A121A),
    primaryBright = Color(0xFFFF5252),
    primaryButtonText = Color(0xFFFFFFFF),
    disciplineGreen = Color(0xFF10B981), // Zafer Zümrütü (Yeşil - Kırmızı temada yüksek kontrast)
    disciplineBlue = Color(0xFF38BDF8),  // Zihinsel Odak Mavisi
    accentRed = Color(0xFFEF4444),
    accentRedContainer = Color(0xFF3B1216),
    accentRedBorder = Color(0x66EF4444),
    accentOrange = Color(0xFFFF9500),
    accentOrangeContainer = Color(0xFF421E05),
    textPrimary = Color(0xFFFFFFFF),
    textBody = Color(0xFFF1F5F9),        // Temiz okunabilir açık metin
    textSecondary = Color(0xFFCBD5E1),   // Yüksek kontrastlı gümüş-arduvaz
    textTertiary = Color(0xFF94A3B8),    // Okunaklı ikincil metin
    textMuted = Color(0xFF64748B),       // Asla kaybolmayan üçüncü metin
    isMonochrome = false
)

object AppThemeManager {
    private const val PREFS_NAME = "discipline_theme_prefs"
    private const val KEY_THEME = "selected_theme_id"

    var currentTheme by mutableStateOf(AppTheme.TURQUOISE_BLACK)
        private set

    val colors: DisciplineColors
        get() = when (currentTheme) {
            AppTheme.TURQUOISE_BLACK -> TurquoiseBlackColors
            AppTheme.TACTICAL -> TacticalColors
            AppTheme.MONOCHROME -> MonochromeColors
            AppTheme.NEON -> NeonColors
            AppTheme.BLOODLINE -> BloodlineColors
        }

    val isMonochrome: Boolean
        get() = currentTheme == AppTheme.MONOCHROME

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedId = prefs.getString(KEY_THEME, AppTheme.TURQUOISE_BLACK.id) ?: AppTheme.TURQUOISE_BLACK.id
        currentTheme = AppTheme.fromId(savedId)
    }

    fun setTheme(context: Context, theme: AppTheme) {
        currentTheme = theme
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme.id).apply()
    }
}
