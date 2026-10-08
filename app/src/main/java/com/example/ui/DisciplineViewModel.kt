package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DisciplineDatabase
import com.example.data.DisciplineEngine
import com.example.data.DisciplineHabit
import com.example.data.DisciplineRepository
import com.example.data.JourneyMilestoneEntity
import com.example.data.MotivationQuote
import com.example.data.NotificationHelper
import com.example.data.NotificationPreferences
import com.example.data.NotificationSettings
import com.example.data.PlanExerciseEntity
import com.example.data.StruggleEngine
import com.example.data.StruggleEvaluationResult
import com.example.data.TransformRepository
import com.example.data.UserProfile
import com.example.data.WorkoutLogEntity
import com.example.data.WorkoutPlanEntity
import com.example.ui.theme.AppTheme
import com.example.ui.theme.AppThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveExerciseState(
    val exercise: PlanExerciseEntity,
    val completedSets: MutableList<Boolean> = mutableListOf(),
    val currentWeightKg: Float,
    val currentReps: Int
)

data class ActiveWorkoutSessionState(
    val plan: WorkoutPlanEntity,
    val exercises: List<ActiveExerciseState> = emptyList(),
    val currentExerciseIndex: Int = 0,
    val startTimestamp: Long = System.currentTimeMillis(),
    val isFinished: Boolean = false
)

class DisciplineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DisciplineRepository(DisciplineDatabase.getDatabase(application))
    private val transformRepo = TransformRepository.getInstance(application)

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val workoutPlans: StateFlow<List<WorkoutPlanEntity>> = repository.workoutPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val milestones: StateFlow<List<JourneyMilestoneEntity>> = repository.milestones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutLogs: StateFlow<List<WorkoutLogEntity>> = repository.workoutLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeSession = MutableStateFlow<ActiveWorkoutSessionState?>(null)
    val activeSession: StateFlow<ActiveWorkoutSessionState?> = _activeSession.asStateFlow()

    private val _inCrisisMode = MutableStateFlow(false)
    val inCrisisMode: StateFlow<Boolean> = _inCrisisMode.asStateFlow()

    private val _recentCrisisCompleted = MutableStateFlow(false)
    val recentCrisisCompleted: StateFlow<Boolean> = _recentCrisisCompleted.asStateFlow()

    private val _showNotificationDialog = MutableStateFlow(false)
    val showNotificationDialog: StateFlow<Boolean> = _showNotificationDialog.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationPreferences.getSettings(application))
    val notificationSettings: StateFlow<NotificationSettings> = _notificationSettings.asStateFlow()

    private val _dailyHabits = MutableStateFlow(DisciplineEngine.getDefaultHabits())
    val dailyHabits: StateFlow<List<DisciplineHabit>> = _dailyHabits.asStateFlow()

    private val _currentQuoteIndex = MutableStateFlow(0)
    val currentQuoteIndex: StateFlow<Int> = _currentQuoteIndex.asStateFlow()

    private val _favoriteQuoteIds = MutableStateFlow<Set<String>>(setOf("q1", "q5"))
    val favoriteQuoteIds: StateFlow<Set<String>> = _favoriteQuoteIds.asStateFlow()

    val motivationQuotes: List<MotivationQuote> = DisciplineEngine.motivationQuotes

    val struggleEvaluation: StateFlow<StruggleEvaluationResult> = combine(
        repository.userProfile,
        _dailyHabits,
        repository.workoutLogs,
        transformRepo.completedTasks
    ) { profile: UserProfile?, habits: List<DisciplineHabit>, logs: List<WorkoutLogEntity>, transformTasks: Set<String> ->
        StruggleEngine.evaluate(
            profile = profile,
            dailyHabits = habits,
            workoutLogs = logs,
            completedTasksCount = transformTasks.size,
            lifeAnalysis = transformRepo.lifeAnalysis.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StruggleEngine.evaluate(null, DisciplineEngine.getDefaultHabits(), emptyList())
    )

    init {
        AppThemeManager.init(application)
        viewModelScope.launch {
            repository.ensureInitialized()
        }
        viewModelScope.launch {
            struggleEvaluation.collect { eval ->
                repository.updateTodayVsOldScore(eval.todayScore)
            }
        }
    }

    fun toggleHabit(habitId: String) {
        _dailyHabits.value = _dailyHabits.value.map {
            if (it.id == habitId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun nextMotivationQuote() {
        val nextIdx = (_currentQuoteIndex.value + 1) % motivationQuotes.size
        _currentQuoteIndex.value = nextIdx
    }

    fun selectMotivationQuote(index: Int) {
        if (index in motivationQuotes.indices) {
            _currentQuoteIndex.value = index
        }
    }

    fun toggleFavoriteQuote(quoteId: String) {
        val current = _favoriteQuoteIds.value.toMutableSet()
        if (current.contains(quoteId)) {
            current.remove(quoteId)
        } else {
            current.add(quoteId)
        }
        _favoriteQuoteIds.value = current
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            repository.updateName(newName)
        }
    }

    fun openCrisisMode() {
        _inCrisisMode.value = true
    }

    fun closeCrisisMode() {
        _inCrisisMode.value = false
    }

    fun toggleNotificationDialog(show: Boolean) {
        _showNotificationDialog.value = show
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        NotificationPreferences.saveSettings(getApplication(), settings)
        _notificationSettings.value = settings
    }

    fun sendWorkoutNotification(planTitle: String? = null) {
        val context = getApplication<Application>()
        val profile = userProfile.value
        val userName = profile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        val todayIdx = getTodayDayOfWeekIndex()
        val deviceDayName = NotificationHelper.getDeviceDayName(todayIdx)

        val todayPlan = workoutPlans.value.firstOrNull { it.dayOfWeek == todayIdx && !it.isCustom }
            ?: workoutPlans.value.firstOrNull { it.dayOfWeek == todayIdx }
            ?: workoutPlans.value.firstOrNull { !it.isCustom }

        val chosenTitle = planTitle ?: todayPlan?.title ?: "$deviceDayName İdmanı"
        val targetMuscles = todayPlan?.targetMuscles
        val dayName = todayPlan?.dayName ?: deviceDayName

        NotificationHelper.sendWorkoutNotification(
            context = context,
            planTitle = chosenTitle,
            userName = userName,
            dayName = dayName,
            targetMuscles = targetMuscles
        )
    }

    fun sendMotivationNotification(customQuote: MotivationQuote? = null) {
        val context = getApplication<Application>()
        val profile = userProfile.value
        val userName = profile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        val personalizedQuotes = DisciplineEngine.getPersonalizedQuotes(profile)
        val quote = customQuote ?: personalizedQuotes.getOrNull(currentQuoteIndex.value % personalizedQuotes.size)
            ?: personalizedQuotes.firstOrNull()
            ?: motivationQuotes.first()
        NotificationHelper.sendMotivationNotification(
            context = context,
            quote = quote.quote,
            author = quote.author,
            userName = userName
        )
    }

    fun completeCrisisAction(actionType: String, actionTitle: String, minutes: Int) {
        viewModelScope.launch {
            repository.completeCrisisMicroAction(actionType, actionTitle, minutes)
            _recentCrisisCompleted.value = true
            _inCrisisMode.value = false
        }
    }

    fun startWorkoutSession(plan: WorkoutPlanEntity) {
        viewModelScope.launch {
            val exercises = repository.getExercisesForPlanSync(plan.id)
            val activeExercises = exercises.map { ex ->
                ActiveExerciseState(
                    exercise = ex,
                    completedSets = MutableList(ex.targetSets) { false },
                    currentWeightKg = if (ex.lastWeightKg > 0f) ex.lastWeightKg + 5f else 0f, // progressive overload
                    currentReps = ex.targetReps
                )
            }
            _activeSession.value = ActiveWorkoutSessionState(
                plan = plan,
                exercises = activeExercises,
                currentExerciseIndex = 0,
                startTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun completeCurrentSet(exerciseIndex: Int, setIndex: Int, weight: Float, reps: Int) {
        val current = _activeSession.value ?: return
        if (exerciseIndex in current.exercises.indices) {
            val exerciseState = current.exercises[exerciseIndex]
            if (setIndex in exerciseState.completedSets.indices) {
                exerciseState.completedSets[setIndex] = true
            }
            // Trigger recomposition
            _activeSession.value = current.copy(
                exercises = current.exercises.mapIndexed { idx, it ->
                    if (idx == exerciseIndex) {
                        it.copy(currentWeightKg = weight, currentReps = reps)
                    } else it
                }
            )
        }
    }

    fun nextExercise() {
        val current = _activeSession.value ?: return
        if (current.currentExerciseIndex < current.exercises.size - 1) {
            _activeSession.value = current.copy(currentExerciseIndex = current.currentExerciseIndex + 1)
        }
    }

    fun previousExercise() {
        val current = _activeSession.value ?: return
        if (current.currentExerciseIndex > 0) {
            _activeSession.value = current.copy(currentExerciseIndex = current.currentExerciseIndex - 1)
        }
    }

    fun finishWorkoutSession() {
        val current = _activeSession.value ?: return
        val durationMinutes = ((System.currentTimeMillis() - current.startTimestamp) / (1000 * 60)).toInt().coerceAtLeast(1)
        val completedCount = current.exercises.count { ex -> ex.completedSets.any { it } }
        val weightsMap = current.exercises.associate { it.exercise.id to it.currentWeightKg }

        viewModelScope.launch {
            repository.completeWorkoutSession(
                planTitle = current.plan.title,
                durationMinutes = durationMinutes,
                completedExercisesCount = completedCount,
                updatedWeights = weightsMap
            )
            _activeSession.value = null
        }
    }

    fun cancelWorkoutSession() {
        _activeSession.value = null
    }

    fun saveOnboarding(
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
        viewModelScope.launch {
            repository.saveOnboarding(
                name = name,
                reason = reason,
                problem = problem,
                desiredPerson = desiredPerson,
                profession = profession,
                fitnessLevel = fitnessLevel,
                workoutLocation = workoutLocation,
                targetGoal = targetGoal,
                motivationStyle = motivationStyle
            )
        }
    }

    fun updateTrainingPreferences(
        fitnessLevel: String,
        workoutLocation: String,
        targetGoal: String,
        motivationStyle: String
    ) {
        viewModelScope.launch {
            repository.updateTrainingPreferences(
                fitnessLevel = fitnessLevel,
                workoutLocation = workoutLocation,
                targetGoal = targetGoal,
                motivationStyle = motivationStyle
            )
        }
    }

    fun updateProfession(newProfession: String) {
        viewModelScope.launch {
            repository.updateProfession(newProfession)
        }
    }

    fun applyProfessionWorkouts(profession: String) {
        viewModelScope.launch {
            repository.applyProfessionWorkouts(profession)
        }
    }

    fun getTodayDayOfWeekIndex(): Int {
        return when (java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)) {
            java.util.Calendar.MONDAY -> 1
            java.util.Calendar.TUESDAY -> 2
            java.util.Calendar.WEDNESDAY -> 3
            java.util.Calendar.THURSDAY -> 4
            java.util.Calendar.FRIDAY -> 5
            java.util.Calendar.SATURDAY -> 6
            java.util.Calendar.SUNDAY -> 7
            else -> 1
        }
    }

    fun updateGoal(newGoal: String) {
        viewModelScope.launch {
            repository.updateGoal(newGoal)
        }
    }

    fun updateIdentity(newIdentity: String) {
        viewModelScope.launch {
            repository.updateIdentity(newIdentity)
        }
    }

    fun createCustomPlan(
        title: String,
        muscles: String,
        minutes: Int,
        exercises: List<Pair<String, Pair<Int, Int>>>
    ) {
        viewModelScope.launch {
            repository.createCustomPlan(title, muscles, minutes, exercises)
        }
    }

    fun applyTheme(theme: AppTheme) {
        AppThemeManager.setTheme(getApplication<Application>(), theme)
        viewModelScope.launch {
            repository.updateTheme(theme.id)
        }
    }

    fun resetAllStatsToZero() {
        viewModelScope.launch {
            repository.resetAllProgressToZero()
        }
    }

    fun getDailyMessage(profile: UserProfile?): String {
        return DisciplineEngine.getDailyDisciplineMessage(
            profile = profile,
            streakDays = profile?.streakDays ?: 0,
            crisisCount = profile?.crisisRescuesCount ?: 0,
            recentCrisisJustCompleted = _recentCrisisCompleted.value
        )
    }
}
