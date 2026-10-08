package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DisciplineLevelEngine
import com.example.data.DisciplineLionEngine
import com.example.data.JourneyMilestoneEntity
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.util.ShareHelper

private val WhatsAppGreen = Color(0xFF25D366)
private val WhatsAppDarkBg = Color(0xFF0D2818)

@Composable
fun JourneyScreen(
    milestones: List<JourneyMilestoneEntity>,
    userProfile: UserProfile? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val totalXp = userProfile?.xp ?: 0
    val levelInfo = remember(totalXp) { DisciplineLevelEngine.getLevelInfo(totalXp) }
    val lionInfo = remember(totalXp) { DisciplineLionEngine.getLionInfo(totalXp) }
    val unlockedCount = remember(milestones) { milestones.count { it.isUnlocked } }
    val totalCount = milestones.size.coerceAtLeast(1)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp)
            .testTag("journey_screen"),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 48.dp)
    ) {
        // ⚔️ BAŞLIK & VİZYON
        item {
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "DİSİPLİN YOLCULUĞU",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }
                Text(
                    text = "Karakterin ateşte dövüldüğü, zayıflığın ve bahanelerin ezildiği dönüm noktaları.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        // 🛡️ SAVAŞÇI SEVİYE & STATÜ KARTI (HERO SECTION)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, TacticalGreenDark)
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TacticalGreenContainer)
                                    .border(1.5.dp, Color(lionInfo.currentStage.auraColorHex), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = lionInfo.currentStage.drawableResId),
                                    contentDescription = lionInfo.currentStage.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column {
                                Text(
                                    text = "${levelInfo.level}. SEVİYE • ${lionInfo.currentStage.shortTag.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color(lionInfo.currentStage.auraColorHex),
                                        letterSpacing = 1.sp
                                    )
                                )
                                Text(
                                    text = levelInfo.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurface
                        ) {
                            Text(
                                text = "$unlockedCount / $totalCount AŞAMA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // XP PROGRESS
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Deneyim (XP)",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = "${levelInfo.currentLevelXp} / ${levelInfo.xpForNextLevel} XP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        LinearProgressIndicator(
                            progress = { levelInfo.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = TacticalGreen,
                            trackColor = DarkSurfaceVariant
                        )
                    }

                    // 📲 WHATSAPP İLE YOLCULUĞU PAYLAŞ BUTONU
                    Button(
                        onClick = {
                            val shareText = ShareHelper.buildWarriorJourneyShareText(
                                userName = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı",
                                levelTitle = levelInfo.title,
                                levelNumber = levelInfo.level,
                                totalXp = totalXp,
                                streakDays = userProfile?.streakDays ?: 0,
                                completedWorkouts = userProfile?.totalWorkouts ?: 0
                            )
                            ShareHelper.shareToWhatsApp(context, shareText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "WHATSAPP'TA YOLCULUĞUMU PAYLAŞ",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // 🌟 KİLOMETRE TAŞLARI / MİLESTONES LİSTESİ
        itemsIndexed(milestones) { index, milestone ->
            JourneyTimelineItem(
                milestone = milestone,
                isLast = index == milestones.size - 1,
                totalSteps = milestones.size,
                userName = userProfile?.name ?: "Savaşçı"
            )
        }
    }
}

@Composable
fun JourneyTimelineItem(
    milestone: JourneyMilestoneEntity,
    isLast: Boolean,
    totalSteps: Int = 8,
    userName: String = "Savaşçı"
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("milestone_${milestone.id}"),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Timeline node indicator with vertical connector line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(38.dp)
        ) {
            // Circle node
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (milestone.isUnlocked) TacticalGreenContainer else DarkSurfaceVariant
                    )
                    .border(
                        1.5.dp,
                        if (milestone.isUnlocked) TacticalGreen else DarkBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (milestone.isUnlocked) {
                    Text(text = milestone.iconEmoji, fontSize = 18.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Kilitli",
                        tint = TextTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Connecting line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.5.dp)
                        .height(104.dp)
                        .background(
                            if (milestone.isUnlocked) TacticalGreenDark.copy(alpha = 0.7f) else DarkBorder
                        )
                )
            }
        }

        // Milestone Content Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (milestone.isUnlocked) DarkSurface else DarkSurface.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                1.dp,
                if (milestone.isUnlocked) TacticalGreenDark.copy(alpha = 0.6f) else DarkBorder.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header: Step index & status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AŞAMA ${milestone.orderIndex} / $totalSteps",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = if (milestone.isUnlocked) TacticalGreenBright else TextTertiary,
                            letterSpacing = 0.5.sp
                        )
                    )

                    if (milestone.isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TacticalGreenContainer
                        ) {
                            Text(
                                text = milestone.dateText ?: "TAMAMLANDI ✓",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Text(
                                text = "KİLİTLİ HEDEF 🔒",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextTertiary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Milestone Title
                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (milestone.isUnlocked) TextPrimary else TextSecondary
                    )
                )

                // Milestone Description
                Text(
                    text = milestone.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (milestone.isUnlocked) TextSecondary else TextMuted,
                        lineHeight = 18.sp
                    )
                )

                // Individual WhatsApp Share for Unlocked Milestones
                if (milestone.isUnlocked) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                val text = """
                                    🏆 DİSİPLİN DÖNÜM NOKTASI KİLİDİ AÇILDI!
                                    
                                    👤 Savaşçı: $userName
                                    📜 Aşama: ${milestone.title}
                                    🎯 "${milestone.subtitle}"
                                    
                                    "Bahaneleri ezdik, yola tavizsiz devam ediyoruz!"
                                    
                                    📲 #Disiplin #SavaşçıYolculuğu #Zafer
                                """.trimIndent()
                                ShareHelper.shareToWhatsApp(context, text)
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, WhatsAppGreen.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WhatsAppGreen
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "WhatsApp'ta Zaferi Paylaş",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
