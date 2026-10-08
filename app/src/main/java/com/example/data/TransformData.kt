package com.example.data

/**
 * Data structures for the 7 TRANSFORM features.
 */

// 1. 🧠 KİŞİSEL HAYAT ANALİZİ
data class LifeAnalysisProfile(
    val isCompleted: Boolean = false,
    val disciplineScore: Int = 50, // 0-100
    val procrastinationLevel: String = "Yüksek", // Düşük, Orta, Yüksek
    val sportLevel: String = "Başlangıç", // Düşük, Orta, Yüksek
    val sleepLevel: String = "Orta", // Düşük, Orta, Yüksek
    val phoneUsageLevel: String = "Günde 4+ Saat",
    val motivationScore: Int = 50,
    val dailyRoutineScore: Int = 40,
    val goalClarityScore: Int = 60,
    val biggestProblem: String = "İstikrar ve Başlayıp Bırakma",
    val completedDate: Long = 0L
)

data class AnalysisQuestion(
    val id: Int,
    val category: String,
    val title: String,
    val options: List<AnalysisOption>
)

data class AnalysisOption(
    val text: String,
    val scoreImpact: Int,
    val category: String
)

// 2. 🎯 30 / 60 / 90 GÜNLÜK DÖNÜŞÜM
data class TransformDayTask(
    val id: String,
    val title: String,
    val category: String, // "Sabah", "İdman", "Odak", "Gece"
    val iconEmoji: String,
    val isCompleted: Boolean = false,
    val xpReward: Int = 50,
    val tacticalGuide: String = "",
    val durationMinutes: Int = 15
)

data class TransformDay(
    val dayNumber: Int,
    val title: String,
    val phase: String, // "Faz 1: Zihinsel Temel", "Faz 2: Demir Alışkanlık", "Faz 3: Zirve Dönüşüm"
    val tasks: List<TransformDayTask>,
    val quote: String
)

// 3. 🤖 KARAR VEREN AI KOÇ
data class AiCoachDecision(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val situation: String,
    val decision: String,
    val actionApplied: String,
    val impactTag: String, // "Hafifletildi", "Zorlaştırıldı", "Kriz Önlemi", "Denge"
    val isActionActive: Boolean = true,
    val isActionCompleted: Boolean = false
)

// 4. 📊 GELİŞMİŞ İSTATİSTİKLER & KENDİNLE MÜCADELE
data class AdvancedTransformStats(
    val disciplineScore: Int = 0,
    val consistencyVsLastMonth: Int = 0, // +%X
    val weeklyCompletionRate: Int = 0, // %
    val monthlyGrowthRate: Int = 0, // %
    val weeklyWorkoutsAverage: Float = 0f,
    val bestDays: List<String> = emptyList(),
    val weakestHabit: String = "Henüz tespit edilmedi",
    val strongestHabit: String = "İlk görev bekleniyor",
    val completedTasksCount: Int = 0,
    val totalTasksCount: Int = 0,
    // Gerçek Uygulama Karşılaştırma Verileri (Eski vs Yeni)
    val workoutsThen: Int = 0, // Başlangıç haftalık idman
    val workoutsNow: Int = 0, // Güncel tamamlanan idman
    val streakThen: Int = 0, // Başlangıç serisi
    val streakNow: Int = 0, // Güncel seri
    val mindScoreThen: Int = 0, // Başlangıç odak / zihin puanı
    val mindScoreNow: Int = 0, // Güncel zihin görevleri / motivasyon
    val nutritionRateThen: Int = 0, // Başlangıç beslenme/su disiplini %
    val nutritionRateNow: Int = 0, // Güncel beslenme/su disiplini %
    val activeDaysCount: Int = 1, // Uygulama kullanım günü
    val todayScore: Int = 50, // Bugünkü Sen skoru (%0..100)
    val oldScore: Int = 50, // Eski Sen skoru (%0..100)
    val statusTitle: String = "⚖️ DENGEDE MÜCADELE",
    val statusDescription: String = "Eski alışkanlıklarınla başa baş bir savaş var. Atacağın tek bir eylem dengeleri Yeni Sen lehine bozar.",
    val nextActionToWin: String = "Günün ilk disiplin eylemini tamamlayarak mücadelede öne geç!"
)

// 5. 🏋️ KİŞİSEL SPOR SİSTEMİ
data class PersonalSportGoal(
    val id: String,
    val name: String,
    val subtitle: String,
    val iconEmoji: String,
    val focusMuscles: String,
    val recommendedSplit: String,
    val weeklyVolumeDays: Int
)

data class SportExerciseTracking(
    val name: String,
    val currentWeightKg: Float,
    val targetSets: Int,
    val targetReps: Int,
    val recommendedNextWeightKg: Float,
    val rpeSuggestion: String,
    val completedSets: Int = 0
)

// 6. 🧨 CHALLENGE SİSTEMİ
data class TransformChallenge(
    val id: String,
    val title: String,
    val description: String,
    val targetDays: Int,
    val currentDays: Int,
    val badgeIcon: String,
    val badgeName: String,
    val xpReward: Int,
    val isCompleted: Boolean,
    val isClaimed: Boolean
)

// 7. 📝 HAFTALIK AI RAPORU
data class WeeklyAiReport(
    val weekNumber: Int,
    val dateRange: String,
    val completedTasks: Int,
    val totalTasks: Int,
    val percentageSuccess: Int,
    val improvementVsLastWeek: Int, // e.g. +11%
    val strongestDomain: String, // "Spor"
    val weakestDomain: String, // "Uyku"
    val nextWeekFocus: String, // "İstikrar ve Erken Yatış"
    val aiPersonalizedAdvice: String
)
