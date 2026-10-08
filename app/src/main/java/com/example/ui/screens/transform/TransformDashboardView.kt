package com.example.ui.screens.transform

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

enum class TransformSection(val title: String, val icon: String) {
    ROADMAP("Program", "🎯"),
    ANALYSIS("Analiz", "🧠"),
    AI_COACH("AI Koç", "🤖"),
    STRUGGLE("Mücadele", "⚔️"),
    SPORT("Spor", "🏋️"),
    CHALLENGES("Challenge", "🧨"),
    WEEKLY_REPORT("Rapor", "📝")
}

@Composable
fun TransformDashboardView(
    repository: TransformRepository,
    userName: String = "Savaşçı",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedSection by remember { mutableStateOf(TransformSection.ROADMAP) }
    var showAnalysisModal by remember { mutableStateOf(false) }

    // Repository states
    val isAnalysisCompleted by repository.isAnalysisCompleted.collectAsState()
    val lifeAnalysis by repository.lifeAnalysis.collectAsState()
    val programDuration by repository.programDuration.collectAsState()
    val currentDay by repository.currentDay.collectAsState()
    val completedTasks by repository.completedTasks.collectAsState()
    val aiDecisions by repository.aiDecisions.collectAsState()
    val coachMode by repository.coachIntensityMode.collectAsState()
    val advancedStats by repository.advancedStats.collectAsState()
    val activeSportGoal by repository.activeSportGoal.collectAsState()
    val sportExercises by repository.sportExercises.collectAsState()
    val challenges by repository.challenges.collectAsState()
    val weeklyReports by repository.weeklyReports.collectAsState()

    // Enforce Life Analysis on first entry
    LaunchedEffect(isAnalysisCompleted) {
        if (!isAnalysisCompleted) {
            showAnalysisModal = true
        }
    }

    val currentDayPlan = remember(currentDay) {
        repository.getDayPlan(currentDay)
    }
    val completedTodayCount = currentDayPlan.tasks.count { completedTasks.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("transform_dashboard")
    ) {
        // TOP STATUS BAR (NON-OVERFLOWING, RESPONSIVE HEADER)
        Surface(
            color = DarkCardBackground,
            border = BorderStroke(0.5.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header row with title & compact AI Koç badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = TacticalGreenBright.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "🔥 AKTİF",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "TRANSFORM",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    // Active Coach Status badge (compact, never wraps or overflows)
                    Surface(
                        color = Color(0xFF1E262A),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TacticalGreenBright)
                            )
                            Text(
                                text = "AI Koç: $coachMode",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Daily Progress Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$currentDay. Gün:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "$completedTodayCount/4 Görev",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (completedTodayCount == 4) TacticalGreenBright else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Surface(
                        color = if (completedTodayCount == 4) TacticalGreenBright.copy(alpha = 0.2f) else DarkSurface,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.5.dp, if (completedTodayCount == 4) TacticalGreenBright else DarkBorder)
                    ) {
                        Text(
                            text = if (completedTodayCount == 4) "🏆 GÜN TAMAMLANDI" else "%${completedTodayCount * 25} Tamamlandı",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (completedTodayCount == 4) TacticalGreenBright else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Smooth horizontal navigation pills that never overflow
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(TransformSection.values()) { section ->
                        val isSelected = selectedSection == section
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) TacticalGreenBright else DarkSurface,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) TacticalGreenBright else DarkBorder
                            ),
                            modifier = Modifier.clickable { selectedSection = section }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(text = section.icon, fontSize = 12.sp)
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        color = if (isSelected) Color.Black else TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // CELEBRATORY FULL DAY VICTORY BANNER
        if (completedTodayCount == 4) {
            Surface(
                color = TacticalGreenBright.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🏆", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "GÜNÜN 4 GÖREVİ DE TAMAMLANDI!",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = "Tavizsiz irade zaferle sonuçlandı. +275 XP kazanıldı & dönüşüm serisi korundu.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // MANDATORY LIFE ANALYSIS NOTICE BANNER IF NOT COMPLETED
        if (!isAnalysisCompleted) {
            Surface(
                color = Color(0xFF2C1E14),
                border = BorderStroke(1.dp, Color(0xFFFF9800)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⚠️ Hayat Analizi Bekleniyor",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color(0xFFFFB74D),
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = "TRANSFORM programı ve AI Koçunu başlatmak için 8 soruluk analizi tamamla.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showAnalysisModal = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Başlat (8 Soru)", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // MAIN CONTENT AREA BY SECTION
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedSection) {
                TransformSection.ROADMAP -> RoadmapView(
                    dayPlan = currentDayPlan,
                    currentDay = currentDay,
                    programDuration = programDuration,
                    completedTasks = completedTasks,
                    onToggleTask = { repository.toggleTask(it) },
                    onSelectDay = { repository.setCurrentDay(it) },
                    onSelectDuration = { repository.setProgramDuration(it) }
                )
                TransformSection.ANALYSIS -> LifeAnalysisResultView(
                    profile = lifeAnalysis,
                    onRetake = { showAnalysisModal = true }
                )
                TransformSection.AI_COACH -> AutonomousAiCoachView(
                    decisions = aiDecisions,
                    activeMode = coachMode,
                    lifeAnalysis = lifeAnalysis,
                    completedTasksCount = completedTasks.size,
                    onTriggerTactic = { repository.triggerAiCoachTactic(it) },
                    onSimulateCheck = { repository.simulateAiPerformanceCheck(it) },
                    onIntensityChange = { repository.setCoachIntensity(it) },
                    onMarkCompleted = { repository.markAiDecisionCompleted(it) },
                    onConsultAi = { topic, query -> repository.consultAiCoach(topic, query) }
                )
                TransformSection.STRUGGLE -> KendinleMucadeleView(
                    stats = advancedStats,
                    onNavigateToSection = { selectedSection = it }
                )
                TransformSection.SPORT -> PersonalSportSystemView(
                    activeGoal = activeSportGoal,
                    exercises = sportExercises,
                    onSelectGoal = { repository.setSportGoal(it) },
                    onLogSet = { repository.logExerciseSet(it) },
                    onResetSets = { repository.resetExerciseSets() },
                    onUpdateWeight = { name, delta -> repository.updateExerciseWeight(name, delta) }
                )
                TransformSection.CHALLENGES -> ChallengesSystemView(
                    challenges = challenges,
                    onClaim = { repository.claimChallenge(it) }
                )
                TransformSection.WEEKLY_REPORT -> WeeklyReportView(
                    reports = weeklyReports,
                    userName = userName
                )
            }
        }
    }

    if (showAnalysisModal) {
        LifeAnalysisDiagnosticDialog(
            onDismiss = { showAnalysisModal = false },
            onSaveProfile = { profile ->
                repository.saveAnalysisProfile(profile)
                showAnalysisModal = false
            },
            isMandatory = !isAnalysisCompleted
        )
    }
}

// ----------------------------------------------------
// 1. 🎯 30 / 60 / 90 GÜNLÜK ADAPTİF YOL HARİTASI
// ----------------------------------------------------
@Composable
private fun RoadmapView(
    dayPlan: TransformDay,
    currentDay: Int,
    programDuration: Int,
    completedTasks: Set<String>,
    onToggleTask: (String) -> Unit,
    onSelectDay: (Int) -> Unit,
    onSelectDuration: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // DURATION SELECTOR (30 / 60 / 90 GÜN)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎯 DÖNÜŞÜM HEDEFİ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "$programDuration Günlük Sistem",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(30, 60, 90).forEach { days ->
                        val isPicked = programDuration == days
                        OutlinedButton(
                            onClick = { onSelectDuration(days) },
                            modifier = Modifier.weight(1f).height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isPicked) TacticalGreenBright.copy(alpha = 0.15f) else Color.Transparent,
                                contentColor = if (isPicked) TacticalGreenBright else TextSecondary
                            ),
                            border = BorderStroke(1.dp, if (isPicked) TacticalGreenBright else DarkBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "$days Gün",
                                fontWeight = if (isPicked) FontWeight.Black else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // DAY SELECTOR & PHASE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = dayPlan.phase.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = dayPlan.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                    }

                    Surface(
                        color = TacticalGreenBright,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "GÜN $currentDay",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        )
                    }
                }

                Text(
                    text = dayPlan.quote,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )

                // Phase Progress Bar
                val currentPhaseNumber = when {
                    currentDay <= 30 -> 1
                    currentDay <= 60 -> 2
                    else -> 3
                }
                val phaseStartDay = (currentPhaseNumber - 1) * 30 + 1
                val phaseEndDay = (currentPhaseNumber * 30).coerceAtMost(programDuration)
                val daysInPhase = (phaseEndDay - phaseStartDay + 1).coerceAtLeast(1)
                val currentInPhase = (currentDay - phaseStartDay + 1).coerceIn(1, daysInPhase)
                val phaseProgress = currentInPhase.toFloat() / daysInPhase.toFloat()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Faz $currentPhaseNumber İlerlemesi (Gün $phaseStartDay-$phaseEndDay)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "$currentInPhase / $daysInPhase Gün (%${(phaseProgress * 100).toInt()})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                    LinearProgressIndicator(
                        progress = { phaseProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TacticalGreenBright,
                        trackColor = DarkSurface
                    )
                }

                // Quick Day Picker Strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val maxDisplayDays = programDuration.coerceAtMost(90)
                    items(maxDisplayDays) { index ->
                        val dayNum = index + 1
                        val isCurrent = dayNum == currentDay
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCurrent) TacticalGreenBright else DarkSurface,
                            border = BorderStroke(1.dp, if (isCurrent) TacticalGreenBright else DarkBorder),
                            modifier = Modifier.clickable { onSelectDay(dayNum) }
                        ) {
                            Text(
                                text = "$dayNum",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Normal,
                                    color = if (isCurrent) Color.Black else TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }

        // DAILY 4 ESSENTIAL TASKS
        var expandedTaskId by remember { mutableStateOf<String?>(null) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BUGÜNÜN 4 TAVİZSİZ GÖREVİ",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = "Detay için göreve dokunun",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            )
        }

        dayPlan.tasks.forEach { task ->
            val isDone = completedTasks.contains(task.id)
            val isExpanded = expandedTaskId == task.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedTaskId = if (isExpanded) null else task.id
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (isDone) DarkCardBackground.copy(alpha = 0.6f) else DarkSurface
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    1.dp,
                    if (isDone) TacticalGreenBright.copy(alpha = 0.6f) else if (isExpanded) TacticalGreenBright.copy(alpha = 0.4f) else DarkBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDone) TacticalGreenBright else Color.Transparent)
                                .border(
                                    1.5.dp,
                                    if (isDone) TacticalGreenBright else DarkBorder,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onToggleTask(task.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = task.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = task.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                if (task.durationMinutes > 0) {
                                    Surface(
                                        color = DarkSurfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "⏱ ${task.durationMinutes} dk",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = TextSecondary,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDone) TextSecondary else TextPrimary
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = TacticalGreenBright.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "+${task.xpReward} XP",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Detay",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // EXPANDABLE TACTICAL GUIDE
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceVariant, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "💡", fontSize = 14.sp)
                                Text(
                                    text = "TAKTIK UYGULAMA PROTOKOLÜ",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = task.tacticalGuide.ifEmpty { "Bu görevi ertelemeden, dikkat dağıtıcı unsurları kaldırarak ve tam odaklanma ile tamamlayın." },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    lineHeight = 18.sp
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { onToggleTask(task.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isDone) DarkBorder else TacticalGreenBright,
                                        contentColor = if (isDone) TextPrimary else Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isDone) "Tamamlandı Olarak İşaretlendi ✓" else "Görevi Tamamla (+${task.xpReward} XP)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2. 🧠 KİŞİSEL HAYAT ANALİZİ
// ----------------------------------------------------
@Composable
private fun LifeAnalysisResultView(
    profile: LifeAnalysisProfile,
    onRetake: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HERO CARD: ANALİZ SONUCU
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "KİŞİSEL PROFİL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TacticalGreenBright,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = if (profile.isCompleted) "8 Boyutlu Hayat Teşhisi" else "Analiz Bekleniyor",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                    Button(
                        onClick = onRetake,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreenBright.copy(alpha = 0.15f),
                            contentColor = TacticalGreenBright
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (profile.isCompleted) "Analizi Yenile" else "Analizi Başlat",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 4 KEY METRICS TILES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ScoreBox(modifier = Modifier.weight(1f), label = "Disiplin", value = "${profile.disciplineScore}/100", highlight = true)
                    ScoreBox(modifier = Modifier.weight(1f), label = "Erteleme", value = profile.procrastinationLevel, isWarning = profile.procrastinationLevel == "Yüksek" || profile.procrastinationLevel == "Kritik")
                    ScoreBox(modifier = Modifier.weight(1f), label = "Spor", value = profile.sportLevel)
                    ScoreBox(modifier = Modifier.weight(1f), label = "Uyku", value = profile.sleepLevel, isWarning = profile.sleepLevel == "Düşük")
                }

                // CRITICAL DIAGNOSIS ALERT
                Surface(
                    color = Color(0xFF2A1C15),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFE57373).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "⚠️", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "EN KRİTİK ENGEL: ${profile.biggestProblem}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFB4AB)
                                )
                            )
                            Text(
                                text = "AI Koç sistemin bu zayıflığına göre uyarlandı. Gerektiğinde yoğunluğu düşürerek sistemden kopmanı engelleyecek.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // DETAILED RADAR SCORES
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "DETAYLI PARAMETRELER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Black
                    )
                )

                ScoreBar(title = "İçsel Motivasyon & Azim", value = profile.motivationScore, max = 100)
                ScoreBar(title = "Günlük Rutin & Düzen", value = profile.dailyRoutineScore, max = 100)
                ScoreBar(title = "Hedef Netliği & Vizyon", value = profile.goalClarityScore, max = 100)
            }
        }

        // 3 PERSONAL AI DEFENSIVE ARMORS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🛡️", fontSize = 18.sp)
                    Text(
                        text = "AI KİŞİSEL SAVUNMA ZIRHLARI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "🌅 Sabah Zırhı (İlk 15 Dk)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        Text(text = "Uyandıktan sonra ekrana bakmadan 500ml su iç ve 10 derin diyafram nefesi al. Zihnin berrak başlasın.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                    }
                }

                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "⚡ Erteleme Kalkanı (5 Dk Kuralı)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        Text(text = "Direnç hissettiğin göreve sadece 5 dakika ayıracağını söyleyerek başla. Beynin başlangıç bariyerini aş.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                    }
                }

                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "🌙 Gece Protokolü (23:00 Sınırı)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        Text(text = "Yatmadan 45 dk önce telefon bildirimlerini kapat. Yarının 1. öncelikli işini masana not bırak.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. 🤖 GELİŞTİRİLMİŞ KARAR VEREN AI KOÇ
