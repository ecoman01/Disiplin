package com.example.data

import com.example.R

data class LionEvolutionStage(
    val stageIndex: Int,
    val minLevel: Int,
    val minXp: Int,
    val title: String,
    val stageName: String,
    val drawableResId: Int,
    val badge: String,
    val shortTag: String,
    val description: String,
    val trait: String,
    val auraColorHex: Long,
    val stats: List<LionStat>,
    val roarQuotes: List<String>
)

data class LionStat(
    val label: String,
    val value: String,
    val percent: Float
)

data class DisciplineLionInfo(
    val currentStage: LionEvolutionStage,
    val nextStage: LionEvolutionStage?,
    val progressToNextStage: Float,
    val currentXp: Int,
    val xpForNextStage: Int,
    val isMaxStage: Boolean,
    val allStages: List<LionEvolutionStage>
)

object DisciplineLionEngine {

    val STAGES = listOf(
        LionEvolutionStage(
            stageIndex = 0,
            minLevel = 0,
            minXp = 0,
            title = "Yavru Aslan (Uyanış Çırağı)",
            stageName = "1. Evrim: Yavru Aslan",
            drawableResId = R.drawable.img_lion_stage_0,
            badge = "🐾",
            shortTag = "Çırak Aslan",
            description = "Disiplin yolculuğunun başındasın. Henüz küçük ama gözlerinde sönmeyen bir savaşçı ateşi var. Tamamladığın her antrenmanla yelen gürleşecek ve pençelerin keskinleşecek!",
            trait = "Meraklı & Kararlı",
            auraColorHex = 0xFF81C784,
            stats = listOf(
                LionStat("İrade Gücü", "%20", 0.20f),
                LionStat("Kondisyon", "%15", 0.15f),
                LionStat("Kükreme Etkisi", "%25", 0.25f)
            ),
            roarQuotes = listOf(
                "“Her büyük kral bir zamanlar sadece vazgeçmeyen bir yavruydu.”",
                "“Bugün zor gelen şey, yarın ısınma hareketin olacak!”",
                "“Bahaneler kas yapmaz, disiplin şampiyon yapar!”"
            )
        ),
        LionEvolutionStage(
            stageIndex = 1,
            minLevel = 1,
            minXp = 100,
            title = "Genç Savaşçı Aslan",
            stageName = "2. Evrim: Genç Savaşçı",
            drawableResId = R.drawable.img_lion_stage_1,
            badge = "⚡",
            shortTag = "Genç Avcı",
            description = "İlk zaferlerini kazandın! Kasların belirginleşti, yelen uzamaya ve bakışların keskinleşmeye başladı. Taktik bilekliklerinle artık sadece antrenman yapmıyorsun, disiplini bir yaşam tarzı yapıyorsun.",
            trait = "Çevik, Hızlı & Tavizsiz",
            auraColorHex = 0xFF4CAF50,
            stats = listOf(
                LionStat("İrade Gücü", "%50", 0.50f),
                LionStat("Kondisyon", "%55", 0.55f),
                LionStat("Kükreme Etkisi", "%60", 0.60f)
            ),
            roarQuotes = listOf(
                "“Tembellik zihni zehirler, hareket ise içindeki aslanı uyandırır!”",
                "“Acı geçicidir; pes etmenin utancı ömür boyu sürer!”",
                "“Gözünü hedeften ayırma, avını kovalayan aslan arkasına bakmaz!”"
            )
        ),
        LionEvolutionStage(
            stageIndex = 2,
            minLevel = 3,
            minXp = 500,
            title = "Zırhlı Gladyatör Aslan",
            stageName = "3. Evrim: Zırhlı Gladyatör",
            drawableResId = R.drawable.img_lion_stage_2,
            badge = "🛡️",
            shortTag = "Demir İrade",
            description = "Gövden çelik gibi sert, karbon fiber zırhın ve disiplin rünlerinle arenadasın. Bahaneler sana vız gelir. Kötü günlerde bile antrenmanını aksatmayan gerçek bir stoik savaşçısın.",
            trait = "Sarsılmaz & Korkusuz",
            auraColorHex = 0xFF00E676,
            stats = listOf(
                LionStat("İrade Gücü", "%80", 0.80f),
                LionStat("Kondisyon", "%85", 0.85f),
                LionStat("Kükreme Etkisi", "%85", 0.85f)
            ),
            roarQuotes = listOf(
                "“Bahanelerin olduğu yerde aslanlar barınmaz. Şartlara boyun eğmeyiz, şartları ezeriz!”",
                "“Demir ateşte dövülür, karakter zorlukta bilenir!”",
                "“Zorluklar beni durduramaz, sadece beni daha güçlü yapar!”"
            )
        ),
        LionEvolutionStage(
            stageIndex = 3,
            minLevel = 5,
            minXp = 1400,
            title = "Yüce Hükümdar / Zirve Aslan Kral",
            stageName = "4. Evrim: Zirve Aslan Kral",
            drawableResId = R.drawable.img_lion_stage_3,
            badge = "👑",
            shortTag = "Zirve Kralı",
            description = "Mutlak zirve! Işıldayan zümrüt ve altın auran, tacın ve efsanevi zırhınla disiplinin yaşayan efendisisin. Artık dış motivasyona ihtiyacın kalmadı; sen motivasyonun kendisisin.",
            trait = "Efsanevi Stoik Efendi",
            auraColorHex = 0xFFFFD700,
            stats = listOf(
                LionStat("İrade Gücü", "%100", 1.0f),
                LionStat("Kondisyon", "%100", 1.0f),
                LionStat("Kükreme Etkisi", "%100", 1.0f)
            ),
            roarQuotes = listOf(
                "“Zirve kalabalık değildir; sadece kendi sınırlarını aşan krallara aittir!”",
                "“Disiplin özgürlüktür. Kendi zihnini yöneten tüm dünyayı yönetir!”",
                "“Bugün dinlenmeyi hak ettin mi? Cevabın evetse bile bir adım daha at!”"
            )
        )
    )

    fun getLionInfo(totalXp: Int): DisciplineLionInfo {
        val safeXp = totalXp.coerceAtLeast(0)
        var stageIndex = 0

        for (i in STAGES.indices.reversed()) {
            if (safeXp >= STAGES[i].minXp) {
                stageIndex = i
                break
            }
        }

        val currentStage = STAGES[stageIndex]
        val nextStage = if (stageIndex < STAGES.size - 1) STAGES[stageIndex + 1] else null

        val progress: Float
        val xpNeeded: Int

        if (nextStage != null) {
            val span = nextStage.minXp - currentStage.minXp
            val inSpan = safeXp - currentStage.minXp
            progress = if (span > 0) (inSpan.toFloat() / span).coerceIn(0f, 1f) else 1f
            xpNeeded = (nextStage.minXp - safeXp).coerceAtLeast(0)
        } else {
            progress = 1f
            xpNeeded = 0
        }

        return DisciplineLionInfo(
            currentStage = currentStage,
            nextStage = nextStage,
            progressToNextStage = progress,
            currentXp = safeXp,
            xpForNextStage = xpNeeded,
            isMaxStage = nextStage == null,
            allStages = STAGES
        )
    }
}
