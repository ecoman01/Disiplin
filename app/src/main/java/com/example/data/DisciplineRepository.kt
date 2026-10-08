package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DisciplineRepository(private val database: DisciplineDatabase) {
    private val userDao = database.userDao()
    private val workoutDao = database.workoutDao()
    private val journeyDao = database.journeyDao()

    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
    val workoutPlans: Flow<List<WorkoutPlanEntity>> = workoutDao.getAllPlans()
    val milestones: Flow<List<JourneyMilestoneEntity>> = journeyDao.getAllMilestones()
    val workoutLogs: Flow<List<WorkoutLogEntity>> = workoutDao.getAllWorkoutLogs()

    fun getExercisesForPlan(planId: Long): Flow<List<PlanExerciseEntity>> {
        return workoutDao.getExercisesForPlan(planId)
    }

    suspend fun getExercisesForPlanSync(planId: Long): List<PlanExerciseEntity> {
        return workoutDao.getExercisesForPlanSync(planId)
    }

    suspend fun updateExerciseWeight(exerciseId: Long, weightKg: Float) {
        workoutDao.updateExerciseWeight(exerciseId, weightKg)
    }

    suspend fun ensureInitialized() {
        // Seed default profile if not exists
        val existingProfile = userDao.getUserProfileSync()
        if (existingProfile == null) {
            userDao.insertOrUpdate(
                UserProfile(
                    id = 1,
                    name = "Savaşçı",
                    mainGoal = "Fit ve Kararlı Olmak",
                    identity = "Disiplinli biri",
                    startingReason = "Disiplin kazanmak",
                    biggestProblem = "Devam etmek",
                    desiredPerson = "Disiplinli",
                    streakDays = 0,
                    longestStreakDays = 0,
                    weeklyTarget = 4,
                    weeklyCompleted = 0,
                    totalWorkouts = 0,
                    totalMinutes = 0,
                    todayVsOldScore = 50,
                    crisisRescuesCount = 0,
                    level = 0,
                    xp = 0,
                    selectedTheme = "tactical",
                    isOnboardingCompleted = false
                )
            )
        } else if (existingProfile.totalWorkouts == 47 || existingProfile.streakDays == 12) {
            // Reset old mock values to clean 0 slate as requested
            userDao.update(
                existingProfile.copy(
                    streakDays = 0,
                    longestStreakDays = 0,
                    weeklyCompleted = 0,
                    totalWorkouts = 0,
                    totalMinutes = 0,
                    todayVsOldScore = 50,
                    crisisRescuesCount = 0,
                    level = 0,
                    xp = 0
                )
            )
        }

        // Seed default 7-day plans if empty or outdated
        if (workoutDao.getPlanCount() < 7) {
            workoutDao.clearDefaultPlans()

            // 1. Pazartesi: Göğüs + Triceps (İtiş)
            val monId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Pazartesi: Göğüs + Triceps (İtiş)",
                    targetMuscles = "Göğüs • Ön Omuz • Triceps",
                    estimatedMinutes = 45,
                    iconType = "push",
                    dayOfWeek = 1,
                    dayName = "Pazartesi"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = monId, name = "Barbell Bench Press", targetSets = 3, targetReps = 10, lastWeightKg = 55f, orderIndex = 1),
                    PlanExerciseEntity(planId = monId, name = "Incline Dumbbell Press", targetSets = 3, targetReps = 10, lastWeightKg = 22f, orderIndex = 2),
                    PlanExerciseEntity(planId = monId, name = "Cable Fly / Şınav", targetSets = 3, targetReps = 12, lastWeightKg = 15f, orderIndex = 3),
                    PlanExerciseEntity(planId = monId, name = "Triceps Halat Pushdown", targetSets = 3, targetReps = 12, lastWeightKg = 20f, orderIndex = 4),
                    PlanExerciseEntity(planId = monId, name = "Dips / Bench Dips", targetSets = 3, targetReps = 12, lastWeightKg = 0f, orderIndex = 5)
                )
            )

            // 2. Salı: Sırt + Biceps (Çekiş)
            val tueId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Salı: Sırt + Biceps (Çekiş)",
                    targetMuscles = "Kanat • Trapez • Biceps • Arka Omuz",
                    estimatedMinutes = 45,
                    iconType = "pull",
                    dayOfWeek = 2,
                    dayName = "Salı"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = tueId, name = "Lat Pulldown / Barfiks", targetSets = 3, targetReps = 10, lastWeightKg = 60f, orderIndex = 1),
                    PlanExerciseEntity(planId = tueId, name = "Barbell / Dumbbell Row", targetSets = 3, targetReps = 10, lastWeightKg = 50f, orderIndex = 2),
                    PlanExerciseEntity(planId = tueId, name = "Seated Cable Row", targetSets = 3, targetReps = 12, lastWeightKg = 45f, orderIndex = 3),
                    PlanExerciseEntity(planId = tueId, name = "Barbell Biceps Curl", targetSets = 3, targetReps = 10, lastWeightKg = 25f, orderIndex = 4),
                    PlanExerciseEntity(planId = tueId, name = "Hammer Curl (Çekiç)", targetSets = 3, targetReps = 12, lastWeightKg = 14f, orderIndex = 5)
                )
            )

            // 3. Çarşamba: Bacak + Karın / Core
            val wedId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Çarşamba: Bacak + Karın (Core)",
                    targetMuscles = "Kuadriseps • Hamstring • Kalça • Karın",
                    estimatedMinutes = 50,
                    iconType = "legs",
                    dayOfWeek = 3,
                    dayName = "Çarşamba"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = wedId, name = "Barbell Squat / Goblet Squat", targetSets = 4, targetReps = 8, lastWeightKg = 75f, orderIndex = 1),
                    PlanExerciseEntity(planId = wedId, name = "Romanian Deadlift", targetSets = 3, targetReps = 10, lastWeightKg = 65f, orderIndex = 2),
                    PlanExerciseEntity(planId = wedId, name = "Leg Press / Walking Lunges", targetSets = 3, targetReps = 12, lastWeightKg = 130f, orderIndex = 3),
                    PlanExerciseEntity(planId = wedId, name = "Asılı Bacak Kaldırma (Hanging Leg Raise)", targetSets = 3, targetReps = 15, lastWeightKg = 0f, orderIndex = 4),
                    PlanExerciseEntity(planId = wedId, name = "Plank (Merkez Stabilizasyonu)", targetSets = 3, targetReps = 60, lastWeightKg = 0f, orderIndex = 5)
                )
            )

            // 4. Perşembe: Aktif Toparlanma & Mobilite
            val thuId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Perşembe: Aktif Toparlanma & Mobilite",
                    targetMuscles = "Eklem Açıklığı • Omurga Esnekliği • Dolaşım",
                    estimatedMinutes = 30,
                    iconType = "recovery",
                    dayOfWeek = 4,
                    dayName = "Perşembe"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = thuId, name = "Kedi-Deve & Torasik Omurga Açma", targetSets = 3, targetReps = 10, lastWeightKg = 0f, orderIndex = 1),
                    PlanExerciseEntity(planId = thuId, name = "Dünyanın En İyi Esnemesi (World's Greatest)", targetSets = 3, targetReps = 8, lastWeightKg = 0f, orderIndex = 2),
                    PlanExerciseEntity(planId = thuId, name = "Kalça Fleksör & Hamstring Germe", targetSets = 3, targetReps = 45, lastWeightKg = 0f, orderIndex = 3),
                    PlanExerciseEntity(planId = thuId, name = "Derin Çömelme (Deep Squat Hold)", targetSets = 3, targetReps = 60, lastWeightKg = 0f, orderIndex = 4),
                    PlanExerciseEntity(planId = thuId, name = "20 Dakika Tempolu Yürüyüş", targetSets = 1, targetReps = 20, lastWeightKg = 0f, orderIndex = 5)
                )
            )

            // 5. Cuma: Omuz + Kol & Üst Vücut
            val friId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Cuma: Omuz + Kollar (Üst Gövde)",
                    targetMuscles = "Omuz Başları • Trapez • Biceps • Triceps",
                    estimatedMinutes = 45,
                    iconType = "push",
                    dayOfWeek = 5,
                    dayName = "Cuma"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = friId, name = "Overhead Dumbbell Press", targetSets = 4, targetReps = 10, lastWeightKg = 18f, orderIndex = 1),
                    PlanExerciseEntity(planId = friId, name = "Dumbbell Lateral Raise (Yan Omuz)", targetSets = 4, targetReps = 15, lastWeightKg = 10f, orderIndex = 2),
                    PlanExerciseEntity(planId = friId, name = "Face Pull (Arka Omuz & Duruş)", targetSets = 3, targetReps = 15, lastWeightKg = 25f, orderIndex = 3),
                    PlanExerciseEntity(planId = friId, name = "Incline Dumbbell Curl", targetSets = 3, targetReps = 10, lastWeightKg = 12f, orderIndex = 4),
                    PlanExerciseEntity(planId = friId, name = "Skull Crushers (Alına Triceps)", targetSets = 3, targetReps = 12, lastWeightKg = 20f, orderIndex = 5)
                )
            )

            // 6. Cumartesi: Fonksiyonel HIIT & Kondisyon
            val satId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Cumartesi: Fonksiyonel Kondisyon & HIIT",
                    targetMuscles = "Metabolik Hız • Yağ Yakımı • Patlayıcı Güç",
                    estimatedMinutes = 35,
                    iconType = "cardio",
                    dayOfWeek = 6,
                    dayName = "Cumartesi"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = satId, name = "Dumbbell Snatch / Kettlebell Swing", targetSets = 4, targetReps = 12, lastWeightKg = 16f, orderIndex = 1),
                    PlanExerciseEntity(planId = satId, name = "Burpees", targetSets = 3, targetReps = 12, lastWeightKg = 0f, orderIndex = 2),
                    PlanExerciseEntity(planId = satId, name = "İp Atlama / Jumping Jacks", targetSets = 4, targetReps = 60, lastWeightKg = 0f, orderIndex = 3),
                    PlanExerciseEntity(planId = satId, name = "Mountain Climbers (Dağcı)", targetSets = 3, targetReps = 30, lastWeightKg = 0f, orderIndex = 4),
                    PlanExerciseEntity(planId = satId, name = "Hava Squatı (Tempolu)", targetSets = 3, targetReps = 25, lastWeightKg = 0f, orderIndex = 5)
                )
            )

            // 7. Pazar: Zihinsel Reset & Core & Esneme
            val sunId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = "Pazar: Zihinsel Reset & Core",
                    targetMuscles = "Zihinsel Berraklık • Karın • Rejenerasyon",
                    estimatedMinutes = 30,
                    iconType = "home",
                    dayOfWeek = 7,
                    dayName = "Pazar"
                )
            )
            workoutDao.insertExercises(
                listOf(
                    PlanExerciseEntity(planId = sunId, name = "Diyafram Nefes Egzersizi (4-4-4)", targetSets = 3, targetReps = 5, lastWeightKg = 0f, orderIndex = 1),
                    PlanExerciseEntity(planId = sunId, name = "Hollow Body Hold", targetSets = 3, targetReps = 30, lastWeightKg = 0f, orderIndex = 2),
                    PlanExerciseEntity(planId = sunId, name = "Yan Plank (Side Plank)", targetSets = 3, targetReps = 40, lastWeightKg = 0f, orderIndex = 3),
                    PlanExerciseEntity(planId = sunId, name = "Kobra & Çocuk Duruşu", targetSets = 3, targetReps = 60, lastWeightKg = 0f, orderIndex = 4),
                    PlanExerciseEntity(planId = sunId, name = "Açık Hava Yürüyüşü / Hafif Koşu", targetSets = 1, targetReps = 25, lastWeightKg = 0f, orderIndex = 5)
                )
            )
        }

        // Seed default milestones if empty or upgrade names
        val todayStr = SimpleDateFormat("d MMMM yyyy", Locale("tr")).format(Date())
        val heroicMilestones = listOf(
            JourneyMilestoneEntity("start", "Kutsal Sözleşme: Uyanış", "Eski zayıf benliğe veda ettin; disiplinli bir hayat için yemin ettin.", "📜", true, todayStr, 1),
            JourneyMilestoneEntity("first_workout", "İlk Kan: Eyleme Geçiş", "Tereddüdü ve bahaneleri ezdin, ilk teri alnından akıttın.", "⚡", false, null, 2),
            JourneyMilestoneEntity("streak_7", "Çelik Zincir: 7 Günlük Kuşatma", "Zihin direnmeyi bıraktı; tavizsiz alışkanlık kök saldı.", "🔗", false, null, 3),
            JourneyMilestoneEntity("crisis_turn", "Ateş Sınavı: Kriz Zaferi", "En yorgun ve isteksiz gününde bile teslim olmadın, asgariyi tamamladın.", "🛡️", false, null, 4),
            JourneyMilestoneEntity("streak_14", "Dönüm Noktası: Demir Otopilot", "Geçici motivasyon tükendi; yerini tavizsiz sisteme bıraktı.", "🎯", false, null, 5),
            JourneyMilestoneEntity("streak_30", "Kaya Gibi Kimlik: 30 Günlük Dönüşüm", "Disiplin artık yaptığın bir şey değil, bizzat kim olduğun.", "💎", false, null, 6),
            JourneyMilestoneEntity("workouts_100", "Yenilmez Yüzyıl: 100 Sefer Kulübü", "Sadece başlayanlar değil, asla bırakmayanlar tarihe geçer.", "🏆", false, null, 7),
            JourneyMilestoneEntity("iron_will", "Stoik Zırh: Mutlak İrade Hakimiyeti", "Zihnin erteleme tuzaklarını 5 kez peş peşe darmadağın ettin.", "👑", false, null, 8)
        )

        if (journeyDao.getMilestoneCount() == 0) {
            journeyDao.insertMilestones(heroicMilestones)
        } else {
            // Update existing milestone titles & descriptions to heroic names while preserving isUnlocked & unlockedDate
            for (m in heroicMilestones) {
                journeyDao.updateTitleAndSubtitle(m.id, m.title, m.subtitle, m.iconEmoji)
            }
        }
    }

    suspend fun applyPersonalizedWorkouts(
        profession: String,
        fitnessLevel: String = "Orta Düzey",
        workoutLocation: String = "Spor Salonu",
        targetGoal: String = "Kas Kütlesi & Hipertrofi"
    ) {
        val cleanProf = profession.trim().ifBlank { "Masa Başı & Ofis / Yazılımcı" }
        val plans = ProfessionCoachEngine.getPersonalized7DayPlan(
            profession = cleanProf,
            fitnessLevel = fitnessLevel,
            workoutLocation = workoutLocation,
            targetGoal = targetGoal
        )
        workoutDao.clearDefaultPlans()
        for (dayPlan in plans) {
            val planId = workoutDao.insertPlan(
                WorkoutPlanEntity(
                    title = dayPlan.title,
                    targetMuscles = dayPlan.targetMuscles,
                    estimatedMinutes = dayPlan.estimatedMinutes,
                    iconType = dayPlan.iconType,
                    dayOfWeek = dayPlan.dayOfWeek,
                    dayName = dayPlan.dayName,
                    isCustom = false
                )
            )
            val exerciseEntities = dayPlan.exercises.map { spec ->
                PlanExerciseEntity(
                    planId = planId,
                    name = spec.name,
                    targetSets = spec.targetSets,
                    targetReps = spec.targetReps,
                    lastWeightKg = spec.defaultWeightKg,
                    orderIndex = spec.orderIndex
                )
            }
            workoutDao.insertExercises(exerciseEntities)
        }
    }

    suspend fun applyProfessionWorkouts(profession: String) {
        val current = userDao.getUserProfileSync()
        applyPersonalizedWorkouts(
            profession = profession,
            fitnessLevel = current?.fitnessLevel ?: "Orta Düzey",
            workoutLocation = current?.workoutLocation ?: "Spor Salonu",
            targetGoal = current?.targetGoal ?: "Kas Kütlesi & Hipertrofi"
        )
    }

    suspend fun saveOnboarding(
        name: String,
        reason: String,
        problem: String,
        desiredPerson: String,
        profession: String = "Masa Başı & Ofis / Yazılımcı",
        fitnessLevel: String = "Orta Düzey",
        workoutLocation: String = "Spor Salonu",
        targetGoal: String = "Kas Kütlesi & Hipertrofi",
        motivationStyle: String = "Stoik Felsefe"
    ) {
        val current = userDao.getUserProfileSync() ?: UserProfile()
        val cleanProf = profession.trim().ifBlank { "Masa Başı & Ofis / Yazılımcı" }
        val cleanLevel = fitnessLevel.trim().ifBlank { "Orta Düzey" }
        val cleanLoc = workoutLocation.trim().ifBlank { "Spor Salonu" }
        val cleanGoal = targetGoal.trim().ifBlank { "Kas Kütlesi & Hipertrofi" }
        val cleanStyle = motivationStyle.trim().ifBlank { "Stoik Felsefe" }

        userDao.insertOrUpdate(
            current.copy(
                name = name.trim().ifBlank { "Savaşçı" },
                startingReason = reason,
                biggestProblem = problem,
                desiredPerson = desiredPerson,
                identity = "$desiredPerson biri",
                profession = cleanProf,
                fitnessLevel = cleanLevel,
                workoutLocation = cleanLoc,
                targetGoal = cleanGoal,
                motivationStyle = cleanStyle,
                isOnboardingCompleted = true
            )
        )
        // Automatically rebuild the 7-day workout plans tailored to ALL these criteria
        applyPersonalizedWorkouts(
            profession = cleanProf,
            fitnessLevel = cleanLevel,
            workoutLocation = cleanLoc,
            targetGoal = cleanGoal
        )
    }

    suspend fun updateTrainingPreferences(
        fitnessLevel: String,
        workoutLocation: String,
        targetGoal: String,
        motivationStyle: String
    ) {
        val current = userDao.getUserProfileSync() ?: return
        val updated = current.copy(
            fitnessLevel = fitnessLevel,
            workoutLocation = workoutLocation,
            targetGoal = targetGoal,
            motivationStyle = motivationStyle
        )
        userDao.update(updated)
        applyPersonalizedWorkouts(
            profession = current.profession,
            fitnessLevel = fitnessLevel,
            workoutLocation = workoutLocation,
            targetGoal = targetGoal
        )
    }

    suspend fun updateProfession(newProfession: String) {
        val current = userDao.getUserProfileSync() ?: return
        val cleanProf = newProfession.trim().ifBlank { "Masa Başı & Ofis / Yazılımcı" }
        userDao.update(current.copy(profession = cleanProf))
        // Automatically rebuild the 7-day workout plans for the new profession and current preferences
        applyPersonalizedWorkouts(
            profession = cleanProf,
            fitnessLevel = current.fitnessLevel,
            workoutLocation = current.workoutLocation,
            targetGoal = current.targetGoal
        )
    }

    suspend fun getPlanForDay(dayOfWeek: Int): WorkoutPlanEntity? {
        return workoutDao.getPlanForDay(dayOfWeek)
    }

    suspend fun updateName(newName: String) {
        val cleanName = newName.trim().ifBlank { "Savaşçı" }
        userDao.updateName(cleanName)
    }

    suspend fun updateGoal(newGoal: String) {
        val current = userDao.getUserProfileSync() ?: return
        userDao.update(current.copy(mainGoal = newGoal))
    }

    suspend fun updateIdentity(newIdentity: String) {
        val current = userDao.getUserProfileSync() ?: return
        userDao.update(current.copy(identity = newIdentity))
    }

    suspend fun createCustomPlan(
        title: String,
        muscles: String,
        estimatedMinutes: Int,
        exercises: List<Pair<String, Pair<Int, Int>>> // name to (sets, reps)
    ): Long {
        val planId = workoutDao.insertPlan(
            WorkoutPlanEntity(
                title = title,
                targetMuscles = muscles.ifBlank { "Özel Program" },
                estimatedMinutes = estimatedMinutes,
                iconType = "custom",
                isCustom = true
            )
        )
        val entities = exercises.mapIndexed { idx, item ->
            PlanExerciseEntity(
                planId = planId,
                name = item.first,
                targetSets = item.second.first,
                targetReps = item.second.second,
                lastWeightKg = 0f,
                orderIndex = idx + 1
            )
        }
        workoutDao.insertExercises(entities)
        return planId
    }

    suspend fun completeWorkoutSession(
        planTitle: String,
        durationMinutes: Int,
        completedExercisesCount: Int,
        updatedWeights: Map<Long, Float>
    ) {
        // Update weights
        for ((exId, weight) in updatedWeights) {
            workoutDao.updateExerciseWeight(exId, weight)
        }

        // Insert log
        workoutDao.insertWorkoutLog(
            WorkoutLogEntity(
                planTitle = planTitle,
                timestamp = System.currentTimeMillis(),
                durationMinutes = durationMinutes,
                completedExercisesCount = completedExercisesCount,
                isCrisisRescue = false
            )
        )

        // Update profile
        val profile = userDao.getUserProfileSync() ?: UserProfile()
        val newStreak = profile.streakDays + 1
        val newLongest = maxOf(profile.longestStreakDays, newStreak)
        val newWeeklyCompleted = minOf(profile.weeklyTarget, profile.weeklyCompleted + 1)
        val newTotalWorkouts = profile.totalWorkouts + 1
        val newTotalMinutes = profile.totalMinutes + durationMinutes
        val newScore = minOf(100, profile.todayVsOldScore + 2)
        val addedXp = 100
        val newXp = profile.xp + addedXp
        val newLevel = DisciplineLevelEngine.getLevelInfo(newXp).level

        userDao.update(
            profile.copy(
                streakDays = newStreak,
                longestStreakDays = newLongest,
                weeklyCompleted = newWeeklyCompleted,
                totalWorkouts = newTotalWorkouts,
                totalMinutes = newTotalMinutes,
                todayVsOldScore = newScore,
                xp = newXp,
                level = newLevel
            )
        )

        // Check milestones
        val todayStr = SimpleDateFormat("d MMMM yyyy", Locale("tr")).format(Date())
        if (newTotalWorkouts >= 1) {
            journeyDao.unlockMilestone("first_workout", todayStr)
        }
        if (newStreak >= 7) {
            journeyDao.unlockMilestone("streak_7", todayStr)
        }
        if (newStreak >= 14) {
            journeyDao.unlockMilestone("streak_14", todayStr)
        }
        if (newStreak >= 30) {
            journeyDao.unlockMilestone("streak_30", todayStr)
        }
        if (newTotalWorkouts >= 100) {
            journeyDao.unlockMilestone("workouts_100", todayStr)
        }
    }

    suspend fun completeCrisisMicroAction(actionType: String, actionTitle: String, minutes: Int) {
        workoutDao.insertWorkoutLog(
            WorkoutLogEntity(
                planTitle = "Kriz Modu: $actionTitle",
                timestamp = System.currentTimeMillis(),
                durationMinutes = minutes,
                completedExercisesCount = 1,
                isCrisisRescue = true,
                crisisType = actionType
            )
        )

        val profile = userDao.getUserProfileSync() ?: UserProfile()
        val newCrisisCount = profile.crisisRescuesCount + 1
        // Crisis actions preserve streak and add discipline points without punishment
        val newStreak = profile.streakDays + 1
        val newLongest = maxOf(profile.longestStreakDays, newStreak)
        val newScore = minOf(100, profile.todayVsOldScore + 3) // Big reward for overcoming resistance
        val newTotalMinutes = profile.totalMinutes + minutes
        val addedXp = 50
        val newXp = profile.xp + addedXp
        val newLevel = DisciplineLevelEngine.getLevelInfo(newXp).level

        userDao.update(
            profile.copy(
                streakDays = newStreak,
                longestStreakDays = newLongest,
                crisisRescuesCount = newCrisisCount,
                todayVsOldScore = newScore,
                totalMinutes = newTotalMinutes,
                xp = newXp,
                level = newLevel
            )
        )

        val todayStr = SimpleDateFormat("d MMMM yyyy", Locale("tr")).format(Date())
        journeyDao.unlockMilestone("crisis_turn", todayStr)
        if (newCrisisCount >= 5) {
            journeyDao.unlockMilestone("iron_will", todayStr)
        }
    }

    suspend fun resetAllProgressToZero() {
        val current = userDao.getUserProfileSync() ?: UserProfile()
        userDao.update(
            current.copy(
                streakDays = 0,
                longestStreakDays = 0,
                weeklyCompleted = 0,
                totalWorkouts = 0,
                totalMinutes = 0,
                todayVsOldScore = 50,
                crisisRescuesCount = 0,
                level = 0,
                xp = 0
            )
        )
    }

    suspend fun updateTheme(themeId: String) {
        val current = userDao.getUserProfileSync() ?: return
        userDao.update(current.copy(selectedTheme = themeId))
    }

    suspend fun updateTodayVsOldScore(newScore: Int) {
        val current = userDao.getUserProfileSync() ?: return
        if (current.todayVsOldScore != newScore) {
            userDao.update(current.copy(todayVsOldScore = newScore))
        }
    }
}
