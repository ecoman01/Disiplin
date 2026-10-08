package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.BillingManager
import com.example.data.NotificationHelper
import com.example.data.NotificationPreferences
import com.example.data.TransformRepository
import com.example.ui.DisciplineViewModel
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.AnalysisScreen
import com.example.ui.screens.CrisisModeScreen
import com.example.ui.screens.DisciplineNotificationDialog
import com.example.ui.screens.JourneyScreen
import com.example.ui.screens.MotivationScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ThemeSelectionDialog
import com.example.ui.screens.TodayScreen
import com.example.ui.screens.WorkoutsScreen
import com.example.ui.screens.transform.TransformMainScreen
import com.example.ui.theme.*

enum class DisciplineTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    TODAY("Bugün", Icons.Filled.Home, Icons.Outlined.Home),
    TRANSFORM("TRANSFORM", Icons.Filled.LocalFireDepartment, Icons.Outlined.LocalFireDepartment),
    WORKOUTS("İdman", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    MOTIVATION("Zihin AI", Icons.Filled.Bolt, Icons.Outlined.Bolt),
    JOURNEY("Yolculuk", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    PROFILE("Profil", Icons.Filled.Person, Icons.Outlined.Person)
}

class MainActivity : ComponentActivity() {
    private val viewModel: DisciplineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        BillingManager.initialize(this)
        NotificationHelper.createNotificationChannels(this)
        val navigateTo = intent?.getStringExtra("navigate_to")
        setContent {
            MyApplicationTheme {
                DisciplineApp(viewModel = viewModel, initialNavigateTo = navigateTo)
            }
        }
    }
}

