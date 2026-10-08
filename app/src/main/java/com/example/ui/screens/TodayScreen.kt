package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.BillingManager
import com.example.data.DisciplineEngine
import com.example.data.DisciplineHabit
import com.example.data.DisciplineLevelEngine
import com.example.data.DisciplineLionEngine
import com.example.data.UserProfile
import com.example.data.WorkoutPlanEntity
import com.example.ui.theme.*

@Composable
fun TodayScreen(
    userProfile: UserProfile?,
    dailyMessage: String,
    todayPlan: WorkoutPlanEntity?,
    dailyHabits: List<DisciplineHabit> = emptyList(),
    onStartWorkout: (WorkoutPlanEntity) -> Unit,
    onOpenCrisis: () -> Unit,
    onOpenNotification: () -> Unit,
    onOpenThemes: () -> Unit = {},
    onToggleHabit: (String) -> Unit = {},
    onUpdateName: (String) -> Unit = {},
    onOpenMotivation: () -> Unit = {},
    onOpenTransform: () -> Unit = {},
    onOpenRecipes: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val name = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
    val greeting = remember(name) { DisciplineEngine.getTimeGreeting(name) }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember(name) { mutableStateOf(name) }

    val streak = userProfile?.streakDays ?: 0
    val weeklyTarget = userProfile?.weeklyTarget ?: 4
    val weeklyCompleted = userProfile?.weeklyCompleted ?: 0
    val todayScore = userProfile?.todayVsOldScore ?: 50
    val totalXp = userProfile?.xp ?: 0
    val levelInfo = remember(totalXp) { DisciplineLevelEngine.getLevelInfo(totalXp) }
    val lionInfo = remember(totalXp) { DisciplineLionEngine.getLionInfo(totalXp) }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "İsmini Güncelle",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Sana nasıl hitap etmemizi istersin?",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_name_input"),
                        placeholder = { Text("Adını yaz...", color = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clean = tempName.trim().ifBlank { "Savaşçı" }
                        onUpdateName(clean)
                        showEditNameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("KAYDET", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("İPTAL", color = TextSecondary)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("today_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // ÜST KISIM (Kullanıcı adı ve selamlama)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = greeting.first,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                            color = TextPrimary
                        )
                    )
                    IconButton(
                        onClick = {
                            tempName = name
                            showEditNameDialog = true
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("edit_name_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "İsmini Değiştir",
                            tint = TacticalGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = greeting.second,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onOpenThemes,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, CircleShape)
                        .size(44.dp)
                        .testTag("today_themes_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Temalar",
                        tint = TacticalGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onOpenNotification,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, CircleShape)
                        .size(44.dp)
                        .testTag("notification_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Bildirim",
                        tint = TacticalGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 🛡️ SEVİYE & RÜTBE KARTI (0'DAN BAŞLAR)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onOpenThemes() }
                .testTag("today_level_badge"),
            color = DarkSurfaceElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.5.dp, Color(lionInfo.currentStage.auraColorHex), RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource(id = lionInfo.currentStage.drawableResId),
                                contentDescription = lionInfo.currentStage.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "SEVİYE ${levelInfo.level} • ${levelInfo.title}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TacticalGreenBright,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                                Text(
                                    text = lionInfo.currentStage.shortTag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(lionInfo.currentStage.auraColorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = "${levelInfo.currentLevelXp} / ${levelInfo.xpForNextLevel} XP (Seviye ${levelInfo.level + 1} için)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TacticalGreenContainer
                    ) {
                        Text(
                            text = "${(levelInfo.progress * 100).toInt()}%",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // XP Progress bar
                LinearProgressIndicator(
                    progress = { levelInfo.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = TacticalGreen,
                    trackColor = DarkSurfaceVariant
                )
            }
        }

        // 🔒 / 🔥 TRANSFORM 90 GÜNLÜK SİSTEM ÖZEL KARTI
        val isTransformUnlocked by BillingManager.isTransformUnlocked.collectAsState()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenTransform() }
                .testTag("today_transform_banner_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isTransformUnlocked) TacticalGreenBright else TacticalGreenBright.copy(alpha = 0.4f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isTransformUnlocked) TacticalGreenBright.copy(alpha = 0.15f)
                            else Color(0xFF2C2417)
                        )
                        .border(
                            1.dp,
                            if (isTransformUnlocked) TacticalGreenBright else Color(0xFFFFB74D),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isTransformUnlocked) "🔥" else "🔒",
                        fontSize = 20.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (isTransformUnlocked) "TRANSFORM AKTİF" else "TRANSFORM",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isTransformUnlocked) TacticalGreenBright else TextPrimary
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isTransformUnlocked) TacticalGreenBright.copy(alpha = 0.2f) else Color(0xFFFFB74D).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isTransformUnlocked) "90 GÜNLÜK" else "199,99 TL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isTransformUnlocked) TacticalGreenBright else Color(0xFFFFB74D),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = if (isTransformUnlocked) "Kişisel Analiz • AI Koç • Adaptif Sistem devrede" else "90 Günlük Kişisel Dönüşüm • AI Koç • Raporlar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TacticalGreenBright
                )
            }
        }

        // 🔥 GÜNLÜK DURUM KARTI
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_status_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = WarningOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "$streak GÜNLÜK SERİ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimary
                            )
                        )
                    }

                    Text(
                        text = "$weeklyCompleted / $weeklyTarget tamamlandı",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Geometric Progress Bar
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(DarkBorder)
                    ) {
                        val progressFraction = (weeklyCompleted.toFloat() / weeklyTarget.toFloat()).coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(5.dp))
                                .background(TacticalGreen)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Haftalık hedef: $weeklyCompleted / $weeklyTarget",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Zincir Aktif",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Text(
                    text = "Son $streak gündür devam ediyorsun. Zinciri kırma.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }

        // 🎯 BUGÜNÜN GÖREVİ / MİSYONU (Geometric Balance Hero Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("mission_card"),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(32.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkSurfaceElevated, DarkSurfaceElevatedEnd)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "BUGÜNÜN MİSYONU",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )

                    val title = todayPlan?.title ?: "Göğüs + Triceps"
                    val minutes = todayPlan?.estimatedMinutes ?: 45

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Tahmini süre: $minutes dakika",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Button(
                        onClick = {
                            todayPlan?.let { onStartWorkout(it) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("start_workout_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = TacticalGreenButtonText
                            )
                            Text(
                                text = "ANTRENMANA BAŞLA",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // ⚡ HIZLI DURUM (3 Kolonlu Geometrik Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Seri
            QuickStatCard(
                icon = "🔥",
                value = "$streak",
                label = "SERİ",
                accentColor = WarningOrange,
                modifier = Modifier.weight(1f)
            )

            // Bu Hafta
            QuickStatCard(
                icon = "💪",
                value = "$weeklyCompleted",
                label = "HAFTALIK",
                accentColor = TacticalGreen,
                modifier = Modifier.weight(1f)
            )

            // Disiplin %
            QuickStatCard(
                icon = "🎯",
                value = "%$todayScore",
                label = "DİSİPLİN",
                accentColor = DisciplineBlue,
                modifier = Modifier.weight(1f)
            )
        }

        // 🧠 BUGÜNÜN MESAJI (Sisteme Bağlı Davranış Mesajı)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_message_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🧠",
                    fontSize = 24.sp
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "SİSTEM MESAJI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "“$dailyMessage”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBody,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // ⚡ GÜNLÜK ZİHİN & DİSİPLİN RİTÜELLERİ (Sadece Spor Değil, Tam Motivasyon)
        if (dailyHabits.isNotEmpty()) {
            val habitsDone = dailyHabits.count { it.isCompleted }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_habits_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "GÜNLÜK İRADE RİTÜELLERİ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Sadece fiziksel değil; zihinsel disiplin",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TacticalGreenContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$habitsDone / ${dailyHabits.size}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    dailyHabits.take(3).forEach { habit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (habit.isCompleted) DarkSurfaceElevated else DarkSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (habit.isCompleted) DarkBorderAccent else DarkBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onToggleHabit(habit.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (habit.isCompleted) TacticalGreen else Color.Transparent)
                                    .border(
                                        1.5.dp,
                                        if (habit.isCompleted) TacticalGreen else TextSecondary,
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (habit.isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = DarkBackground,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Text(
                                text = habit.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (habit.isCompleted) TacticalGreenBright else TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onOpenMotivation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalGreen),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = TacticalGreen, modifier = Modifier.size(16.dp))
                            Text(
                                text = "TÜM MOTİVASYON & ZİHNİYET MERKEZİ",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TacticalGreen
                                )
                            )
                        }
                    }
                }
            }
        }

        // 🥗 FIT MUTFAK & SAĞLIKLI TARİFLER KARTI
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenRecipes() }
                .testTag("today_fit_recipes_banner"),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TacticalGreen.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(TacticalGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🥗", fontSize = 24.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "FIT MUTFAK & TARİFLER",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = DisciplineGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "YENİ",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DisciplineGreen,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Şekersiz fit tatlılar, yüksek protein ve kolay tarifler",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp
                            ),
                            maxLines = 2
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = DarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Tariflere Git",
                        tint = TacticalGreenBright,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }
            }
        }

        // 🚨 KRİZ MODU BUTONU (Geometric Balance Crisis Card)
        Button(
            onClick = onOpenCrisis,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("crisis_mode_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = OldYouRedContainer,
                contentColor = OldYouRed
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, OldYouRedBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "😩", fontSize = 16.sp)
                Text(
                    text = "BUGÜN HİÇ İSTEMİYORUM",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = OldYouRed
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun QuickStatCard(
    icon: String,
    value: String,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = icon, fontSize = 20.sp)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = accentColor
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
        }
    }
}
