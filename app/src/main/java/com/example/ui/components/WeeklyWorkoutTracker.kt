package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutPlanEntity
import com.example.ui.theme.*

data class DayWorkoutInfo(
    val dayNumber: Int,
    val shortName: String,
    val fullName: String,
    val emoji: String,
    val defaultTitle: String,
    val defaultMuscles: String,
    val defaultMinutes: Int
)

@Composable
fun WeeklyWorkoutTracker(
    plans: List<WorkoutPlanEntity>,
    completedDays: Set<Int>,
    currentDayOfWeek: Int,
    weeklyTarget: Int = 4,
    onToggleDay: (Int) -> Unit,
    onSelectPlan: (WorkoutPlanEntity) -> Unit = {},
    onMarkAllCompleted: () -> Unit = {},
    onResetWeek: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val weekDays = remember {
        listOf(
            DayWorkoutInfo(1, "Pzt", "Pazartesi", "💪", "Göğüs & Triceps", "Göğüs, Arka Kol", 45),
            DayWorkoutInfo(2, "Sal", "Salı", "🦾", "Sırt & Biceps", "Sırt, Ön Kol, Trapez", 45),
            DayWorkoutInfo(3, "Çar", "Çarşamba", "🦵", "Bacak & Kalf", "Ön Bacak, Arka Bacak, Kalça", 50),
            DayWorkoutInfo(4, "Per", "Perşembe", "🧘", "Omuz & Karın", "Ön/Yan/Arka Omuz, Core", 40),
            DayWorkoutInfo(5, "Cum", "Cuma", "⚡", "Tüm Vücut Güç", "Göğüs, Sırt, Bacak Bileşke", 50),
            DayWorkoutInfo(6, "Cmt", "Cumartesi", "🔥", "Kardiyo & Hipertrofi", "HIIT, Karın, Kondisyon", 35),
            DayWorkoutInfo(7, "Paz", "Pazar", "🧠", "Mobilite & Esneme", "Esneme, Bel/Boyun Sağlığı", 30)
        )
    }

    val completedCount = completedDays.size
    val totalDays = 7
    val progressFraction = (completedCount.toFloat() / totalDays).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "tracker_progress")
    val percentInt = (progressFraction * 100).toInt()

    val motivationNotice = remember(completedCount, weeklyTarget) {
        when {
            completedCount == 7 -> "🏆 KUSURSUZ HAFTA! 7 günün tamamı bitti, iradeni taşa kazıdın!"
            completedCount >= weeklyTarget -> "🔥 HAFTALIK HEDEF AŞILDI! Harika bir disiplin makinesisin."
            completedCount in 1 until weeklyTarget -> "⚡ İyi gidiyorsun! Kalan ${weeklyTarget - completedCount} günü de tamamla ve haftayı zaferle kapat."
            else -> "📅 Hafta yeni başladı. İlk kutucuğu işaretlemek için bugün harika bir gün!"
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weekly_workout_tracker"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, TacticalGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==========================================
            // 1. HEADER & PROGRESS OVERVIEW
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TacticalGreenContainer)
                            .border(1.5.dp, TacticalGreenBright, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📅", fontSize = 20.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "HAFTALIK ANTRENMAN TAKİPÇİSİ",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = "7 Günlük İdman Programını İşaretle",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onResetWeek,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Haftayı Sıfırla",
                            tint = TextSecondary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            // İlerleme Çubuğu ve Yüzde Bilgisi
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$completedCount / $totalDays Gün",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TacticalGreenBright
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TacticalGreenContainer
                        ) {
                            Text(
                                text = "%$percentInt",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TacticalGreenBright,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (completedCount >= weeklyTarget) TacticalGreenContainer else DarkSurfaceVariant
                    ) {
                        Text(
                            text = "Hedef: $weeklyTarget Gün",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (completedCount >= weeklyTarget) TacticalGreenBright else TextSecondary
                            )
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = TacticalGreenBright,
                    trackColor = DarkSurfaceVariant
                )

                Text(
                    text = motivationNotice,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (completedCount >= weeklyTarget) TacticalGreenBright else TextSecondary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Divider(color = DarkBorder, thickness = 1.dp)

            // ==========================================
            // 2. 7 GÜNLÜK INTERACTIVE CHECKBOX LİSTESİ
            // ==========================================
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                weekDays.forEach { dayInfo ->
                    val isCompleted = completedDays.contains(dayInfo.dayNumber)
                    val isToday = dayInfo.dayNumber == currentDayOfWeek
                    val plan = plans.firstOrNull { it.dayOfWeek == dayInfo.dayNumber && !it.isCustom }
                        ?: plans.firstOrNull { it.dayOfWeek == dayInfo.dayNumber }

                    val displayTitle = plan?.title ?: dayInfo.defaultTitle
                    val displayMuscles = plan?.targetMuscles ?: dayInfo.defaultMuscles
                    val displayMinutes = plan?.estimatedMinutes ?: dayInfo.defaultMinutes

                    WeeklyDayCheckboxRow(
                        dayInfo = dayInfo,
                        planTitle = displayTitle,
                        muscles = displayMuscles,
                        estimatedMinutes = displayMinutes,
                        isCompleted = isCompleted,
                        isToday = isToday,
                        onToggle = { onToggleDay(dayInfo.dayNumber) },
                        onStartWorkout = {
                            if (plan != null) {
                                onSelectPlan(plan)
                            }
                        }
                    )
                }
            }

            // ==========================================
            // 3. BOTTOM QUICK ACTIONS (TÜMÜNÜ İŞARETLE)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onMarkAllCompleted,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = TacticalGreenBright,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tümünü Tamamla",
                        color = TacticalGreenBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "İşaretleyince +35 XP",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun WeeklyDayCheckboxRow(
    dayInfo: DayWorkoutInfo,
    planTitle: String,
    muscles: String,
    estimatedMinutes: Int,
    isCompleted: Boolean,
    isToday: Boolean,
    onToggle: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCompleted -> DarkSurfaceVariant.copy(alpha = 0.85f)
            isToday -> DarkSurfaceElevatedEnd
            else -> DarkSurface
        },
        label = "row_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isCompleted -> TacticalGreen.copy(alpha = 0.7f)
            isToday -> WarningOrange.copy(alpha = 0.8f)
            else -> DarkBorder
        },
        label = "row_border"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.2.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("weekly_day_row_${dayInfo.dayNumber}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // SOL: Gün Numarası & Gün Rozeti
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) TacticalGreenContainer else DarkSurfaceVariant)
                        .border(
                            1.dp,
                            if (isCompleted) TacticalGreenBright else if (isToday) WarningOrange else DarkBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCompleted) "✓" else dayInfo.emoji,
                        fontSize = if (isCompleted) 16.sp else 14.sp,
                        color = if (isCompleted) TacticalGreenBright else Color.Unspecified,
                        fontWeight = if (isCompleted) FontWeight.Black else FontWeight.Normal
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = dayInfo.fullName,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) TextSecondary else TextPrimary,
                                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                            )
                        )

                        if (isToday) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = WarningOrange.copy(alpha = 0.2f),
                                border = BorderStroke(0.8.dp, WarningOrange)
                            ) {
                                Text(
                                    text = "BUGÜN",
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = WarningOrange,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }

                        if (isCompleted) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = TacticalGreenContainer
                            ) {
                                Text(
                                    text = "TAMAM",
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    Text(
                        text = "$planTitle • $estimatedMinutes dk",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isCompleted) TacticalGreenDark else TacticalGreenBright,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                }
            }

            // SAĞ: İdmanı Başlat İkonu & CHECKBOX
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!isCompleted) {
                    IconButton(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("start_workout_day_${dayInfo.dayNumber}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "İdmanı Başlat",
                            tint = TacticalGreenBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = TacticalGreen,
                        checkmarkColor = TacticalGreenButtonText,
                        uncheckedColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("checkbox_day_${dayInfo.dayNumber}")
                )
            }
        }
    }
}