@Composable
fun DisciplineApp(viewModel: DisciplineViewModel, initialNavigateTo: String? = null) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val workoutPlans by viewModel.workoutPlans.collectAsState()
    val milestones by viewModel.milestones.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()
    val inCrisisMode by viewModel.inCrisisMode.collectAsState()
    val showNotificationDialog by viewModel.showNotificationDialog.collectAsState()
    val notificationSettings by viewModel.notificationSettings.collectAsState()
    val dailyHabits by viewModel.dailyHabits.collectAsState()
    val workoutLogs by viewModel.workoutLogs.collectAsState()
    val struggleEvaluation by viewModel.struggleEvaluation.collectAsState()
    val currentQuoteIndex by viewModel.currentQuoteIndex.collectAsState()
    val favoriteQuoteIds by viewModel.favoriteQuoteIds.collectAsState()

    // Bildirim izni launcher (Android 13+ / API 33+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        NotificationPreferences.setAskedPermission(context, true)
        if (isGranted) {
            val settings = NotificationPreferences.getSettings(context)
            NotificationPreferences.saveSettings(context, settings)
        }
    }

    // İlk girişte otomatik sistem bildirim iznini iste
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationHelper.areNotificationsEnabled(context) && !NotificationPreferences.hasAskedPermission(context)) {
                NotificationPreferences.setAskedPermission(context, true)
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val isTransformUnlocked by BillingManager.isTransformUnlocked.collectAsState()
    val transformRepository = remember { TransformRepository(context) }

    var currentTab by remember {
        mutableStateOf(
            when (initialNavigateTo) {
                "transform" -> DisciplineTab.TRANSFORM
                "workouts" -> DisciplineTab.WORKOUTS
                "motivation" -> DisciplineTab.MOTIVATION
                else -> DisciplineTab.TODAY
            }
        )
    }
    var forceShowOnboarding by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showRecipesSheet by remember { mutableStateOf(false) }

    // If onboarding is not completed or user explicitly requested to retake it
    val needsOnboarding = forceShowOnboarding || (userProfile != null && !userProfile!!.isOnboardingCompleted)

    if (needsOnboarding) {
        OnboardingScreen(
            initialName = userProfile?.name?.takeIf { it != "Emre" } ?: "",
            onComplete = { name, reason, problem, desiredPerson, profession, fitnessLevel, workoutLocation, targetGoal, motivationStyle ->
                viewModel.saveOnboarding(
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
                forceShowOnboarding = false
            }
        )
        return
    }

    // Active Workout takes precedence for focus
    if (activeSession != null) {
        ActiveWorkoutScreen(
            session = activeSession!!,
            onCompleteSet = { exIdx, setIdx, weight, reps ->
                viewModel.completeCurrentSet(exIdx, setIdx, weight, reps)
            },
            onNextExercise = { viewModel.nextExercise() },
            onPrevExercise = { viewModel.previousExercise() },
            onFinishWorkout = { viewModel.finishWorkoutSession() },
            onCancelWorkout = { viewModel.cancelWorkoutSession() }
        )
        return
    }

    // Crisis Mode takes over screen for calm minimalism
    if (inCrisisMode) {
        CrisisModeScreen(
            onClose = { viewModel.closeCrisisMode() },
            onCompleteCrisisAction = { type, title, minutes ->
                viewModel.completeCrisisAction(type, title, minutes)
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = DarkNavBackground,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .border(
                        androidx.compose.foundation.BorderStroke(
                            1.dp,
                            DarkBorder
                        )
                    )
                    .testTag("main_bottom_nav")
            ) {
                DisciplineTab.values().forEach { tab ->
                    val selected = currentTab == tab
                    val tabIcon = if (tab == DisciplineTab.TRANSFORM) {
                        if (isTransformUnlocked) {
                            if (selected) Icons.Filled.LocalFireDepartment else Icons.Outlined.LocalFireDepartment
                        } else {
                            if (selected) Icons.Filled.Lock else Icons.Outlined.Lock
                        }
                    } else {
                        if (selected) tab.selectedIcon else tab.unselectedIcon
                    }

                    val tabTitle = if (tab == DisciplineTab.TRANSFORM) {
                        if (isTransformUnlocked) "🔥 Transform" else "🔒 Transform"
                    } else {
                        tab.title
                    }

                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tabIcon,
                                contentDescription = tabTitle
                            )
                        },
                        label = {
                            Text(
                                text = tabTitle,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TacticalGreenBright,
                            selectedTextColor = TacticalGreenBright,
                            indicatorColor = DarkNavPill,
                            unselectedIconColor = TextSecondary.copy(alpha = 0.6f),
                            unselectedTextColor = TextSecondary.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { targetTab ->
                when (targetTab) {
                    DisciplineTab.TODAY -> {
                        val todayDayIdx = viewModel.getTodayDayOfWeekIndex()
                        val todayPlan = workoutPlans.firstOrNull { it.dayOfWeek == todayDayIdx && !it.isCustom }
                            ?: workoutPlans.firstOrNull { !it.isCustom }

                        TodayScreen(
                            userProfile = userProfile,
                            dailyMessage = viewModel.getDailyMessage(userProfile),
                            todayPlan = todayPlan,
                            dailyHabits = dailyHabits,
                            onStartWorkout = { plan ->
                                viewModel.startWorkoutSession(plan)
                            },
                            onOpenCrisis = { viewModel.openCrisisMode() },
                            onOpenNotification = { viewModel.toggleNotificationDialog(true) },
                            onOpenThemes = { showThemeDialog = true },
                            onToggleHabit = { viewModel.toggleHabit(it) },
                            onUpdateName = { viewModel.updateName(it) },
                            onOpenMotivation = { currentTab = DisciplineTab.MOTIVATION },
                            onOpenTransform = { currentTab = DisciplineTab.TRANSFORM },
                            onOpenRecipes = { showRecipesSheet = true }
                        )
                    }
                    DisciplineTab.TRANSFORM -> {
                        TransformMainScreen(
                            repository = transformRepository,
                            userName = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
                        )
                    }
                    DisciplineTab.MOTIVATION -> {
                        MotivationScreen(
                            quotes = viewModel.motivationQuotes,
                            currentQuoteIndex = currentQuoteIndex,
                            favoriteQuoteIds = favoriteQuoteIds,
                            dailyHabits = dailyHabits,
                            onNextQuote = { viewModel.nextMotivationQuote() },
                            onSelectQuote = { viewModel.selectMotivationQuote(it) },
                            onToggleFavorite = { viewModel.toggleFavoriteQuote(it) },
                            onToggleHabit = { viewModel.toggleHabit(it) },
                            userName = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı",
                            userProfession = userProfile?.profession ?: "Masa Başı & Ofis / Yazılımcı",
                            userProfile = userProfile
                        )
                    }
                    DisciplineTab.WORKOUTS -> {
                        WorkoutsScreen(
                            plans = workoutPlans,
                            workoutLogs = workoutLogs,
                            currentDayOfWeek = viewModel.getTodayDayOfWeekIndex(),
                            userProfession = userProfile?.profession ?: "Masa Başı & Ofis / Yazılımcı",
                            userName = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı",
                            streakDays = userProfile?.streakDays ?: 0,
                            userProfile = userProfile,
                            onSelectPlan = { plan ->
                                viewModel.startWorkoutSession(plan)
                            },
                            onCreateCustomPlan = { title, muscles, minutes, exercises ->
                                viewModel.createCustomPlan(title, muscles, minutes, exercises)
                            },
                            onResetToProfessionWorkouts = { prof ->
                                viewModel.applyProfessionWorkouts(prof)
                            },
                            onRetakeOnboarding = { forceShowOnboarding = true }
                        )
                    }
                    DisciplineTab.JOURNEY -> {
                        JourneyScreen(
                            milestones = milestones,
                            userProfile = userProfile
                        )
                    }
                    DisciplineTab.PROFILE -> {
                        ProfileScreen(
                            userProfile = userProfile,
                            dailyHabits = dailyHabits,
                            workoutLogs = workoutLogs,
                            struggleEvaluation = struggleEvaluation,
                            onUpdateName = { viewModel.updateName(it) },
                            onUpdateGoal = { viewModel.updateGoal(it) },
                            onUpdateIdentity = { viewModel.updateIdentity(it) },
                            onUpdateProfession = { viewModel.updateProfession(it) },
                            onResetOnboarding = { forceShowOnboarding = true },
                            onOpenNotificationSettings = { viewModel.toggleNotificationDialog(true) },
                            onOpenThemes = { showThemeDialog = true },
                            onResetStats = { viewModel.resetAllStatsToZero() }
                        )
                    }
                }
            }
        }
    }

    if (showNotificationDialog) {
        val todayIdx = viewModel.getTodayDayOfWeekIndex()
        val todayPlan = workoutPlans.firstOrNull { it.dayOfWeek == todayIdx && !it.isCustom }
            ?: workoutPlans.firstOrNull { it.dayOfWeek == todayIdx }
            ?: workoutPlans.firstOrNull { !it.isCustom }

        DisciplineNotificationDialog(
            notificationSettings = notificationSettings,
            userProfile = userProfile,
            todayPlanTitle = todayPlan?.title,
            todayDayName = todayPlan?.dayName ?: com.example.data.NotificationHelper.getDeviceDayName(todayIdx),
            onUpdateSettings = { viewModel.updateNotificationSettings(it) },
            onSendWorkoutNotification = { viewModel.sendWorkoutNotification() },
            onSendMotivationNotification = { viewModel.sendMotivationNotification() },
            onDismiss = { viewModel.toggleNotificationDialog(false) }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            viewModel = viewModel,
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showRecipesSheet) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showRecipesSheet = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground)
            ) {
                com.example.ui.screens.nutrition.NutritionRecipesSection(
                    onClose = { showRecipesSheet = false }
                )
            }
        }
    }
}
