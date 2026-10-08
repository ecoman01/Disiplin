package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repository for managing all features of the TRANSFORM module.
 * Everything starts from 0 for fresh users with zero pre-filled mock data.
 */
class TransformRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("transform_module_prefs", Context.MODE_PRIVATE)

    init {
        INSTANCE = this
    }

    // 1. 🧠 Kişisel Hayat Analizi & İlk Giriş Kontrolü
    private val _isAnalysisCompleted = MutableStateFlow(prefs.getBoolean("is_analysis_completed", false))
    val isAnalysisCompleted: StateFlow<Boolean> = _isAnalysisCompleted.asStateFlow()

    private val _lifeAnalysis = MutableStateFlow(loadLifeAnalysis())
    val lifeAnalysis: StateFlow<LifeAnalysisProfile> = _lifeAnalysis.asStateFlow()

    // 2. 🎯 Program Süresi (30 / 60 / 90 Gün) & Aktif Gün (Starts at 1)
    private val _programDuration = MutableStateFlow(prefs.getInt("program_duration", 90))
    val programDuration: StateFlow<Int> = _programDuration.asStateFlow()

    private val _currentDay = MutableStateFlow(prefs.getInt("current_day", 1))
    val currentDay: StateFlow<Int> = _currentDay.asStateFlow()

    // Tamamlanan görevler 0'dan başlar (boş küme)
    private val _completedTasks = MutableStateFlow(loadCompletedTasks())
    val completedTasks: StateFlow<Set<String>> = _completedTasks.asStateFlow()

    // 3. 🤖 AI Koç Kararları
    private val _coachIntensityMode = MutableStateFlow(prefs.getString("coach_intensity", "Adaptif Dengeli") ?: "Adaptif Dengeli")
    val coachIntensityMode: StateFlow<String> = _coachIntensityMode.asStateFlow()

    private val _aiDecisions = MutableStateFlow(loadAiDecisions())
    val aiDecisions: StateFlow<List<AiCoachDecision>> = _aiDecisions.asStateFlow()

    // 4. 📊 Gelişmiş İstatistikler & Kendinle Mücadele (Eski vs Yeni)
    private val _advancedStats = MutableStateFlow(computeRealStats())
    val advancedStats: StateFlow<AdvancedTransformStats> = _advancedStats.asStateFlow()

    // 5. 🏋️ Kişisel Spor Sistemi
    private val _activeSportGoal = MutableStateFlow(prefs.getString("active_sport_goal", "hypertrophy") ?: "hypertrophy")
    val activeSportGoal: StateFlow<String> = _activeSportGoal.asStateFlow()

    private val _sportExercises = MutableStateFlow(loadSportExercises())
    val sportExercises: StateFlow<List<SportExerciseTracking>> = _sportExercises.asStateFlow()

    // 6. 🧨 Challenge Sistemi (0'dan başlar)
    private val _challenges = MutableStateFlow(computeChallenges())
    val challenges: StateFlow<List<TransformChallenge>> = _challenges.asStateFlow()

    // 7. 📝 Haftalık AI Raporu
    private val _weeklyReports = MutableStateFlow(generateWeeklyReports())
    val weeklyReports: StateFlow<List<WeeklyAiReport>> = _weeklyReports.asStateFlow()

    // ================= ACTIONS =================

    fun setProgramDuration(days: Int) {
        _programDuration.value = days
        prefs.edit().putInt("program_duration", days).apply()
        _challenges.value = computeChallenges()
    }

    fun setCurrentDay(day: Int) {
        val safeDay = day.coerceIn(1, _programDuration.value)
        _currentDay.value = safeDay
        prefs.edit().putInt("current_day", safeDay).apply()
        _advancedStats.value = computeRealStats()
        _challenges.value = computeChallenges()
        _weeklyReports.value = generateWeeklyReports()
    }

    fun toggleTask(taskId: String) {
        val current = _completedTasks.value.toMutableSet()
        if (current.contains(taskId)) {
            current.remove(taskId)
        } else {
            current.add(taskId)
        }
        _completedTasks.value = current
        saveCompletedTasks(current)

        // Gerçek veriler doğrultusunda istatistikleri ve challenge'ları güncelle
        _advancedStats.value = computeRealStats()
        _challenges.value = computeChallenges()
        _weeklyReports.value = generateWeeklyReports()
    }

    fun saveAnalysisProfile(profile: LifeAnalysisProfile) {
        val completedProfile = profile.copy(
            isCompleted = true,
            completedDate = System.currentTimeMillis()
        )
        _lifeAnalysis.value = completedProfile
        _isAnalysisCompleted.value = true

        prefs.edit()
            .putBoolean("is_analysis_completed", true)
            .putInt("ana_discipline", completedProfile.disciplineScore)
            .putString("ana_procrastination", completedProfile.procrastinationLevel)
            .putString("ana_sport", completedProfile.sportLevel)
            .putString("ana_sleep", completedProfile.sleepLevel)
            .putString("ana_phone", completedProfile.phoneUsageLevel)
            .putInt("ana_motivation", completedProfile.motivationScore)
            .putInt("ana_routine", completedProfile.dailyRoutineScore)
            .putInt("ana_goals", completedProfile.goalClarityScore)
            .putString("ana_problem", completedProfile.biggestProblem)
            .putLong("ana_date", completedProfile.completedDate)
            .apply()

        // AI Koç teşhisi ve ilk kararı
        val impact = if (completedProfile.procrastinationLevel == "Yüksek" || completedProfile.procrastinationLevel == "Kritik") {
            "Asgari Yük"
        } else {
            "Adaptif Dengeli"
        }

        addAiDecision(
            title = "Kişisel Kalibrasyon: ${completedProfile.biggestProblem} Önlemi",
            situation = "Hayat analizin tamamlandı. Erteleme düzeyi '${completedProfile.procrastinationLevel}', en büyük engel '${completedProfile.biggestProblem}' olarak saptandı.",
            decision = "Giriş bariyerini kırmak ve istikrar oluşturmak için ilk 7 günün temposu uyarlandı.",
            actionApplied = "Bugün 500ml su + 20 dk idman + 2 saat kesintisiz odak kuralı devrede.",
            impactTag = impact
        )

        _advancedStats.value = computeRealStats()
    }

    fun setSportGoal(goalId: String) {
        _activeSportGoal.value = goalId
        prefs.edit().putString("active_sport_goal", goalId).apply()
        _sportExercises.value = loadSportExercisesForGoal(goalId)
    }

    fun addAiDecision(
        title: String,
        situation: String,
        decision: String,
        actionApplied: String,
        impactTag: String
    ) {
        val newDecision = AiCoachDecision(
            id = "dec_" + System.currentTimeMillis(),
            timestamp = System.currentTimeMillis(),
            title = title,
            situation = situation,
            decision = decision,
            actionApplied = actionApplied,
            impactTag = impactTag
        )
        val updated = listOf(newDecision) + _aiDecisions.value.take(8)
        _aiDecisions.value = updated
        _coachIntensityMode.value = impactTag
        prefs.edit().putString("coach_intensity", impactTag).apply()
    }

    fun setCoachIntensity(mode: String) {
        _coachIntensityMode.value = mode
        prefs.edit().putString("coach_intensity", mode).apply()
    }

    fun markAiDecisionCompleted(decisionId: String) {
        val updated = _aiDecisions.value.map { dec ->
            if (dec.id == decisionId) dec.copy(isActionCompleted = !dec.isActionCompleted) else dec
        }
        _aiDecisions.value = updated
        _advancedStats.value = computeRealStats()
    }

    fun consultAiCoach(topic: String, customQuery: String = "") {
        when (topic) {
            "procrastination_skip_workout" -> {
                addAiDecision(
                    title = "🚨 Kriz Müdahalesi: İdman Bariyerini Yık",
                    situation = "Antrenmandan kaçma veya erteleme isteği bildirildi. Beyin enerji tasarrufu yapmaya çalışıyor.",
                    decision = "Büyük idman psikolojisini askıya al. 'Sadece 5 Dakika' kuralını devreye sok.",
                    actionApplied = "Spor kıyafetlerini giy ve sadece 5 dakika başla. 5 dakika sonra bırakmak serbest. (Vücut bırakmayacak).",
                    impactTag = "Acil Odak"
                )
            }
            "phone_distraction" -> {
                addAiDecision(
                    title = "📵 Dopamin Reset: Ekran Prangası Kırma",
                    situation = "Telefona sürekli bakma ve dikkat dağınıklığı tespiti. Beyin ucuz dopamin arıyor.",
                    decision = "Cihazı fiziksel olarak görüş alanından uzaklaştır. 20 dakikalık monoblok başlat.",
                    actionApplied = "Telefonu sessize alıp başka bir odaya bırak. Masanda sadece su ve yapacağın tek iş kalsın.",
                    impactTag = "Acil Odak"
                )
            }
            "diet_slip" -> {
                addAiDecision(
                    title = "🥗 Hasar Kontrolü: İkincil Hata Engelleme",
                    situation = "Diyet veya beslenme protokolü aksatıldı. 'Nasıl olsa bozuldu' sendromu riski.",
                    decision = "Bir öğün kaçtı diye tüm günü mahvetme kuralı. Su ve proteinle dengele.",
                    actionApplied = "Hemen 500ml soğuk su iç, sonraki öğünde karbonhidratı sıfırla, yalnızca ızgara/yeşillik tüket.",
                    impactTag = "Beslenme"
                )
            }
            "sleep_deprived" -> {
                addAiDecision(
                    title = "🔋 Enerji Koruma: Uykusuz Gün Protokolü",
                    situation = "Yetersiz uyku ve düşük zihinsel kapasite. Yüksek kortizol ve açlık sinyali riski.",
                    decision = "Ağır merkezi sinir sistemi yükünü azalt. Bol su ve erken akşam yatışı hedefi.",
                    actionApplied = "Kafeini 14:00'te kes, idmanı hafif tempo toparlanmaya çevir ve bu gece 22:30'da yatağa gir.",
                    impactTag = "Hafifletildi"
                )
            }
            else -> {
                val queryText = if (customQuery.isNotBlank()) customQuery else "Taktiksel Durum Danışması"
                addAiDecision(
                    title = "⚡ Özel AI Taktik: $queryText",
                    situation = "Bildirilen durum: '$queryText'. İrade momentumu korunmalı.",
                    decision = "Durum analiz edildi. En düşük dirençli mikro aksiyon planlandı.",
                    actionApplied = "Hemen şu an 3 derin nefes al, önündeki ilk mikro adımı 10 dakika içinde bitir.",
                    impactTag = "Adaptif Dengeli"
                )
            }
        }
    }

    fun logExerciseSet(exerciseName: String) {
        val updated = _sportExercises.value.map { ex ->
            if (ex.name == exerciseName) {
                val nextSets = if (ex.completedSets >= ex.targetSets) 0 else ex.completedSets + 1
                ex.copy(completedSets = nextSets)
            } else ex
        }
        _sportExercises.value = updated
    }

    fun resetExerciseSets() {
        val updated = _sportExercises.value.map { it.copy(completedSets = 0) }
        _sportExercises.value = updated
    }

    fun updateExerciseWeight(exerciseName: String, deltaKg: Float) {
        val updated = _sportExercises.value.map { ex ->
            if (ex.name == exerciseName) {
                val newWeight = (ex.currentWeightKg + deltaKg).coerceAtLeast(0f)
                val newRec = if (newWeight > 0) newWeight + 2.5f else ex.recommendedNextWeightKg
                ex.copy(currentWeightKg = newWeight, recommendedNextWeightKg = newRec)
            } else ex
        }
        _sportExercises.value = updated
    }

    /**
     * AI Koç Anlık Müdahale ve Taktik Seçenekleri
     */
    fun triggerAiCoachTactic(tacticKey: String) {
        when (tacticKey) {
            "low_energy" -> {
                addAiDecision(
                    title = "Kurtarma Kararı: Düşük Yoğunluk Toparlanma",
                    situation = "Yorgunluk veya düşük enerji bildirildi. Sistemi terk etmeni engelliyorum.",
                    decision = "Program yoğunluğunu %25 düşürdüm. 20 dakikalık toparlanma ve esneme idmanı tanımlandı.",
                    actionApplied = "Bugün sadece 20 dk hafif idman + 22:30 uyku protokolü devrede.",
                    impactTag = "Hafifletildi"
                )
            }
            "procrastination_crisis" -> {
                addAiDecision(
                    title = "Dopamin Müdahalesi: Acil Odak Protokolü",
                    situation = "Erteleme döngüsü ve dikkat dağınıklığı tespit edildi.",
                    decision = "Büyük hedefleri askıya aldım. Zihni tek bir mikro eyleme kilitliyorum.",
                    actionApplied = "Telefonu ters çevir, 15 dakika sadece tek bir göreve odaklan.",
                    impactTag = "Acil Odak"
                )
            }
            "peak_energy" -> {
                addAiDecision(
                    title = "Zirve Form Kararı: Konfor Alanı Yıkımı",
                    situation = "Zihinsel ve fiziksel direncin yüksek olduğu sinyali alındı.",
                    decision = "Eşik aşıldı; bugünkü antrenmana ve odak süresine ekstra hacim eklendi.",
                    actionApplied = "+1 ekstra set, +2.5 kg direnç artışı ve 45 dk derin çalışma aktif.",
                    impactTag = "Zorlaştırıldı"
                )
            }
            "nutrition_boost" -> {
                addAiDecision(
                    title = "Beden Takviyesi: Metabolik & Hidrasyon Protokolü",
                    situation = "Toparlanma ve kas onarımı için besin & su desteği talebi.",
                    decision = "Fiziksel dayanıklılığı artırmak için hidrasyon ve temiz beslenme kuralı verildi.",
                    actionApplied = "Bugün 3 Litre su hedefi + antrenman sonrası temiz protein alımı kuralı devrede.",
                    impactTag = "Beslenme"
                )
            }
        }
    }

    fun simulateAiPerformanceCheck(daysMissed: Int = 4) {
        if (daysMissed >= 3) {
            triggerAiCoachTactic("low_energy")
        } else {
            triggerAiCoachTactic("peak_energy")
        }
    }

    fun claimChallenge(challengeId: String) {
        prefs.edit().putBoolean("ch_claimed_$challengeId", true).apply()
        val updated = _challenges.value.map { ch ->
            if (ch.id == challengeId) ch.copy(isClaimed = true) else ch
        }
        _challenges.value = updated
    }

    // ================= REAL COMPARATIVE STATS (KENDİNLE MÜCADELE) =================

    /**
     * Calculates real statistics and compares "Eski Sen (Başlangıç)" vs "Yeni Sen (Güncel)"
     * based purely on real application usage, workouts, motivation/mind and nutrition.
     */
    private fun computeRealStats(): AdvancedTransformStats {
        val completed = _completedTasks.value
        val day = _currentDay.value
        val analysis = _lifeAnalysis.value

        val completedCount = completed.size
        val totalExpectedTasks = (day * 4).coerceAtLeast(4)
        val completionRate = if (totalExpectedTasks > 0) {
            ((completedCount.toFloat() / totalExpectedTasks) * 100).toInt().coerceIn(0, 100)
        } else 0

        // 1. Antrenman Görevleri (tasks ending in task2)
        val workoutTasksCompleted = completed.count { it.endsWith("task2") }
        val workoutsBaseline = when (analysis.sportLevel) {
            "Yüksek", "Zirve" -> 2
            "Orta" -> 1
            else -> 0 // Düşük / Başlangıç
        }

        // 2. Uygulama Kullanımı ve Disiplin Zinciri
        val streakBaseline = 0
        val streakCurrent = if (completedCount > 0) day else 0

        // 3. Zihin & Motive Görevleri (tasks ending in task3 or task4)
        val mindTasksCompleted = completed.count { it.endsWith("task3") || it.endsWith("task4") }
        val mindBaseline = if (analysis.isCompleted) analysis.motivationScore else 35
        val mindCurrent = (mindBaseline + (mindTasksCompleted * 4)).coerceIn(mindBaseline, 100)

        // 4. Beslenme & Su Görevleri (tasks ending in task1)
        val nutritionTasksCompleted = completed.count { it.endsWith("task1") }
        val nutritionBaseline = if (analysis.isCompleted) (100 - (if (analysis.phoneUsageLevel.contains("Riskli")) 30 else 15)).coerceIn(20, 50) else 20
        val nutritionCurrent = (nutritionBaseline + (nutritionTasksCompleted * 8)).coerceIn(nutritionBaseline, 100)

        // Genel Disiplin Skoru: 0'dan başlar, tamamlanan görevlerle yükselir
        val disciplineScore = if (completedCount == 0) {
            0
        } else {
            val score = (completionRate * 0.5f + (workoutTasksCompleted * 6f) + (mindTasksCompleted * 4f)).toInt()
            score.coerceIn(1, 100)
        }

        // Gelişim / İstikrar artışı
        val improvementPercent = if (completedCount > 0) {
            ((completedCount * 3.5f) + 10).toInt().coerceIn(10, 85)
        } else 0

        // En zayıf ve güçlü alan tespiti
        val strongest = when {
            workoutTasksCompleted >= nutritionTasksCompleted && workoutTasksCompleted >= mindTasksCompleted && workoutTasksCompleted > 0 ->
                "Düzenli İdman & Fiziksel Kararlılık"
            nutritionTasksCompleted >= workoutTasksCompleted && nutritionTasksCompleted >= mindTasksCompleted && nutritionTasksCompleted > 0 ->
                "Sabah Zırhı & Su/Beslenme Disiplini"
            mindTasksCompleted > 0 ->
                "Odaklanma & Gece Muhasebesi"
            else -> "Henüz ilk görev bekleniyor"
        }

        val weakest = when {
            workoutTasksCompleted == 0 && completedCount > 0 -> "İdman Görevleri Aksatılıyor"
            nutritionTasksCompleted == 0 && completedCount > 0 -> "Sabah Su & Beslenme Rutini"
            mindTasksCompleted == 0 && completedCount > 0 -> "Derin Odak & Gece Değerlendirmesi"
            completedCount == 0 -> "Henüz veri yok (Başlangıç Günü)"
            else -> "Gece Uyku & Ekran Süresi Kontrolü"
        }

        val bestDaysList = if (completedCount >= 3) {
            listOf("Bugün", "Aktif Günler")
        } else {
            emptyList()
        }

        val baseStruggle = when (analysis.sportLevel) {
            "Zirve", "Yüksek" -> 44
            "Orta" -> 38
            else -> 32
        }
        val workoutBonus = (workoutTasksCompleted * 8).coerceAtMost(24)
        val mindBonus = (mindTasksCompleted * 5).coerceAtMost(20)
        val nutritionBonus = (nutritionTasksCompleted * 6).coerceAtMost(16)
        val rawStruggleScore = baseStruggle + workoutBonus + mindBonus + nutritionBonus
        val dynamicTodayScore = if (completedCount == 0) 50 else rawStruggleScore.coerceIn(10, 96)
        val dynamicOldScore = 100 - dynamicTodayScore

        val (struggleTitle, struggleDesc) = when {
            dynamicTodayScore >= 80 -> Pair(
                "👑 ZİRVE İRADE HAKİMİYETİ",
                "Eski erteleyen kimliğin tamamen etkisizleştirildi. Bedenine ve zihnine eksiksiz hükmediyorsun."
            )
            dynamicTodayScore >= 65 -> Pair(
                "⚔️ ÜSTÜNLÜK YENİ SENDE",
                "Tavizsiz sistem kontrolü elinde tutuyor. Günlük eylemlerin eski konfor alışkanlıklarını bastırıyor."
            )
            dynamicTodayScore >= 50 -> Pair(
                "⚖️ DENGEDE MÜCADELE",
                "Eski alışkanlıklarınla başa baş bir savaş var. Atacağın tek bir eylem dengeleri Yeni Sen lehine bozar."
            )
            else -> Pair(
                "⚠️ ESKİ SENİN BASKISI",
                "Erteleme ve konfor alanı şu an ağır basıyor. Hemen tek bir somut eylemle kontrolü geri al."
            )
        }

        val nextAction = when {
            workoutTasksCompleted == 0 -> "Günün antrenman görevini tamamlayarak 'Yeni Sen'i anında +%16 güçlendir!"
            mindTasksCompleted == 0 -> "Bugünkü derin odak ve stoik akşam muhasebesini tamamla."
            nutritionTasksCompleted == 0 -> "Su ve beslenme disiplini görevini tamamla."
            else -> "Bugünün tüm görevleri zaferle bitti. Yarının planına odaklan."
        }

        return AdvancedTransformStats(
            disciplineScore = disciplineScore,
            consistencyVsLastMonth = improvementPercent,
            weeklyCompletionRate = completionRate,
            monthlyGrowthRate = improvementPercent,
            weeklyWorkoutsAverage = (workoutTasksCompleted.toFloat() / (day.coerceAtLeast(1) / 7f).coerceAtLeast(1f)),
            bestDays = bestDaysList,
            weakestHabit = weakest,
            strongestHabit = strongest,
            completedTasksCount = completedCount,
            totalTasksCount = totalExpectedTasks,
            // Gerçek Karşılaştırma Sütunları
            workoutsThen = workoutsBaseline,
            workoutsNow = workoutTasksCompleted,
            streakThen = streakBaseline,
            streakNow = streakCurrent,
            mindScoreThen = mindBaseline,
            mindScoreNow = mindCurrent,
            nutritionRateThen = nutritionBaseline,
            nutritionRateNow = nutritionCurrent,
            activeDaysCount = day,
            todayScore = dynamicTodayScore,
            oldScore = dynamicOldScore,
            statusTitle = struggleTitle,
            statusDescription = struggleDesc,
            nextActionToWin = nextAction
        )
    }

    private fun computeChallenges(): List<TransformChallenge> {
        val completedCount = _completedTasks.value.size
        val day = _currentDay.value
        val claimed7 = prefs.getBoolean("ch_claimed_ch_7_days", false)
        val claimed14 = prefs.getBoolean("ch_claimed_ch_14_days", false)
        val claimed30 = prefs.getBoolean("ch_claimed_ch_30_days", false)
        val claimed60 = prefs.getBoolean("ch_claimed_ch_60_days", false)
        val claimed90 = prefs.getBoolean("ch_claimed_ch_90_days", false)

        val daysWithCompletedTasks = (completedCount / 2).coerceIn(0, day)

        return listOf(
            TransformChallenge(
                id = "ch_7_days",
                title = "7 Günlük Disiplin Ateşi",
                description = "İlk 7 gün boyunca günlük dönüşüm görevlerini ve idmanlarını aksatmadan tamamla.",
                targetDays = 7,
                currentDays = daysWithCompletedTasks.coerceIn(0, 7),
                badgeIcon = "🔥",
                badgeName = "Kıvılcım Rozeti",
                xpReward = 250,
                isCompleted = daysWithCompletedTasks >= 7,
                isClaimed = claimed7
            ),
            TransformChallenge(
                id = "ch_14_days",
                title = "14 Günlük Otopilot Kilidi",
                description = "2 hafta boyunca erteleme eğilimini kır, sabah zırhı ve derin odak rutinini sabitle.",
                targetDays = 14,
                currentDays = daysWithCompletedTasks.coerceIn(0, 14),
                badgeIcon = "⚡",
                badgeName = "Otopilot Rozeti",
                xpReward = 500,
                isCompleted = daysWithCompletedTasks >= 14,
                isClaimed = claimed14
            ),
            TransformChallenge(
                id = "ch_30_days",
                title = "30 Günlük Demir İrade",
                description = "Faz 1'i eksiksiz bitir. Zihinsel temeli at, sıfır bahane felsefesini içselleştir.",
                targetDays = 30,
                currentDays = daysWithCompletedTasks.coerceIn(0, 30),
                badgeIcon = "🛡️",
                badgeName = "Çelik İrade Rozeti",
                xpReward = 1000,
                isCompleted = daysWithCompletedTasks >= 30,
                isClaimed = claimed30
            ),
            TransformChallenge(
                id = "ch_60_days",
                title = "60 Günlük Karakter Fethi",
                description = "Faz 2'yi tamamla. Eski zayıf kimliğin yerine yeni tavizsiz disiplini inşa et.",
                targetDays = 60,
                currentDays = daysWithCompletedTasks.coerceIn(0, 60),
                badgeIcon = "⚔️",
                badgeName = "Fatih Rozeti",
                xpReward = 2000,
                isCompleted = daysWithCompletedTasks >= 60,
                isClaimed = claimed60
            ),
            TransformChallenge(
                id = "ch_90_days",
                title = "90 Günlük Transform Nihai Zafer",
                description = "90 günlük bütünsel fiziksel ve zihinsel dönüşüm maratonunu tamamla. Zirve versiyonuna ulaş.",
                targetDays = 90,
                currentDays = daysWithCompletedTasks.coerceIn(0, 90),
                badgeIcon = "👑",
                badgeName = "Altın Transform Efsanesi",
                xpReward = 3500,
                isCompleted = daysWithCompletedTasks >= 90,
                isClaimed = claimed90
            )
        )
    }

    private fun generateWeeklyReports(): List<WeeklyAiReport> {
        val completed = _completedTasks.value
        val day = _currentDay.value
        val totalExpected = (day * 4).coerceAtLeast(4)
        val successPct = ((completed.size.toFloat() / totalExpected) * 100).toInt().coerceIn(0, 100)

        // Gerçek kullanıcı durumuna göre dinamik rapor
        return listOf(
            WeeklyAiReport(
                weekNumber = ((day - 1) / 7) + 1,
                dateRange = "1. Hafta (Gün 1 - ${day.coerceAtMost(7)})",
                completedTasks = completed.size,
                totalTasks = totalExpected,
                percentageSuccess = successPct,
                improvementVsLastWeek = if (completed.isNotEmpty()) 12 else 0,
                strongestDomain = if (completed.isNotEmpty()) "Başlangıç İradesi" else "Veri Toplanıyor",
                weakestDomain = if (completed.size < 2) "Görev Tamamlama" else "Zaman Yönetimi",
                nextWeekFocus = "Günlük 4 Görevi Kesintisiz Tamamlamak",
                aiPersonalizedAdvice = if (completed.isEmpty()) {
                    "Henüz başlangıç aşamasındasın. Bugün ilk görevlerini tamamlayarak sisteme ilk kıvılcımı çak."
                } else {
                    "Disiplin momentumun başladı. ${completed.size} görevi tamamladın. Sistemi bozmadan her gün küçük bir zafer kazan."
                }
            )
        )
    }

    // ================= LOADERS & INITIALIZERS =================

    private fun loadLifeAnalysis(): LifeAnalysisProfile {
        val isCompleted = prefs.getBoolean("is_analysis_completed", false)
        return LifeAnalysisProfile(
            isCompleted = isCompleted,
            disciplineScore = prefs.getInt("ana_discipline", 50),
            procrastinationLevel = prefs.getString("ana_procrastination", "Yüksek") ?: "Yüksek",
            sportLevel = prefs.getString("ana_sport", "Başlangıç") ?: "Başlangıç",
            sleepLevel = prefs.getString("ana_sleep", "Orta") ?: "Orta",
            phoneUsageLevel = prefs.getString("ana_phone", "4+ Saat") ?: "4+ Saat",
            motivationScore = prefs.getInt("ana_motivation", 50),
            dailyRoutineScore = prefs.getInt("ana_routine", 40),
            goalClarityScore = prefs.getInt("ana_goals", 60),
            biggestProblem = prefs.getString("ana_problem", "İstikrar ve Başlayıp Bırakma") ?: "İstikrar ve Başlayıp Bırakma",
            completedDate = prefs.getLong("ana_date", 0L)
        )
    }

    private fun loadCompletedTasks(): Set<String> {
        // Varsayılan olarak boş küme; kullanıcı kendisi işaretleyecek
        return prefs.getStringSet("completed_tasks_set", emptySet()) ?: emptySet()
    }

    private fun saveCompletedTasks(set: Set<String>) {
        prefs.edit().putStringSet("completed_tasks_set", set).apply()
    }

    private fun loadAiDecisions(): List<AiCoachDecision> {
        // Boş liste ile başlar, kullanıcı analiz yaptıkça veya eylem aldıkça kararlar oluşur
        return emptyList()
    }

    private fun loadSportExercises(): List<SportExerciseTracking> {
        return loadSportExercisesForGoal(_activeSportGoal.value)
    }

    fun loadSportExercisesForGoal(goalId: String): List<SportExerciseTracking> {
        return when (goalId) {
            "fat_burn" -> listOf(
                SportExerciseTracking("Kettlebell / Dumbbell Swing", 16.0f, 4, 15, 18.0f, "RPE 8 (Metabolik)"),
                SportExerciseTracking("Goblet Squat + Jump", 14.0f, 4, 12, 16.0f, "RPE 8.5"),
                SportExerciseTracking("Push-Up & Mountain Climber", 0f, 4, 20, 0f, "RPE 9"),
                SportExerciseTracking("Plank to Push-up", 0f, 3, 45, 0f, "RPE 8")
            )
            "conditioning" -> listOf(
                SportExerciseTracking("Barbell Deadlift", 75.0f, 4, 6, 80.0f, "RPE 8.5 (Kuvvet)"),
                SportExerciseTracking("Overhead Military Press", 37.5f, 4, 8, 40.0f, "RPE 8"),
                SportExerciseTracking("Pull-Up / Barfiks", 0f, 4, 8, 2.5f, "RPE 9"),
                SportExerciseTracking("Farmer's Walk", 24.0f, 3, 60, 26.0f, "RPE 8.5")
            )
            "general_fit" -> listOf(
                SportExerciseTracking("Dumbbell Goblet Squat", 18.0f, 3, 12, 20.0f, "RPE 7.5"),
                SportExerciseTracking("Dumbbell Floor Press", 20.0f, 3, 10, 22.0f, "RPE 8"),
                SportExerciseTracking("Seated Cable / Band Row", 45.0f, 3, 12, 47.5f, "RPE 7.5"),
                SportExerciseTracking("Core Deadbug & Plank", 0f, 3, 15, 0f, "RPE 7")
            )
            else -> listOf( // hypertrophy (kas)
                SportExerciseTracking("Barbell Bench Press", 65.0f, 4, 8, 67.5f, "RPE 8 (Hipertrofi)"),
                SportExerciseTracking("Barbell Back Squat", 80.0f, 4, 8, 85.0f, "RPE 8.5"),
                SportExerciseTracking("Barbell Bent-over Row", 55.0f, 4, 10, 57.5f, "RPE 8"),
                SportExerciseTracking("Incline Dumbbell Curl", 12.5f, 3, 12, 14.0f, "RPE 8.5")
            )
        }
    }

    fun getDayPlan(day: Int): TransformDay {
        val phase = when {
            day <= 30 -> "Faz 1: Zihinsel Temel & Detoks"
            day <= 60 -> "Faz 2: Demir İrade & Otopilot"
            else -> "Faz 3: Zirve Dönüşüm & Karakter"
        }

        val tasks = listOf(
            TransformDayTask(
                id = "day${day}_task1",
                title = "🌅 Sabah Zırhı: 500ml Su + 5 Dk Derin Nefes & Hedef Okuma",
                category = "Sabah",
                iconEmoji = "⚡",
                xpReward = 50,
                durationMinutes = 10,
                tacticalGuide = "Uyanır uyanmaz ilk 10 dakikada 500ml ılık veya oda sıcaklığında su iç. Hücresel hidrasyon uykuda kaybedilen sıvıyı yerine koyar ve kortizolü dengeler. Ardından 4-4-4 diyafram nefesi alarak bugünkü 1 numaralı hedefini sesli tekrar et."
            ),
            TransformDayTask(
                id = "day${day}_task2",
                title = "🏋️ Günlük Dönüşüm İdmanı: Programlanmış $day. Gün Antrenmanı",
                category = "İdman",
                iconEmoji = "💪",
                xpReward = 100,
                durationMinutes = 45,
                tacticalGuide = "Telefonu uçak moduna al. Isınma setlerinin ardından reçetedeki hareketleri RPE 8 seviyesinde icra et. Her set arasında 90 saniye dinlenme süresine sadık kal."
            ),
            TransformDayTask(
                id = "day${day}_task3",
                title = "📵 2 Saat Kesintisiz Derin Odak (Bildirimsiz Çalışma)",
                category = "Odak",
                iconEmoji = "🎯",
                xpReward = 75,
                durationMinutes = 120,
                tacticalGuide = "Bildirimleri tamamen sessize al, telefonu başka bir odaya bırak. Günün en zor, ertelemeye en meyilli tek bir kritik işine 2 blok halinde kilitlen. Zihinsel dopamin reseptörlerini sıfırlar."
            ),
            TransformDayTask(
                id = "day${day}_task4",
                title = "📖 Gece Muhasebesi: Günü Değerlendir + Yarını Planla",
                category = "Gece",
                iconEmoji = "🌙",
                xpReward = 50,
                durationMinutes = 10,
                tacticalGuide = "Stoik akşam sorgulaması: 'Bugün nerede taviz verdim? Nerede iradeli kaldım? Yarın sabah hangi saatte uyanacağım?'. 3 dakika zihinsel kapanış ve yarının kıyafetlerini hazırlama."
            )
        )

        val quotes = listOf(
            "“Bugün kendini fethedemezsen, dünya seni fetheder.”",
            "“Disiplin, ne istediğin ile en çok ne istediğin arasındaki tercihtir.”",
            "“Acı geçicidir; pes etmenin utancı ise ebedidir.”",
            "“Tereddüt ettiğin an, zihnin sana bahaneler üretir. Hemen eyleme geç.”"
        )

        return TransformDay(
            dayNumber = day,
            title = "$day. Gün: Kararlılık Kuşatması",
            phase = phase,
            tasks = tasks,
            quote = quotes[day % quotes.size]
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: TransformRepository? = null

        fun getInstance(context: Context): TransformRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransformRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