// ----------------------------------------------------
@Composable
private fun AutonomousAiCoachView(
    decisions: List<AiCoachDecision>,
    activeMode: String,
    lifeAnalysis: LifeAnalysisProfile,
    completedTasksCount: Int,
    onTriggerTactic: (String) -> Unit,
    onSimulateCheck: (Int) -> Unit,
    onIntensityChange: (String) -> Unit,
    onMarkCompleted: (String) -> Unit,
    onConsultAi: (String, String) -> Unit
) {
    val scrollState = rememberScrollState()
    var customQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // COACH INTRO & LIVE DIAGNOSIS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TacticalGreenBright.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = TacticalGreenBright, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text(
                            text = "KARAR VEREN AI KOÇ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Durumunu analiz eder, taktik kararlar alır ve sistemi uyarlar.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                // AI COACH INTENSITY MODE SELECTOR
                Text(
                    text = "AI KOÇ ÇALIŞMA MODU",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val modes = listOf(
                        "Kriz / Asgari Yük" to "Kriz Modu",
                        "Adaptif Dengeli" to "Dengeli",
                        "Tavizsiz / Zirve Disiplin" to "Zirve Disiplin"
                    )
                    modes.forEach { (modeValue, modeLabel) ->
                        val isSelected = activeMode == modeValue
                        OutlinedButton(
                            onClick = { onIntensityChange(modeValue) },
                            modifier = Modifier.weight(1f).height(38.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) TacticalGreenBright.copy(alpha = 0.2f) else Color.Transparent,
                                contentColor = if (isSelected) TacticalGreenBright else TextSecondary
                            ),
                            border = BorderStroke(1.dp, if (isSelected) TacticalGreenBright else DarkBorder),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Text(
                                text = modeLabel,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Live dynamic diagnosis based on real usage
                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "⚡ GÜNCEL TEŞHİS & STRATEJİ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = if (completedTasksCount == 0) {
                                "İlk görev bekleniyor. Hedef: Giriş bariyerini yıkmak. Bugün sadece 1 görevi tamamlayıp sistemi başlat."
                            } else {
                                "Şu ana kadar $completedTasksCount görev tamamlandı. Aktif Mod: '$activeMode'. En büyük risk alanın '${lifeAnalysis.biggestProblem}'."
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Text(
                    text = "ANLIK KRİZ MÜDAHALESİ TALEP ET",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    )
                )

                // 4 Interactive Quick Crisis Interventions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onConsultAi("idman", "İdmandan kaçmak istiyorum, motivasyonum sıfır") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFB74D)),
                        border = BorderStroke(1.dp, Color(0xFFFFB74D).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("🏃‍♂️ İdmandan Kaçmak İstiyorum", fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    OutlinedButton(
                        onClick = { onConsultAi("telefon", "Sürekli telefona bakıyorum ve odaklanamıyorum") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                        border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("📱 Telefon Dikkat Dağınıklığı", fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onConsultAi("diyet", "Diyetim bozuldu, abur cubur krizi yaşıyorum") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64B5F6)),
                        border = BorderStroke(1.dp, Color(0xFF64B5F6).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("🥗 Diyet & Beslenme Krizi", fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    OutlinedButton(
                        onClick = { onTriggerTactic("peak_energy") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalGreenBright),
                        border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("⚡ Zirve Enerji / Zorlaştır", fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }

                // Interactive Custom Consult Field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customQuery,
                        onValueChange = { customQuery = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("AI Koç'a danışmak istediğin durumu yaz...", fontSize = 11.sp, color = TextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TacticalGreenBright,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    IconButton(
                        onClick = {
                            if (customQuery.isNotBlank()) {
                                onConsultAi("custom", customQuery)
                                customQuery = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .background(TacticalGreenBright, RoundedCornerShape(10.dp))
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Gönder", tint = Color.Black)
                    }
                }
            }
        }

        // AI DECISION FEED
        Text(
            text = "AI KOÇ KARARLARI & MÜDAHALELERİ",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
        )

        if (decisions.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🤖", fontSize = 24.sp)
                    Text(
                        text = "AI Koç Karar Geçmişi Temiz",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "Yukarıdaki kriz butonlarına dokunduğunda veya soru sorduğunda AI Koç kararları burada aktifleşir.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, textAlign = TextAlign.Center)
                    )
                }
            }
        } else {
            decisions.forEach { dec ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        if (dec.isActionCompleted) DisciplineGreen.copy(alpha = 0.5f) else DarkBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dec.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = when (dec.impactTag) {
                                    "Hafifletildi" -> Color(0xFF332014)
                                    "Acil Odak" -> Color(0xFF3B1515)
                                    "Beslenme" -> Color(0xFF15283B)
                                    else -> TacticalGreenBright.copy(alpha = 0.15f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = dec.impactTag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = when (dec.impactTag) {
                                            "Hafifletildi" -> Color(0xFFFFB74D)
                                            "Acil Odak" -> Color(0xFFFF8A80)
                                            "Beslenme" -> Color(0xFF90CAF9)
                                            else -> TacticalGreenBright
                                        },
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Tespit: ${dec.situation}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        Surface(
                            color = DarkCardBackground,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "⚡ Karar & Eylem: ${dec.actionApplied}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (dec.isActionCompleted) {
                                Surface(
                                    color = DisciplineGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, DisciplineGreen.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "✓ Taktik Uygulandı (+50 XP)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = DisciplineGreen,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = { onMarkCompleted(dec.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TacticalGreenBright,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Taktiği Uygula ✓ (+50 XP)", fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 4. ⚔️ KENDİNLE MÜCADELE (ESKİ SEN vs YENİ SEN)
// ----------------------------------------------------
@Composable
private fun KendinleMucadeleView(
    stats: AdvancedTransformStats,
    onNavigateToSection: (TransformSection) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HIGHLIGHT CARD: KENDİNLE MÜCADELE MİLADI
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "⚔️", fontSize = 20.sp)
                    Text(
                        text = "KENDİNLE MÜCADELE (ESKİ vs YENİ)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    )
                }

                if (stats.completedTasksCount == 0) {
                    Text(
                        text = "Sıfır Noktası: Mücadelen Bugün Başlıyor.",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Eski hayatındaki alışkanlıkların ile yeni inşa edeceğin disiplinli sen arasındaki fark burada canlı kıyaslanır. Tamamladığın her antrenman, su, zihin ve odak göreviyle 'Yeni Sen' güçlenecek.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                    )
                } else {
                    Text(
                        text = "Geçmişe Göre +%${stats.consistencyVsLastMonth} Daha Kararlısın.",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${stats.completedTasksCount} adet görev başarıyla tamamlandı. Eski erteleyen kimliğin ile yeni tavizsiz iraden arasındaki somut fark aşağıda hesaplandı.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                    )
                }
            }
        }

        // CANLI MÜCADELE DENGESİ: BUGÜNKÜ SEN vs ESKİ SEN
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, DarkBorderAccent)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stats.statusTitle,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = if (stats.todayScore >= 50) DisciplineGreen else WarningOrange
                            )
                        )
                        Text(
                            text = stats.statusDescription,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                }

                // Dual Scores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DisciplineGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, DisciplineGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(bottom = 3.dp)
                        ) {
                            Text(
                                text = "🔥 BUGÜNKÜ SEN",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DisciplineGreen,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            )
                        }
                        Text(
                            text = "%${stats.todayScore}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = DisciplineGreen
                            )
                        )
                    }

                    Text(
                        text = "VS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Black
                        )
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ComparisonOldYouSurface,
                            border = BorderStroke(1.dp, ComparisonOldYouBorder),
                            modifier = Modifier.padding(bottom = 3.dp)
                        ) {
                            Text(
                                text = "⏳ ESKİ SEN",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ComparisonOldYouText,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            )
                        }
                        Text(
                            text = "%${stats.oldScore}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = ComparisonOldYouText
                            )
                        )
                    }
                }

                // Ratio Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(DarkSurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(stats.todayScore.toFloat().coerceAtLeast(1f))
                            .fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(TacticalGreen, DisciplineGreen)))
                    )
                    Box(
                        modifier = Modifier
                            .weight(stats.oldScore.toFloat().coerceAtLeast(1f))
                            .fillMaxHeight()
                            .background(ComparisonOldYouBorder)
                    )
                }

                // Tactical action
                Surface(
                    color = TacticalGreenBright.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, TacticalGreenBright.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🎯", fontSize = 16.sp)
                            Text(
                                text = stats.nextActionToWin,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onNavigateToSection(TransformSection.ROADMAP) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TacticalGreenBright,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Hamleyi Yap ➔",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 PILLARS COMPARISON: ESKİ SEN vs YENİ SEN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "4 BOYUTTA ESKİ SEN vs YENİ SEN",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                )
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DisciplineGreen.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, DisciplineGreen.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "CANLI DÖNÜŞÜM",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DisciplineGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // 1. Antrenman & Spor
        ComparisonRowCard(
            title = "Antrenman & Fiziksel İrade",
            icon = "🏋️",
            oldMetric = "${stats.workoutsThen} İdman / Hafta",
            oldSub = "Sedanter, Hareketsiz",
            newMetric = "${stats.workoutsNow} İdman Tamamlandı",
            newSub = if (stats.workoutsNow > 0) "Aktif Kas Gelişimi" else "İlk İdman Bekleniyor",
            growth = if (stats.workoutsNow > stats.workoutsThen) "+${stats.workoutsNow - stats.workoutsThen} İdman Farkı" else "İlk adım bekleniyor",
            isPositive = stats.workoutsNow > 0,
            progress = (stats.workoutsNow.toFloat() / 12f).coerceIn(0.05f, 1f)
        )

        // 2. Uygulama Kullanımı & İrade Zinciri
        ComparisonRowCard(
            title = "İrade Zinciri & Süreklilik",
            icon = "⚡",
            oldMetric = "0 Gün Zincir",
            oldSub = "Düzensiz, Kolay Pes Eden",
            newMetric = "${stats.streakNow} Gün Zincir",
            newSub = if (stats.streakNow > 0) "Tavizsiz Kararlılık" else "1. Gün Mücadelesi",
            growth = if (stats.streakNow > 0) "${stats.streakNow} Gündür Kesintisiz" else "Mücadele Başladı",
            isPositive = stats.streakNow > 0,
            progress = (stats.streakNow.toFloat() / 21f).coerceIn(0.05f, 1f)
        )

        // 3. Zihin & Motive (Erteleme Direnci)
        ComparisonRowCard(
            title = "Motive & Zihinsel Odak",
            icon = "🧠",
            oldMetric = "%${stats.mindScoreThen} Başlangıç",
            oldSub = "Dopamin & Dikkat Dağınıklığı",
            newMetric = "%${stats.mindScoreNow} Zihin Gücü",
            newSub = "Stoik Odak & Erteleme Yok",
            growth = if (stats.mindScoreNow > stats.mindScoreThen) "+%${stats.mindScoreNow - stats.mindScoreThen} Zihin Gücü" else "Odak seansı yap",
            isPositive = stats.mindScoreNow > stats.mindScoreThen,
            progress = (stats.mindScoreNow.toFloat() / 100f).coerceIn(0.1f, 1f)
        )

        // 4. Beslenme & Hidrasyon (Beden Disiplini)
        ComparisonRowCard(
            title = "Beslenme & Fit Mutfak",
            icon = "🥗",
            oldMetric = "%${stats.nutritionRateThen} Su / Rutin",
            oldSub = "Şekerli Krizler & İhmal",
            newMetric = "%${stats.nutritionRateNow} Beden Düzeni",
            newSub = "Protein & Düzenli Hidrasyon",
            growth = if (stats.nutritionRateNow > stats.nutritionRateThen) "+%${stats.nutritionRateNow - stats.nutritionRateThen} Beden Disiplini" else "Fit mutfağı incele",
            isPositive = stats.nutritionRateNow > stats.nutritionRateThen,
            progress = (stats.nutritionRateNow.toFloat() / 100f).coerceIn(0.1f, 1f)
        )

        // OVERALL STATS SUMMARY
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Disiplin Skoru", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    Text(text = "${stats.disciplineScore} / 100", style = MaterialTheme.typography.titleLarge.copy(color = DisciplineGreen, fontWeight = FontWeight.Black))
                    Text(text = "+%${stats.monthlyGrowthRate} Kararlılık Artışı", style = MaterialTheme.typography.labelSmall.copy(color = DisciplineGreen, fontSize = 10.sp))
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Görev Oranı", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    Text(text = "%${stats.weeklyCompletionRate}", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Black))
                    Text(text = "${stats.completedTasksCount} / ${stats.totalTasksCount} Tamamlandı", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                }
            }
        }

        // HABIT HIGHLIGHTS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "💪", fontSize = 18.sp)
                    Text(
                        text = "En Güçlü Alışkanlık: ${stats.strongestHabit}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DisciplineGreen)
                    )
                }
                Divider(color = DarkBorder, thickness = 0.5.dp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "⚠️", fontSize = 18.sp)
                    Text(
                        text = "Kritik Risk Alanı: ${stats.weakestHabit}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = WarningOrange)
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonRowCard(
    title: String,
    icon: String,
    oldMetric: String,
    oldSub: String,
    newMetric: String,
    newSub: String,
    growth: String,
    isPositive: Boolean,
    progress: Float = 0.5f
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isPositive) TacticalGreen.copy(alpha = 0.35f) else DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row with Title and Growth Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = icon, fontSize = 18.sp)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
                Surface(
                    color = if (isPositive) DisciplineGreen.copy(alpha = 0.15f) else DarkSurfaceVariant,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        0.8.dp,
                        if (isPositive) DisciplineGreen.copy(alpha = 0.4f) else DarkBorder
                    )
                ) {
                    Text(
                        text = growth,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isPositive) DisciplineGreen else TextSecondary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Dual Side-by-Side Comparison Boxes (Old You vs New You)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ESKİ SEN (SLATE / ASH CONTRAST)
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = ComparisonOldYouSurface,
                    border = BorderStroke(1.dp, ComparisonOldYouBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "✕ ESKİ SEN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ComparisonOldYouText,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            )
                        }
                        Text(
                            text = oldMetric,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ComparisonOldYouText
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = oldSub,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ComparisonOldYouText.copy(alpha = 0.7f),
                                fontSize = 9.sp
                            ),
                            maxLines = 1
                        )
                    }
                }

                // DIRECTIONAL ARROW
                Surface(
                    shape = CircleShape,
                    color = DarkSurfaceVariant,
                    border = BorderStroke(0.5.dp, DarkBorder)
                ) {
                    Text(
                        text = "➔",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isPositive) DisciplineGreen else TextSecondary,
                            fontWeight = FontWeight.Black
                        )
                    )
                }

                // YENİ SEN (TRIUMPHANT DISCIPLINE)
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = DisciplineGreen.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, DisciplineGreen.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "✓ YENİ İRADE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DisciplineGreen,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            )
                        }
                        Text(
                            text = newMetric,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = newSub,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DisciplineGreen,
                                fontSize = 9.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }

            // Transformation Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DarkSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(
                            Brush.horizontalGradient(
                                listOf(TacticalGreenDark, DisciplineGreen)
                            )
                        )
                )
            }
        }
    }
}

