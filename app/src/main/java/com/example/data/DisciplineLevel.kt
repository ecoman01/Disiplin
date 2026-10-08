package com.example.data

data class DisciplineLevelInfo(
    val level: Int,
    val title: String,
    val badge: String,
    val currentLevelXp: Int,
    val xpForNextLevel: Int,
    val progress: Float,
    val totalXp: Int
)

object DisciplineLevelEngine {
    private val LEVEL_THRESHOLDS = listOf(
        0,     // Level 0: Başlangıç
        100,   // Level 1: İlk Adım (1 Antrenman)
        250,   // Level 2: Kararlı Savaşçı
        500,   // Level 3: Demir İrade
        900,   // Level 4: Durdurulamaz
        1400,  // Level 5: Stoik Usta
        2200   // Level 6+: Disiplin Efsanesi
    )

    private val LEVEL_TITLES = listOf(
        "Uyanış & Karar Çırağı",
        "Kıvılcım & İlk Zafer",
        "Çelik Zırhlı Savaşçı",
        "Tavizsiz Demir İrade",
        "Yenilmez Gladyatör",
        "Stoik Zihin Ustası",
        "Zirve Disiplin Efsanesi"
    )

    private val LEVEL_BADGES = listOf(
        "🛡️", "⚡", "🗡️", "🔥", "💎", "👑", "🌌"
    )

    fun getLevelInfo(totalXp: Int): DisciplineLevelInfo {
        val safeXp = totalXp.coerceAtLeast(0)
        var level = 0
        for (i in LEVEL_THRESHOLDS.indices.reversed()) {
            if (safeXp >= LEVEL_THRESHOLDS[i]) {
                level = i
                break
            }
        }

        val currentThreshold = LEVEL_THRESHOLDS[level]
        val nextThreshold = LEVEL_THRESHOLDS.getOrElse(level + 1) { currentThreshold + 1000 }
        val xpInLevel = safeXp - currentThreshold
        val xpNeeded = nextThreshold - currentThreshold
        val progress = if (xpNeeded > 0) (xpInLevel.toFloat() / xpNeeded).coerceIn(0f, 1f) else 1f

        val title = LEVEL_TITLES.getOrElse(level) { "Disiplin Efsanesi" }
        val badge = LEVEL_BADGES.getOrElse(level) { "🌌" }

        return DisciplineLevelInfo(
            level = level,
            title = title,
            badge = badge,
            currentLevelXp = xpInLevel,
            xpForNextLevel = xpNeeded,
            progress = progress,
            totalXp = safeXp
        )
    }
}
