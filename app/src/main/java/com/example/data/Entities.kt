package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Savaşçı",
    val mainGoal: String = "Fit ve Kararlı Olmak",
    val identity: String = "Disiplinli biri",
    val startingReason: String = "Disiplin kazanmak",
    val biggestProblem: String = "Devam etmek",
    val desiredPerson: String = "Disiplinli",
    val streakDays: Int = 0,
    val longestStreakDays: Int = 0,
    val weeklyTarget: Int = 4,
    val weeklyCompleted: Int = 0,
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val todayVsOldScore: Int = 50, // 50% clean neutral balance at start
    val crisisRescuesCount: Int = 0,
    val level: Int = 0,
    val xp: Int = 0,
    val profession: String = "Masa Başı & Ofis",
    val selectedTheme: String = "tactical",
    val fitnessLevel: String = "Orta Düzey",
    val workoutLocation: String = "Spor Salonu",
    val targetGoal: String = "Kas Kütlesi & Hipertrofi",
    val motivationStyle: String = "Stoik Felsefe",
    val isOnboardingCompleted: Boolean = false,
    val startDateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_plans")
data class WorkoutPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetMuscles: String,
    val estimatedMinutes: Int,
    val iconType: String, // "push", "pull", "legs", "cardio", "home", "recovery", "custom"
    val dayOfWeek: Int = 1, // 1: Pazartesi, 2: Salı, 3: Çarşamba, 4: Perşembe, 5: Cuma, 6: Cumartesi, 7: Pazar
    val dayName: String = "Pazartesi",
    val isCustom: Boolean = false
)

@Entity(tableName = "plan_exercises")
data class PlanExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val name: String,
    val targetSets: Int,
    val targetReps: Int,
    val lastWeightKg: Float,
    val orderIndex: Int
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planTitle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val completedExercisesCount: Int,
    val isCrisisRescue: Boolean = false,
    val crisisType: String = "" // "walk", "mini", "stretch" or empty
)

@Entity(tableName = "journey_milestones")
data class JourneyMilestoneEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val dateText: String? = null,
    val orderIndex: Int
)
