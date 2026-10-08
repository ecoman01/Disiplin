package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.data.WorkoutLogEntity
import com.example.data.WorkoutPlanEntity
import com.example.ui.components.TrainingCycleProgressChart
import com.example.ui.theme.*
import com.example.util.ShareHelper

private val WhatsAppGreenColor = Color(0xFF25D366)

data class WeekDayTab(
    val dayNumber: Int,
    val shortName: String,
    val fullName: String,
    val defaultIcon: String
)

@Composable
fun WorkoutsScreen(
    plans: List<WorkoutPlanEntity>,
    workoutLogs: List<WorkoutLogEntity> = emptyList(),
    currentDayOfWeek: Int = 1,
    userProfession: String = "Masa Başı & Ofis / Yazılımcı",
    userName: String = "Savaşçı",
    streakDays: Int = 0,
    userProfile: UserProfile? = null,
    onSelectPlan: (WorkoutPlanEntity) -> Unit,
    onCreateCustomPlan: (title: String, muscles: String, minutes: Int, exercises: List<Pair<String, Pair<Int, Int>>>) -> Unit,
    onResetToProfessionWorkouts: (String) -> Unit = {},
    onRetakeOnboarding: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedDay by remember(currentDayOfWeek) { mutableIntStateOf(currentDayOfWeek) }

    val daysOfWeek = listOf(
        WeekDayTab(1, "Pzt", "Pazartesi", "💪"),
        WeekDayTab(2, "Sal", "Salı", "🦾"),
        WeekDayTab(3, "Çar", "Çarşamba", "🦵"),
        WeekDayTab(4, "Per", "Perşembe", "🧘"),
        WeekDayTab(5, "Cum", "Cuma", "⚡"),
        WeekDayTab(6, "Cmt", "Cumartesi", "🔥"),
        WeekDayTab(7, "Paz", "Pazar", "🧠")
    )

    // Find the plan dedicated to the selected day
    val activeDayPlan = remember(plans, selectedDay) {
        plans.firstOrNull { it.dayOfWeek == selectedDay && !it.isCustom }
            ?: plans.firstOrNull { !it.isCustom }
    }

    val customPlans = remember(plans) { plans.filter { it.isCustom } }
    val defaultDailyPlans = remember(plans) { plans.filter { !it.isCustom }.sortedBy { it.dayOfWeek } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp)
            .testTag("workouts_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 32.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "GÜNLÜK İDMANLAR",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Haftanın 7 gününe ve mesleki dinamiklerine özel yapılandırılmış antrenman sistemi",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // 👔 MESLEK BAZLI PROGRAM BİLGİ KARTI
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, TacticalGreenDark)
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = TacticalGreenBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "MESLEKİ PROGRAM DİZAYNI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TacticalGreenContainer
                        ) {
                            Text(
                                text = "Aktif Plan",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = userProfile?.profession ?: userProfession,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    // 🏷️ Anket Yanıtı Bazlı Kişiselleştirme Rozetleri
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Text(
                                text = "📍 ${userProfile?.workoutLocation ?: "Spor Salonu"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Text(
                                text = "⚡ ${userProfile?.fitnessLevel ?: "Orta Düzey"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Text(
                                text = "🎯 ${userProfile?.targetGoal ?: "Kas Kütlesi & Hipertrofi"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Programındaki egzersizler, set hacimleri ve dinlenme aralıkları en başta seçtiğin ${userProfile?.workoutLocation ?: "lokasyona"} ve ${userProfile?.fitnessLevel ?: "seviyene"} göre baştan inşa edildi.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onRetakeOnboarding,
                            colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreenBright),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "⚡ Soruları & Programı Değiştir",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                            )
                        }

                        TextButton(
                            onClick = { onResetToProfessionWorkouts(userProfession) },
                            colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "↺ Varsayılana Sıfırla",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }
                }
            }
        }

        // 📈 7 GÜNLÜK İDMAN DÖNGÜSÜ İLERLEME GRAFİĞİ (D3 / Recharts Tarzı)
        item {
            TrainingCycleProgressChart(
                plans = plans,
                workoutLogs = workoutLogs,
                currentDayOfWeek = currentDayOfWeek
            )
        }

        // 🗓️ 7 GÜNLÜK HAFTALIK GÜN SEÇİCİ
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = TacticalGreenBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "HAFTALIK PROGRAM SEÇİMİ",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TacticalGreenBright,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        val selectedDayObj = daysOfWeek.find { it.dayNumber == selectedDay }
                        Text(
                            text = selectedDayObj?.fullName ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Gün selector pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        daysOfWeek.forEach { day ->
                            val isSelected = day.dayNumber == selectedDay
                            val isToday = day.dayNumber == currentDayOfWeek

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        when {
                                            isSelected -> TacticalGreenContainer
                                            isToday -> DarkSurfaceElevated
                                            else -> DarkSurfaceVariant
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else if (isToday) 1.dp else 0.5.dp,
                                        color = when {
                                            isSelected -> TacticalGreen
                                            isToday -> TacticalGreenDark
                                            else -> DarkBorder
                                        },
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedDay = day.dayNumber }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                if (isToday) {
                                    Text(
                                        text = "BUGÜN",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) TacticalGreenBright else TacticalGreen
                                        )
                                    )
                                }
                                Text(
                                    text = day.defaultIcon,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = day.shortName,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) TacticalGreenBright else TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // ⭐ SEÇİLEN GÜNÜN ÖNE ÇIKAN İDMAN KARTI
        activeDayPlan?.let { plan ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_day_workout_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.5.dp, TacticalGreenDark)
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
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TacticalGreenContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = TacticalGreenBright,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (plan.dayOfWeek == currentDayOfWeek) "BUGÜNÜN İDMANI" else "${plan.dayName.uppercase()} İDMANI",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TacticalGreenBright,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${plan.estimatedMinutes} Dk",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "🎯 Hedef: ${plan.targetMuscles}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Button(
                            onClick = { onSelectPlan(plan) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_day_workout_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TacticalGreen,
                                contentColor = TacticalGreenButtonText
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Text(
                                    text = "BU İDMANA BAŞLA",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 📋 TÜM GÜNLERİN ANTRENMANLARI BAŞLIĞI
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗓️ 7 GÜNLÜK TAM TAKVİM",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TacticalGreenBright,
                        letterSpacing = 1.sp
                    )
                )

                TextButton(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen),
                    modifier = Modifier.testTag("create_plan_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ÖZEL OLUŞTUR",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 7 GÜNLÜK PROGRAM LİSTESİ
        items(defaultDailyPlans) { plan ->
            val isCurrentDayPlan = plan.dayOfWeek == currentDayOfWeek
            val isDaySelected = plan.dayOfWeek == selectedDay

            WorkoutPlanCard(
                plan = plan,
                isToday = isCurrentDayPlan,
                isSelected = isDaySelected,
                onStart = { onSelectPlan(plan) }
            )
        }

        // ÖZEL PROGRAMLAR (Varsa)
        if (customPlans.isNotEmpty()) {
            item {
                Text(
                    text = "⭐ ÖZEL PROGRAMLARIN",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TacticalGreenBright,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            items(customPlans) { plan ->
                WorkoutPlanCard(
                    plan = plan,
                    isToday = false,
                    isSelected = false,
                    onStart = { onSelectPlan(plan) }
                )
            }
        }

        item {
            // Button to create custom program
            OutlinedButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("add_custom_program_btn"),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DarkSurfaceVariant.copy(alpha = 0.5f),
                    contentColor = TacticalGreenBright
                ),
                border = BorderStroke(1.dp, TacticalGreenDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = TacticalGreenBright)
                    Text(
                        text = "➕ KENDİ ÖZEL ANTRENMANINI OLUŞTUR",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateCustomPlanDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { title, muscles, minutes, exercises ->
                onCreateCustomPlan(title, muscles, minutes, exercises)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlanEntity,
    isToday: Boolean = false,
    isSelected: Boolean = false,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emoji = when (plan.iconType) {
        "push" -> "💪"
        "pull" -> "🦾"
        "legs" -> "🦵"
        "cardio" -> "🏃"
        "recovery" -> "🧘"
        "home" -> "🏠"
        else -> "🏋️"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onStart() }
            .testTag("plan_card_${plan.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DarkSurfaceElevated else DarkSurface
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) TacticalGreen else if (isToday) TacticalGreenDark else DarkBorder
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 22.sp)
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            if (isToday) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = TacticalGreenContainer
                                ) {
                                    Text(
                                        text = "BUGÜN",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TacticalGreenBright,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                        Text(
                            text = plan.targetMuscles,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceVariant
                ) {
                    Text(
                        text = "${plan.estimatedMinutes} dk",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Hızlı Başlat Çubuğu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected || isToday) TacticalGreen else DarkSurfaceVariant,
                        contentColor = if (isSelected || isToday) TacticalGreenButtonText else TacticalGreenBright
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BAŞLAT",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateCustomPlanDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, muscles: String, minutes: Int, exercises: List<Pair<String, Pair<Int, Int>>>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var muscles by remember { mutableStateOf("") }
    var minutesText by remember { mutableStateOf("45") }

    var ex1Name by remember { mutableStateOf("") }
    var ex1Sets by remember { mutableStateOf("3") }
    var ex1Reps by remember { mutableStateOf("10") }

    var ex2Name by remember { mutableStateOf("") }
    var ex2Sets by remember { mutableStateOf("3") }
    var ex2Reps by remember { mutableStateOf("10") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Yeni Özel Antrenman",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Program Adı (Örn: Kol Günü)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = TacticalGreen,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("custom_plan_title_input")
                )

                OutlinedTextField(
                    value = muscles,
                    onValueChange = { muscles = it },
                    label = { Text("Hedef Kaslar (Örn: Göğüs • Triceps)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = TacticalGreen,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Egzersizler (Set × Tekrar):",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TacticalGreenBright,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ex1Name,
                        onValueChange = { ex1Name = it },
                        label = { Text("1. Egzersiz") },
                        placeholder = { Text("Bench Press") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = ex1Sets,
                        onValueChange = { ex1Sets = it },
                        label = { Text("Set") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = ex1Reps,
                        onValueChange = { ex1Reps = it },
                        label = { Text("Tekrar") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ex2Name,
                        onValueChange = { ex2Name = it },
                        label = { Text("2. Egzersiz") },
                        placeholder = { Text("Incline Dumbbell") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = ex2Sets,
                        onValueChange = { ex2Sets = it },
                        label = { Text("Set") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = ex2Reps,
                        onValueChange = { ex2Reps = it },
                        label = { Text("Tekrar") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val exercises = mutableListOf<Pair<String, Pair<Int, Int>>>()
                        if (ex1Name.isNotBlank()) {
                            exercises.add(ex1Name to ((ex1Sets.toIntOrNull() ?: 3) to (ex1Reps.toIntOrNull() ?: 10)))
                        }
                        if (ex2Name.isNotBlank()) {
                            exercises.add(ex2Name to ((ex2Sets.toIntOrNull() ?: 3) to (ex2Reps.toIntOrNull() ?: 10)))
                        }
                        if (exercises.isEmpty()) {
                            exercises.add("Push Up" to (3 to 10))
                        }
                        onConfirm(title, muscles, minutesText.toIntOrNull() ?: 45, exercises)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TacticalGreen),
                modifier = Modifier.testTag("save_custom_plan_btn")
            ) {
                Text("KAYDET", color = DarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = TextSecondary)
            }
        },
        containerColor = DarkSurface
    )
}
