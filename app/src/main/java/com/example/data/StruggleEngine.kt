package com.example.data

import java.util.Calendar

/**
 * Reel Faktörlere Dayalı Kendinle Mücadele Sistemi (Struggle Evaluation Engine)
 *
 * Kullanıcının "Bugünkü Yeni Sen" ile "Eski Erteleyen Sen" arasındaki mücadelesini
 * soyut veya statik bir sayı yerine, uygulamanın GERÇEK canlı verilerine dayanarak hesaplar:
 * 1. Fiziksel Eylem & İdman (Bugün spor yapıldı mı? Haftalık hedef ne durumda?)
 * 2. Günlük Alışkanlıklar (5 disiplin alışkanlığının kaçı tamamlandı?)
 * 3. Kriz & Direnç Zaferleri (Tembellik krizi geldiğinde pes etmek yerine kriz moduyla eyleme geçildi mi?)
 * 4. Disiplin Zinciri & Momentum (Kaç gündür kesintisiz devam ediliyor?)
 * 5. Zihinsel Dönüşüm Görevleri (30 günlük programda kaç görev başarıldı?)
 */

data class RealStruggleFactor(
    val id: String,
    val title: String,
    val category: String,
    val iconEmoji: String,
    val statusText: String,
    val impactPoints: Int, // e.g. +18 or -6
    val isPositive: Boolean,
    val explanation: String,
    val tacticalAdvice: String
)

data class StruggleEvaluationResult(
    val todayScore: Int, // 0..100
    val oldScore: Int,   // 100 - todayScore
    val netDominance: Int, // todayScore - oldScore
    val statusTitle: String,
    val statusDescription: String,
    val factors: List<RealStruggleFactor>,
    val nextActionToWin: String,
    val strongestPillar: String,
    val weakestPillar: String
)

object StruggleEngine {

