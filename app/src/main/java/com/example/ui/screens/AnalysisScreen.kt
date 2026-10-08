package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BehavioralInsight
import com.example.data.DisciplineEngine
import com.example.data.InsightType
import com.example.data.UserProfile
import com.example.data.WorkoutLogEntity
import com.example.data.WorkoutPlanEntity
import com.example.ui.components.TrainingCycleProgressChart
import com.example.ui.theme.*

@Composable
fun AnalysisScreen(
    userProfile: UserProfile?,
    plans: List<WorkoutPlanEntity> = emptyList(),
    workoutLogs: List<WorkoutLogEntity> = emptyList(),
    currentDayOfWeek: Int = 1,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val longestStreak = userProfile?.longestStreakDays ?: 16
    val totalWorkouts = userProfile?.totalWorkouts ?: 47

    val insights = remember(longestStreak, totalWorkouts) {
        DisciplineEngine.getBehavioralInsights(longestStreak, totalWorkouts)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("analysis_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "ANALİZ",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = (-0.5).sp
                )
            )
            Text(
                text = "Davranışlarını anla. Sistemi güçlendir.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // 📈 GENEL ANALİZ
        Card(
            modifier = Modifier.fillMaxWidth().testTag("general_analysis_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📈 GENEL ANALİZ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Bu Hafta",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricMiniBlock(
                        label = "Antrenman",
                        value = "4",
                        icon = "💪",
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniBlock(
                        label = "Devamlılık",
                        value = "%80",
                        icon = "🔥",
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniBlock(
                        label = "Süre",
                        value = "3s 45d",
                        icon = "⏱️",
                        modifier = Modifier.weight(1.2f)
                    )
                }

                // 📈 7 GÜNLÜK İDMAN DÖNGÜSÜ İLERLEME GRAFİĞİ (D3 / Recharts)
                TrainingCycleProgressChart(
                    plans = plans,
                    workoutLogs = workoutLogs,
                    currentDayOfWeek = currentDayOfWeek
                )
            }
        }

        // 🧠 DİSİPLİN ANALİZİ (Behavioral Deductions)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "🧠 DİSİPLİN ANALİZİ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = TacticalGreenBright,
                    letterSpacing = 0.5.sp
                )
            )

            insights.forEach { insight ->
                InsightCard(insight = insight)
            }
        }

        // Extra context quote
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "🛡️", fontSize = 22.sp)
                Text(
                    text = "Bu analizler seni yargılamak için değil; sisteminin zayıf noktalarını takviye etmek için tasarlandı.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
fun MetricMiniBlock(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 16.sp)
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
fun InsightCard(insight: BehavioralInsight) {
    val (borderColor, containerColor, iconColor) = when (insight.type) {
        InsightType.WARNING -> Triple(WarningOrange, WarningOrangeContainer.copy(alpha = 0.4f), WarningOrange)
        InsightType.DISCOVERY -> Triple(DarkBorderAccent, DarkSurfaceElevated, TacticalGreenBright)
        InsightType.STRENGTH -> Triple(TacticalGreen, TacticalGreenContainer.copy(alpha = 0.5f), TacticalGreen)
    }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("insight_card_${insight.type.name.lowercase()}"),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = insight.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = iconColor,
                        letterSpacing = 0.5.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = insight.badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Text(
                text = insight.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            )
        }
    }
}
