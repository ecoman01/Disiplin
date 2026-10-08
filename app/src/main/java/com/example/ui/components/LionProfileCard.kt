package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.DisciplineLionEngine
import com.example.data.DisciplineLionInfo
import com.example.data.LionEvolutionStage
import com.example.ui.theme.*

@Composable
fun LionProfileCard(
    totalXp: Int,
    userName: String = "Savaşçı",
    modifier: Modifier = Modifier
) {
    val lionInfo: DisciplineLionInfo = remember(totalXp) {
        DisciplineLionEngine.getLionInfo(totalXp)
    }

    var showRoarDialog by remember { mutableStateOf(false) }
    var currentRoarQuote by remember { mutableStateOf("") }
    var showAllStagesDialog by remember { mutableStateOf(false) }

    // Breathing pulsation animation for the lion's aura
    val infiniteTransition = rememberInfiniteTransition(label = "lion_aura")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_scale"
    )

    val currentStage = lionInfo.currentStage
    val stageColor = Color(currentStage.auraColorHex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lion_profile_hero_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, stageColor.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Category Title & Evolution Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(stageColor)
                    )
                    Text(
                        text = "🦁 DİSİPLİN ASLANI RUH TOTEMİ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = stageColor,
                            letterSpacing = 0.8.sp
                        )
                    )
                }

                Surface(
                    color = stageColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, stageColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = currentStage.shortTag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = stageColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Central Portrait + Specs Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lion Avatar Frame with Glowing Border & Breathing Aura
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(auraScale),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer glow box
                    Box(
                        modifier = Modifier
                            .size(116.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        stageColor.copy(alpha = 0.4f),
                                        stageColor.copy(alpha = 0.05f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Image container
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                2.dp,
                                Brush.sweepGradient(
                                    listOf(stageColor, TacticalGreenBright, stageColor)
                                ),
                                RoundedCornerShape(20.dp)
                            )
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                    ) {
                        Image(
                            painter = painterResource(id = currentStage.drawableResId),
                            contentDescription = currentStage.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Stage index badge on image bottom-right
                        Surface(
                            color = Color(0xCC0D1410),
                            shape = RoundedCornerShape(topStart = 8.dp),
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Text(
                                text = "${currentStage.badge} Aşama ${currentStage.stageIndex + 1}/4",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = stageColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Lion Identity & Trait details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = currentStage.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    )

                    Text(
                        text = "Mizaç: ${currentStage.trait}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = stageColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )

                    Text(
                        text = "Seviye atladıkça aslanın güçlenir, zırh kuşanır ve krala dönüşür.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Quick mini stats chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, DarkBorder)
                        ) {
                            Text(
                                text = "XP: $totalXp",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, DarkBorder)
                        ) {
                            Text(
                                text = currentStage.stageName.split(":")[1].trim(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = stageColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Description Box
            Surface(
                color = DarkBackground,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Text(
                    text = currentStage.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 17.sp,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }

            // Lion Stat Bars (İrade Gücü, Kondisyon, Kükreme Etkisi)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ASLANIN GÜÇ GELİŞİMİ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )

                currentStage.stats.forEach { stat ->
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stat.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = stat.value,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = stageColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        LinearProgressIndicator(
                            progress = { stat.percent },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = stageColor,
                            trackColor = DarkBorder
                        )
                    }
                }
            }

            // Next Evolution Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lionInfo.isMaxStage) "👑 MAKSİMUM EVRİM ZİRVESİ" else "BİR SONRAKİ EVRİM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (lionInfo.isMaxStage) Color(0xFFFFD700) else TextSecondary,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Text(
                        text = if (lionInfo.isMaxStage) {
                            "Zirve Kral Tamamlandı"
                        } else {
                            "${lionInfo.xpForNextStage} XP Kaldı (${lionInfo.nextStage?.title ?: ""})"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = stageColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                LinearProgressIndicator(
                    progress = { lionInfo.progressToNextStage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = stageColor,
                    trackColor = DarkBorder
                )
            }

            // Interactive Action Buttons (Kükre & Tüm Evrim Aşamaları)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Roar Button
                OutlinedButton(
                    onClick = {
                        currentRoarQuote = currentStage.roarQuotes.random()
                        showRoarDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = stageColor
                    ),
                    border = BorderStroke(1.dp, stageColor.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Aslanı Kükret ⚡",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Show All Evolution Stages Button
                Button(
                    onClick = { showAllStagesDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Evrim Aşamaları",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

    // 🦁 ROAR MOTIVATIONAL DIALOG
    if (showRoarDialog) {
        Dialog(onDismissRequest = { showRoarDialog = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = DarkSurfaceElevated,
                border = BorderStroke(2.dp, stageColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Glowing Lion Avatar in Dialog
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, stageColor, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = currentStage.drawableResId),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = "🦁 ASLANIN SAVAŞ KÜKREMESİ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = stageColor,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Surface(
                        color = DarkBackground,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text(
                            text = currentRoarQuote,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Text(
                        text = "– $userName'ın ${currentStage.title}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = stageColor,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Button(
                        onClick = { showRoarDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        )
                    ) {
                        Text("ANLADIM, DEVAM ET!", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }

    // 📜 ALL EVOLUTION STAGES SHOWCASE DIALOG
    if (showAllStagesDialog) {
        Dialog(onDismissRequest = { showAllStagesDialog = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = DarkSurfaceElevated,
                border = BorderStroke(1.5.dp, TacticalGreenBright.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Dialog Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🦁 ASLAN EVRİM YOLCULUĞU",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright
                                )
                            )
                            Text(
                                text = "Seviye ve XP arttıkça aslanın dönüşümü",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        IconButton(
                            onClick = { showAllStagesDialog = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Kapat",
                                tint = TextSecondary
                            )
                        }
                    }

                    Divider(color = DarkBorder, thickness = 1.dp)

                    // Scrollable List of 4 Evolution Stages
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        lionInfo.allStages.forEach { stage ->
                            val isUnlocked = totalXp >= stage.minXp
                            val isCurrent = stage.stageIndex == currentStage.stageIndex
                            val itemColor = Color(stage.auraColorHex)

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isCurrent) itemColor.copy(alpha = 0.12f) else DarkSurface,
                                border = BorderStroke(
                                    if (isCurrent) 1.5.dp else 1.dp,
                                    if (isCurrent) itemColor else if (isUnlocked) DarkBorderAccent else DarkBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Stage Thumbnail
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(
                                                1.5.dp,
                                                if (isCurrent) itemColor else DarkBorder,
                                                RoundedCornerShape(14.dp)
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(id = stage.drawableResId),
                                            contentDescription = stage.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            alpha = if (isUnlocked) 1f else 0.35f
                                        )

                                        if (!isUnlocked) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color(0x99000000)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lock,
                                                    contentDescription = "Kilitli",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Stage Info
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = stage.stageName,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (isUnlocked) itemColor else TextSecondary,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 11.sp
                                                )
                                            )
                                            if (isCurrent) {
                                                Surface(
                                                    color = itemColor,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "ŞU ANKİ",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            color = Color.Black,
                                                            fontWeight = FontWeight.Black,
                                                            fontSize = 9.sp
                                                        ),
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = stage.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) TextPrimary else TextMuted
                                            )
                                        )

                                        Text(
                                            text = if (isUnlocked) {
                                                "Gereken: ${stage.minXp} XP (Açıldı ✓)"
                                            } else {
                                                "Kilitli • ${stage.minXp} XP Gerekiyor"
                                            },
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (isUnlocked) TacticalGreenBright else TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        )

                                        Text(
                                            text = stage.trait,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary,
                                                fontSize = 10.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }

                                    // Status Badge
                                    if (isUnlocked) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Açıldı",
                                            tint = itemColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { showAllStagesDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        )
                    ) {
                        Text("KAPAT", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