    fun evaluate(
        profile: UserProfile?,
        dailyHabits: List<DisciplineHabit>,
        workoutLogs: List<WorkoutLogEntity>,
        completedTasksCount: Int = 0,
        lifeAnalysis: LifeAnalysisProfile? = null
    ): StruggleEvaluationResult {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfToday = calendar.timeInMillis

        // 1. FİZİKSEL EYLEM & İDMAN FAKTÖRÜ
        val workoutToday = workoutLogs.any { it.timestamp >= startOfToday && !it.isCrisisRescue }
        val crisisToday = workoutLogs.any { it.timestamp >= startOfToday && it.isCrisisRescue }
        val weeklyCompleted = profile?.weeklyCompleted ?: 0
        val weeklyTarget = (profile?.weeklyTarget ?: 4).coerceAtLeast(1)

        val (workoutPoints, workoutStatus, workoutAdv) = when {
            workoutToday -> Triple(
                18,
                "Bugün İdman Tamamlandı",
                "Fiziksel irade devreye girdi, kaslar ve metabolizma çalıştırıldı."
            )
            crisisToday -> Triple(
                12,
                "Kriz Eylemiyle Harekete Geçildi",
                "Tembellik direnci aşıldı, mikro eylemle hareket sağlandı."
            )
            weeklyCompleted >= weeklyTarget -> Triple(
                8,
                "Haftalık Hedefte ($weeklyCompleted/$weeklyTarget)",
                "Haftalık program tamamlandı, aktif dinlenme dengede."
            )
            else -> Triple(
                -6,
                "Bugün İdman Bekleniyor ($weeklyCompleted/$weeklyTarget)",
                "Günün antrenmanını tamamla veya 10 dk mikro yürüyüşle direnci kır."
            )
        }

        // 2. GÜNLÜK ALIŞKANLIKLAR & RUTİN FAKTÖRÜ
        val totalHabits = dailyHabits.size.coerceAtLeast(1)
        val habitsDone = dailyHabits.count { it.isCompleted }
        val pendingHabits = dailyHabits.filter { !it.isCompleted }

        val (habitPoints, habitStatus, habitAdv) = when (habitsDone) {
            totalHabits -> Triple(
                22,
                "$habitsDone/$totalHabits Alışkanlık Eksiksiz",
                "Günün tüm disiplin ritüelleri başarıyla tamamlandı, sıfır taviz."
            )
            in 3..4 -> Triple(
                14,
                "$habitsDone/$totalHabits Tamamlandı",
                "Güçlü bir rutin yürütülüyor. Kalan: ${pendingHabits.firstOrNull()?.title?.take(22) ?: "1 görev"}..."
            )
            in 1..2 -> Triple(
                6,
                "$habitsDone/$totalHabits Kısmi Başlangıç",
                "İlk adımlar atıldı. Bekleyen ${pendingHabits.size} alışkanlığı tamamlayarak farkı aç."
            )
            else -> Triple(
                -8,
                "0/$totalHabits Alışkanlık Yapılmadı",
                "Günün disiplin görevleri henüz başlatılmadı; erteleme baskı yapıyor."
            )
        }

        // 3. KRİZ ZAFERLERİ (DİRENÇ KIRMA) FAKTÖRÜ
        val crisisCount = profile?.crisisRescuesCount ?: 0
        val (crisisPoints, crisisStatus, crisisAdv) = if (crisisCount > 0) {
            val pts = (crisisCount * 3).coerceAtMost(15)
            Triple(
                pts,
                "$crisisCount Kez Tembellik Kırıldı",
                "Zor anlarda pes etmek yerine Kriz Modu kullanılarak irade kurtarıldı."
            )
        } else {
            Triple(
                0,
                "Henüz Kriz Zaferi Yok",
                "Zorlandığın, canının istemediği günlerde Kriz Modu'nu kullanarak direnci kır."
            )
        }

        // 4. DİSİPLİN ZİNCİRİ & MOMENTUM FAKTÖRÜ
        val streak = profile?.streakDays ?: 0
        val (streakPoints, streakStatus, streakAdv) = when {
            streak >= 30 -> Triple(
                20,
                "$streak Günlük Demir Zincir",
                "30 günü aşan sarsılmaz momentum. Eski kimlik tamamen geride bırakıldı."
            )
            streak >= 14 -> Triple(
                16,
                "$streak Günlük Kesintisiz Seri",
                "İki haftalık güçlü irade. Alışkanlıklar otomatikleşiyor."
            )
            streak >= 7 -> Triple(
                12,
                "$streak Günlük Seri",
                "1 haftalık kritik eşik aşıldı. Momentum her gün katlanarak büyüyor."
            )
            streak in 1..6 -> Triple(
                streak * 2,
                "$streak Günlük Aktif Seri",
                "Zincir her geçen gün güçleniyor. Bugünü asla boş geçme."
            )
            else -> Triple(
                -4,
                "0 Gün Zincir (Başlangıç)",
                "Mücadelenin sıfır noktasındasın. İlk günü tamamlayıp zinciri başlat."
            )
        }

        // 5. ZİHİN, ODAK & DÖNÜŞÜM PROGRAMI FAKTÖRÜ
        val (transformPoints, transformStatus, transformAdv) = if (completedTasksCount > 0) {
            val pts = (completedTasksCount * 2).coerceAtMost(16)
            Triple(
                pts,
                "$completedTasksCount Dönüşüm Görevi Yapıldı",
                "Sabah zırhı, derin odak ve akşam muhasebesiyle zihin keskinleştirildi."
            )
        } else {
            Triple(
                0,
                "İlk Görev Bekleniyor",
                "Dönüşüm sekmesindeki günlük 4 taktik görevi tamamlayarak zihinsel iradeni güçlendir."
            )
        }

        // TABAN PUAN HESABI
        val baseScore = if (lifeAnalysis != null && lifeAnalysis.isCompleted) {
            when (lifeAnalysis.sportLevel) {
                "Zirve", "Yüksek" -> 44
                "Orta" -> 38
                else -> 32
            }
        } else {
            36
        }

        // REEL MATEMATİKSEL DİNAMİK SKOR
        val rawScore = baseScore + workoutPoints + habitPoints + crisisPoints + streakPoints + transformPoints
        val todayScore = rawScore.coerceIn(8, 96)
        val oldScore = 100 - todayScore
        val netDominance = todayScore - oldScore

        // FAKTÖR LİSTESİ
        val factors = listOf(
            RealStruggleFactor(
                id = "factor_workout",
                title = "Fiziksel Eylem & İdman",
                category = "Beden İradesi",
                iconEmoji = "🏋️",
                statusText = workoutStatus,
                impactPoints = workoutPoints,
                isPositive = workoutPoints > 0,
                explanation = if (workoutToday) "Bugün antrenman yapılarak bedene söz geçirildi." else "Hareketsizlik erteleyen benliği besler.",
                tacticalAdvice = workoutAdv
            ),
            RealStruggleFactor(
                id = "factor_habits",
                title = "Günlük Disiplin Ritüelleri",
                category = "Rutin & Alışkanlık",
                iconEmoji = "⚡",
                statusText = habitStatus,
                impactPoints = habitPoints,
                isPositive = habitPoints > 0,
                explanation = "$habitsDone / $totalHabits alışkanlık aktif olarak tamamlandı.",
                tacticalAdvice = habitAdv
            ),
            RealStruggleFactor(
                id = "factor_crisis",
                title = "Direnç & Kriz Zaferleri",
                category = "Bahaneleri Susturma",
                iconEmoji = "🚨",
                statusText = crisisStatus,
                impactPoints = crisisPoints,
                isPositive = crisisPoints > 0,
                explanation = "Tembellik ve vazgeçme dürtüsü eyleme dönüştürüldü.",
                tacticalAdvice = crisisAdv
            ),
            RealStruggleFactor(
                id = "factor_streak",
                title = "Disiplin Zinciri & Momentum",
                category = "Süreklilik",
                iconEmoji = "🔗",
                statusText = streakStatus,
                impactPoints = streakPoints,
                isPositive = streakPoints > 0,
                explanation = if (streak > 0) "$streak gündür tavizsiz kararlılık sürdürülüyor." else "Zincir kırık veya henüz başlamadı.",
                tacticalAdvice = streakAdv
            ),
            RealStruggleFactor(
                id = "factor_transform",
                title = "Zihinsel Dönüşüm & Odak",
                category = "Zihin & Stoik",
                iconEmoji = "🧠",
                statusText = transformStatus,
                impactPoints = transformPoints,
                isPositive = transformPoints > 0,
                explanation = "30 günlük programdaki derin odak ve planlama görevleri.",
                tacticalAdvice = transformAdv
            )
        )

        // DURUM BAŞLIĞI VE AÇIKLAMASI
        val (statusTitle, statusDescription) = when {
            todayScore >= 80 -> Pair(
                "👑 ZİRVE İRADE HAKİMİYETİ",
                "Eski erteleyen kimliğin tamamen etkisizleştirildi. Bedenine ve zihnine eksiksiz hükmediyorsun."
            )
            todayScore >= 65 -> Pair(
                "⚔️ ÜSTÜNLÜK YENİ SENDE",
                "Tavizsiz sistem kontrolü elinde tutuyor. Günlük eylemlerin eski konfor alışkanlıklarını bastırıyor."
            )
            todayScore >= 50 -> Pair(
                "⚖️ DENGEDE MÜCADELE",
                "Eski alışkanlıklarınla başa baş bir savaş var. Atacağın tek bir eylem dengeleri Yeni Sen lehine bozar."
            )
            else -> Pair(
                "⚠️ ESKİ SENİN BASKISI",
                "Erteleme ve konfor alanı şu an ağır basıyor. Hemen tek bir somut eylemle kontrolü geri al."
            )
        }

        // ANLIK ZAFER HAMLESİ (Actionable next step to shift balance)
        val nextActionToWin = when {
            !workoutToday && !crisisToday ->
                "Günün antrenmanını tamamla veya Kriz Modu başlatarak iradeye anında +%16 kazandır!"
            habitsDone < totalHabits ->
                "Kalan '${pendingHabits.firstOrNull()?.title?.replace(Regex("^[\\p{So}\\p{Sk}\\s]+"), "")?.take(25) ?: "alışkanlığı"}' tamamlayarak dengeyi +%6 daha güçlendir!"
            completedTasksCount == 0 ->
                "Dönüşüm sekmesindeki bugünkü zihin görevini açıp tamamlayarak zaferi pekiştir!"
            else ->
                "Tüm cephelerde hakimiyet kurdun! Akşam stoik muhasebeni yap ve zinciri yarına taşı."
        }

        val strongest = factors.maxByOrNull { it.impactPoints }?.title ?: "Günlük Rutin"
        val weakest = factors.minByOrNull { it.impactPoints }?.title ?: "İdman Disiplini"

        return StruggleEvaluationResult(
            todayScore = todayScore,
            oldScore = oldScore,
            netDominance = netDominance,
            statusTitle = statusTitle,
            statusDescription = statusDescription,
            factors = factors,
            nextActionToWin = nextActionToWin,
            strongestPillar = strongest,
            weakestPillar = weakest
        )
    }
}