// ----------------------------------------------------
// 5. 🏋️ KİŞİSEL SPOR SİSTEMİ
// ----------------------------------------------------
@Composable
private fun PersonalSportSystemView(
    activeGoal: String,
    exercises: List<SportExerciseTracking>,
    onSelectGoal: (String) -> Unit,
    onLogSet: (String) -> Unit = {},
    onResetSets: () -> Unit = {},
    onUpdateWeight: (String, Float) -> Unit = { _, _ -> }
) {
    val scrollState = rememberScrollState()

    // Interactive Rest Timer State
    var restSecondsLeft by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning, restSecondsLeft) {
        if (isTimerRunning && restSecondsLeft > 0) {
            kotlinx.coroutines.delay(1000L)
            restSecondsLeft--
            if (restSecondsLeft == 0) {
                isTimerRunning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // INTERACTIVE SET REST STOPWATCH
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, if (restSecondsLeft > 0) TacticalGreenBright else DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "⏱️", fontSize = 16.sp)
                        Text(
                            text = "SET ARASI DİNLENME SAYACI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TacticalGreenBright,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Text(
                        text = String.format("%02d:%02d", restSecondsLeft / 60, restSecondsLeft % 60),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = if (restSecondsLeft > 0) TacticalGreenBright else TextSecondary
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(30 to "30 sn", 60 to "60 sn", 90 to "90 sn", 120 to "120 sn").forEach { (sec, lbl) ->
                        OutlinedButton(
                            onClick = {
                                restSecondsLeft = sec
                                isTimerRunning = true
                            },
                            modifier = Modifier.weight(1f).height(36.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (restSecondsLeft == sec && isTimerRunning) TacticalGreenBright.copy(alpha = 0.2f) else DarkSurface,
                                contentColor = TextPrimary
                            ),
                            border = BorderStroke(1.dp, DarkBorder),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = lbl, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (restSecondsLeft > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { isTimerRunning = !isTimerRunning },
                            colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreenBright)
                        ) {
                            Text(if (isTimerRunning) "Duraklat" else "Devam Et", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        TextButton(
                            onClick = {
                                isTimerRunning = false
                                restSecondsLeft = 0
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                        ) {
                            Text("Sıfırla", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Text(
            text = "HEDEF PROGRAMI SEÇİN",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val goals = listOf(
                "hypertrophy" to "Kas & Güç",
                "fat_burn" to "Yağ Yakımı",
                "conditioning" to "Kondisyon",
                "general_fit" to "Genel Fit"
            )
            goals.forEach { (id, label) ->
                val isSelected = activeGoal == id
                OutlinedButton(
                    onClick = { onSelectGoal(id) },
                    modifier = Modifier.weight(1f).height(42.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) TacticalGreenBright.copy(alpha = 0.15f) else Color.Transparent,
                        contentColor = if (isSelected) TacticalGreenBright else TextSecondary
                    ),
                    border = BorderStroke(1.dp, if (isSelected) TacticalGreenBright else DarkBorder),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // EXERCISE CARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ÖZELLEŞTİRİLMİŞ EGZERSİZ REÇETESİ",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                )
            )
            TextButton(
                onClick = onResetSets,
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Setleri Sıfırla ↺", fontSize = 11.sp)
            }
        }

        exercises.forEach { ex ->
            val allSetsDone = ex.completedSets >= ex.targetSets

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (allSetsDone) DarkCardBackground.copy(alpha = 0.8f) else DarkSurface
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    1.dp,
                    if (allSetsDone) DisciplineGreen.copy(alpha = 0.6f) else DarkBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ex.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Surface(
                            color = TacticalGreenBright.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = ex.rpeSuggestion,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SportBox(modifier = Modifier.weight(1f), label = "Mevcut", value = if (ex.currentWeightKg > 0) "${ex.currentWeightKg} kg" else "Vücut Ağırlığı")
                        SportBox(modifier = Modifier.weight(1f), label = "Set x Tekrar", value = "${ex.targetSets} x ${ex.targetReps}")
                        SportBox(modifier = Modifier.weight(1f), label = "Sonraki Hedef", value = if (ex.recommendedNextWeightKg > 0) "+${ex.recommendedNextWeightKg} kg" else "+2 Tekrar", highlight = true)
                    }

                    // INTERACTIVE SET LOGGING & OVERLOAD CONTROLS
                    HorizontalDivider(color = DarkBorder, thickness = 0.5.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Set Counter
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Setler: ${ex.completedSets}/${ex.targetSets}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (allSetsDone) DisciplineGreen else TextPrimary
                                )
                            )
                            if (allSetsDone) {
                                Text("✓ Bitti", fontSize = 11.sp, color = DisciplineGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Weight Overload Adjusters (if weighted)
                        if (ex.currentWeightKg > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = { onUpdateWeight(ex.name, -2.5f) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(DarkSurfaceVariant, CircleShape)
                                ) {
                                    Text("-", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                                }
                                Text("${ex.currentWeightKg}kg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                IconButton(
                                    onClick = { onUpdateWeight(ex.name, 2.5f) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(DarkSurfaceVariant, CircleShape)
                                ) {
                                    Text("+", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                                }
                            }
                        }

                        // Log Set Button
                        Button(
                            onClick = {
                                onLogSet(ex.name)
                                restSecondsLeft = 60
                                isTimerRunning = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (allSetsDone) DarkBorder else TacticalGreenBright,
                                contentColor = if (allSetsDone) TextPrimary else Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (allSetsDone) "+ Ekstra Set" else "Set Tamamla ✓",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 6. 🧨 CHALLENGE SİSTEMİ
// ----------------------------------------------------
@Composable
private fun ChallengesSystemView(
    challenges: List<TransformChallenge>,
    onClaim: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var filterTab by remember { mutableStateOf("all") }

    val completedCount = challenges.count { it.isCompleted }
    val totalXpEarned = challenges.filter { it.isCompleted }.sumOf { it.xpReward }

    val filteredChallenges = when (filterTab) {
        "completed" -> challenges.filter { it.isCompleted }
        "ongoing" -> challenges.filter { !it.isCompleted }
        else -> challenges
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // CHALLENGE SUMMARY STATS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MEYDAN OKUMA İLERLEMESİ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "$completedCount / ${challenges.size} Rozet Açıldı",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                }

                Surface(
                    color = TacticalGreenBright.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "+$totalXpEarned XP",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
        }

        // FILTER TABS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                "all" to "Tümü (${challenges.size})",
                "ongoing" to "Devam Eden (${challenges.size - completedCount})",
                "completed" to "Kazanılan ($completedCount)"
            )
            tabs.forEach { (tabKey, label) ->
                val isSelected = filterTab == tabKey
                OutlinedButton(
                    onClick = { filterTab = tabKey },
                    modifier = Modifier.weight(1f).height(38.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) TacticalGreenBright.copy(alpha = 0.15f) else Color.Transparent,
                        contentColor = if (isSelected) TacticalGreenBright else TextSecondary
                    ),
                    border = BorderStroke(1.dp, if (isSelected) TacticalGreenBright else DarkBorder),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        filteredChallenges.forEach { ch ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp,
                    if (ch.isCompleted) TacticalGreenBright.copy(alpha = 0.6f) else DarkBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = ch.badgeIcon, fontSize = 28.sp)
                            Column {
                                Text(
                                    text = ch.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = ch.badgeName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Surface(
                            color = TacticalGreenBright.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "+${ch.xpReward} XP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Black
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = ch.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    // Progress bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "İlerleme", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                            Text(text = "${ch.currentDays} / ${ch.targetDays} Gün", style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                        }
                        LinearProgressIndicator(
                            progress = { ch.currentDays.toFloat() / ch.targetDays },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = TacticalGreenBright,
                            trackColor = DarkCardBackground
                        )
                    }

                    if (ch.isCompleted) {
                        Button(
                            onClick = { onClaim(ch.id) },
                            enabled = !ch.isClaimed,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (ch.isClaimed) DarkBorder else TacticalGreenBright,
                                contentColor = if (ch.isClaimed) TextSecondary else Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (ch.isClaimed) "ROZET KAZANILDI ✓" else "ROZETİ VE ÖDÜLÜ AL 🏆",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 7. 📝 HAFTALIK AI RAPORU (WHATSAPP HARİÇ, SADECE RAPOR DETAYI)
// ----------------------------------------------------
@Composable
private fun WeeklyReportView(
    reports: List<WeeklyAiReport>,
    userName: String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (reports.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📝", fontSize = 28.sp)
                    Text(
                        text = "1. Hafta Raporu Hazırlanıyor",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "İlk 7 günün görevlerini tamamladıkça AI Koç haftalık raporunu burada oluşturacak.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, textAlign = TextAlign.Center)
                    )
                }
            }
        } else {
            reports.forEach { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "HAFTA ${report.weekNumber} RAPORU",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = report.dateRange,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }

                            Surface(
                                color = TacticalGreenBright.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "+%${report.improvementVsLastWeek} Gelişim",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Black
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Divider(color = DarkBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ScoreBox(modifier = Modifier.weight(1f), label = "En Güçlü Alan", value = report.strongestDomain)
                            ScoreBox(modifier = Modifier.weight(1f), label = "Gelişim Alanı", value = report.weakestDomain, isWarning = true)
                        }

                        Surface(
                            color = DarkSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(0.5.dp, DarkBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "🎯 Gelecek Hafta Odak Noktan: ${report.nextWeekFocus}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TacticalGreenBright
                                    )
                                )
                                Text(
                                    text = report.aiPersonalizedAdvice,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        lineHeight = 17.sp
                                    )
                                )
                            }
                        }

                        // Copy Report Button
                        val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                        val context = androidx.compose.ui.platform.LocalContext.current
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    val summaryText = "📋 Transform Hafta ${report.weekNumber} Raporu\n" +
                                            "Gelişim: +%${report.improvementVsLastWeek}\n" +
                                            "Güçlü Alan: ${report.strongestDomain}\n" +
                                            "Gelişim Alanı: ${report.weakestDomain}\n" +
                                            "Odak: ${report.nextWeekFocus}\n" +
                                            "AI Tavsiyesi: ${report.aiPersonalizedAdvice}"
                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(summaryText))
                                    android.widget.Toast.makeText(context, "Haftalık rapor panoya kopyalandı 📋", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreenBright)
                            ) {
                                Text("Raporu Kopyala 📋", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// HELPER COMPOSABLES
// ----------------------------------------------------
@Composable
private fun ScoreBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    highlight: Boolean = false,
    isWarning: Boolean = false
) {
    Surface(
        modifier = modifier,
        color = DarkSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (highlight) TacticalGreenBright.copy(alpha = 0.5f) else DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 9.sp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = if (isWarning) Color(0xFFFFB4AB) else if (highlight) TacticalGreenBright else TextPrimary,
                    fontSize = 11.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SportBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Surface(
        modifier = modifier,
        color = DarkCardBackground,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (highlight) TacticalGreenBright else DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 9.sp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = if (highlight) TacticalGreenBright else TextPrimary
                )
            )
        }
    }
}

@Composable
private fun ScoreBar(
    title: String,
    value: Int,
    max: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary))
            Text(text = "$value / $max", style = MaterialTheme.typography.bodySmall.copy(color = TacticalGreenBright, fontWeight = FontWeight.Bold))
        }
        LinearProgressIndicator(
            progress = { value.toFloat() / max },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = TacticalGreenBright,
            trackColor = DarkCardBackground
        )
    }
}
