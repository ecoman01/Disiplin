package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.BillingManager
import com.example.data.DisciplineHabit
import com.example.data.DisciplineLevelEngine
import com.example.data.DisciplineLionEngine
import com.example.data.RealStruggleFactor
import com.example.data.StruggleEngine
import com.example.data.StruggleEvaluationResult
import com.example.data.TransformRepository
import com.example.data.UserProfile
import com.example.data.WorkoutLogEntity
import com.example.ui.components.LionProfileCard
import com.example.ui.theme.*
import com.example.util.ShareHelper

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    dailyHabits: List<DisciplineHabit> = emptyList(),
    workoutLogs: List<WorkoutLogEntity> = emptyList(),
    struggleEvaluation: StruggleEvaluationResult? = null,
    onUpdateName: (String) -> Unit = {},
    onUpdateGoal: (String) -> Unit,
    onUpdateIdentity: (String) -> Unit,
    onUpdateProfession: (String) -> Unit = {},
    onResetOnboarding: () -> Unit,
    onOpenNotificationSettings: () -> Unit = {},
    onOpenThemes: () -> Unit = {},
    onResetStats: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val evaluatedStruggle = remember(userProfile, dailyHabits, workoutLogs, struggleEvaluation) {
        struggleEvaluation ?: StruggleEngine.evaluate(
            profile = userProfile,
            dailyHabits = dailyHabits,
            workoutLogs = workoutLogs,
            completedTasksCount = userProfile?.totalWorkouts ?: 0,
            lifeAnalysis = null
        )
    }
    val todayScore = evaluatedStruggle.todayScore
    val oldScore = evaluatedStruggle.oldScore
    var expandedFactorId by remember { mutableStateOf<String?>(null) }
    val totalXp = userProfile?.xp ?: 0
    val levelInfo = remember(totalXp) { DisciplineLevelEngine.getLevelInfo(totalXp) }
    val lionInfo = remember(totalXp) { DisciplineLionEngine.getLionInfo(totalXp) }
    val currentTheme = AppThemeManager.currentTheme

    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember(userProfile?.name) { mutableStateOf(userProfile?.name ?: "Savaşçı") }
    var showEditGoalDialog by remember { mutableStateOf(false) }
    var showEditIdentityDialog by remember { mutableStateOf(false) }
    var showEditProfessionDialog by remember { mutableStateOf(false) }
    var showResetStatsConfirmDialog by remember { mutableStateOf(false) }

    val professionOptions = listOf(
        "💻 Masa Başı & Ofis / Yazılımcı",
        "🚶 Ayakta / Saha / Fiziksel İş",
        "🌙 Vardiyalı / Gece Çalışanı",
        "📚 Öğrenci / Yoğun Ders",
        "⚡ Serbest / Girişimci / Esnek"
    )

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
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
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

    if (showEditProfessionDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfessionDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "Meslek ve Çalışma Tarzını Seç",
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
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    professionOptions.forEach { opt ->
                        val isSel = (userProfile?.profession ?: "Masa Başı & Ofis / Yazılımcı") == opt
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUpdateProfession(opt)
                                    showEditProfessionDialog = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) TacticalGreenContainer else DarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSel) TacticalGreen else DarkBorder)
                        ) {
                            Text(
                                text = opt,
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) TacticalGreenBright else TextPrimary
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEditProfessionDialog = false }) {
                    Text("KAPAT", color = TextSecondary)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "SENİN PROFİLİN",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = (-0.5).sp
                )
            )
            Text(
                text = "Kimliğin, hedeflerin ve seviye ile evrimleşen ruh totem aslanın.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // 🦁 DİSİPLİN ASLANI (SEVİYE & XP ARTTIKÇA EVRİMLEŞEN ASLAN)
        LionProfileCard(
            totalXp = totalXp,
            userName = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        )

        // ⚔️ KENDİNLE MÜCADELE: "BUGÜNKÜ SEN vs ESKİ SEN"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("today_vs_old_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent)
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
                        text = "⚔️ KENDİNLE MÜCADELE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TacticalGreenBright.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = "CANLI SİSTEM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp
                            ),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Canlı Durum Özeti Banner
                Surface(
                    color = DarkBackground.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, DarkBorderAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = evaluatedStruggle.statusTitle,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = if (todayScore >= 50) TacticalGreenBright else WarningOrange,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = evaluatedStruggle.statusDescription,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }

                // Scores & Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DisciplineGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, DisciplineGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "🔥 BUGÜNKÜ SEN",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DisciplineGreen,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Text(
                            text = "%$todayScore",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = DisciplineGreen
                            )
                        )
                        Text(
                            text = "Aktif Disiplin",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = DarkSurfaceVariant,
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text(
                            text = "VS",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ComparisonOldYouSurface,
                            border = BorderStroke(1.dp, ComparisonOldYouBorder),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "⏳ ESKİ SEN",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ComparisonOldYouText,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Text(
                            text = "%$oldScore",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = ComparisonOldYouText
                            )
                        )
                        Text(
                            text = "Erteleme & Pasiflik",
                            style = MaterialTheme.typography.labelSmall.copy(color = ComparisonOldYouText.copy(alpha = 0.8f), fontSize = 10.sp)
                        )
                    }
                }

                // Modern Ratio Bar with clear contrast
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .border(BorderStroke(1.dp, DarkBorder), RoundedCornerShape(8.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(todayScore.toFloat().coerceAtLeast(1f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(TacticalGreen, DisciplineGreen)
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(oldScore.toFloat().coerceAtLeast(1f))
                                .fillMaxHeight()
                                .background(ComparisonOldYouBorder)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (todayScore >= 50) "▲ +%${todayScore - oldScore} İrade Üstünlüğü" else "▼ Mücadele Başlıyor",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (todayScore >= 50) DisciplineGreen else ComparisonOldYouText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = "Denge: $todayScore / $oldScore",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Anlık Zafer Hamlesi (Next Action to Shift Balance)
                Surface(
                    color = TacticalGreenBright.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🎯", fontSize = 20.sp)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "ANLIK ZAFER HAMLESİ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = evaluatedStruggle.nextActionToWin,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                // Canlı Reel Faktör Matrisi
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurfaceVariant)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Canlı Etken Analizi",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Aktivitelerine göre anlık hesaplanır",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Text(
                            text = "Detay için dokun",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary.copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        )
                    }

                    evaluatedStruggle.factors.forEach { factor ->
                        RealStruggleFactorRow(
                            factor = factor,
                            isExpanded = expandedFactorId == factor.id,
                            onToggleExpand = {
                                expandedFactorId = if (expandedFactorId == factor.id) null else factor.id
                            }
                        )
                    }
                }

                // Disiplin felsefesi notu
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkBackground)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "“Bu denge rastgele veya statik bir puan değildir; bugün attığın her somut adım (idman, rutin alışkanlıklar, kriz yönetimi) Yeni Sen'i güçlendirir, erteleme ve eylemsizlik ise Eski Sen'e alan açar.”",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Kimlik ve Hedefler Kartı
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🎯 HEDEFLER & KİMLİK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TacticalGreenBright,
                        letterSpacing = 0.5.sp
                    )
                )

                // İsim / Hitap & Aslan Avatarı
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
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(lionInfo.currentStage.auraColorHex), CircleShape)
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
                                text = "Sana Hitap Şekli • ${lionInfo.currentStage.shortTag}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TacticalGreenBright
                                )
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            tempName = userProfile?.name ?: "Savaşçı"
                            showEditNameDialog = true
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen)
                    ) {
                        Text("Değiştir →", fontWeight = FontWeight.Bold)
                    }
                }

                Divider(color = DarkBorder, thickness = 1.dp)

                // Ana Hedef
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ana Hedef",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = userProfile?.mainGoal ?: "Fit olmak",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    TextButton(
                        onClick = { showEditGoalDialog = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen)
                    ) {
                        Text("Değiştir →", fontWeight = FontWeight.Bold)
                    }
                }

                Divider(color = DarkBorder, thickness = 1.dp)

                // Kimlik
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Oluşturmak İstediğin Kimlik",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = userProfile?.identity ?: "Disiplinli biri",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TacticalGreenBright
                            )
                        )
                    }

                    TextButton(
                        onClick = { showEditIdentityDialog = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen)
                    ) {
                        Text("Değiştir →", fontWeight = FontWeight.Bold)
                    }
                }

                Divider(color = DarkBorder, thickness = 1.dp)

                // Meslek & Çalışma Düzeni
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Meslek & Çalışma Düzeni",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = userProfile?.profession ?: "Masa Başı & Ofis / Yazılımcı",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TacticalGreenBright
                            )
                        )
                    }

                    TextButton(
                        onClick = { showEditProfessionDialog = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen)
                    ) {
                        Text("Değiştir →", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 💳 GOOGLE PLAY LİSANSI & TRANSFORM KARTI
        val isTransformUnlocked by BillingManager.isTransformUnlocked.collectAsState()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_google_play_license_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isTransformUnlocked) TacticalGreenBright.copy(alpha = 0.5f) else DarkBorder
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💳 GOOGLE PLAY LİSANSI",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Surface(
                        color = if (isTransformUnlocked) TacticalGreenBright.copy(alpha = 0.15f) else DarkSurfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isTransformUnlocked) "🔥 TRANSFORM AKTİF" else "🔒 TRANSFORM KİLİTLİ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isTransformUnlocked) TacticalGreenBright else TextSecondary,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = if (isTransformUnlocked)
                        "Google Play siparişiniz doğrulandı. 90 Günlük TRANSFORM, AI Koç, Hayat Analizi ve Haftalık Raporlar sınırsız erişiminizdedir."
                    else
                        "TRANSFORM bölümü kilitlidir. Tek seferlik 199,99 TL ödeyerek ömür boyu erişim sağlayabilir veya önceki alımınızı geri yükleyebilirsiniz.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val restored = BillingManager.restorePurchases(context)
                            if (restored) {
                                android.widget.Toast.makeText(context, "Google Play lisansı doğrulandı ve geri yüklendi!", android.widget.Toast.LENGTH_LONG).show()
                            } else {
                                android.widget.Toast.makeText(context, "Aktif bir TRANSFORM lisansı bulunamadı.", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Lisansı Geri Yükle", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    if (isTransformUnlocked) {
                        OutlinedButton(
                            onClick = {
                                BillingManager.lockForTesting(context)
                                android.widget.Toast.makeText(context, "Test modu: Transform kilitlendi.", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).height(42.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE57373)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE57373).copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Test: Kilitle", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                BillingManager.unlockForTesting(context)
                                android.widget.Toast.makeText(context, "Test modu: Transform kilidi açıldı!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).height(42.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TacticalGreenBright),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Test: Kilidi Aç", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Genel İstatistikler
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "📊 KÜMÜLATİF VERİLER",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TacticalGreenBright,
                        letterSpacing = 0.5.sp
                    )
                )

                ProfileStatRow(
                    icon = "🔥",
                    label = "En Uzun Seri",
                    value = "${userProfile?.longestStreakDays ?: 0} Gün"
                )
                ProfileStatRow(
                    icon = "💪",
                    label = "Toplam Antrenman",
                    value = "${userProfile?.totalWorkouts ?: 0}"
                )
                ProfileStatRow(
                    icon = "⏱️",
                    label = "Toplam Spor Süresi",
                    value = "${(userProfile?.totalMinutes ?: 0) / 60} Saat"
                )
                ProfileStatRow(
                    icon = "⚡",
                    label = "Kriz Modu Kurtarmaları",
                    value = "${userProfile?.crisisRescuesCount ?: 0} Kez"
                )
            }
        }

        // 🎖️ SEVİYE & RÜTBE KARTI (0'DAN BAŞLAR)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_level_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent)
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, Color(lionInfo.currentStage.auraColorHex), RoundedCornerShape(12.dp))
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
                                text = "SEVİYE ${levelInfo.level} • ${levelInfo.title}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "${lionInfo.currentStage.stageName} (${lionInfo.currentStage.badge})",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(lionInfo.currentStage.auraColorHex),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TacticalGreenContainer
                    ) {
                        Text(
                            text = "${levelInfo.totalXp} XP",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sonraki Seviye İlerlemesi",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "${levelInfo.currentLevelXp} / ${levelInfo.xpForNextLevel} XP (%${(levelInfo.progress * 100).toInt()})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextPrimary,
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

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurface
                ) {
                    Text(
                        text = "⭐ Her tamamlanan antrenman: +100 XP\n⚡ Her kriz kurtarma: +50 XP",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextBody,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // 🎨 TEMALAR VE RENK MODU SEÇİMİ
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_themes_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, TacticalGreen)
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(currentTheme.icon, fontSize = 24.sp)
                        Column {
                            Text(
                                text = "🎨 TEMALAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "Aktif: ${currentTheme.title}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Button(
                        onClick = onOpenThemes,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("profile_change_theme_button")
                    ) {
                        Text("DEĞİŞTİR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Text(
                    text = "Farklı temalar seçebilirsin: Siyah & Beyaz (Noir), Siber Neon, Taktik Yeşil ve Kızıl İrade. Seçilen tema tüm buton ve ekranlara anında uygulanır.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }

        // BİLDİRİM VE GÜNLÜK HATIRLATICILAR KARTI
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = TacticalGreenBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "BİLDİRİM & GÜNLÜK HATIRLATICILAR",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Text(
                    text = "Günün kritik anlarında iradeni diri tutacak stoik uyarılar, su içme alarmları ve antrenman saati bildirimlerini yapılandır.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )

                Button(
                    onClick = onOpenNotificationSettings,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "BİLDİRİM & ALARM AYARLARI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 🎯 KİŞİSELLEŞTİRİLMİŞ ZİHNİYET & SPOR PROGRAMI PROFİLİ
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TacticalGreenDark)
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
                        Text(text = "🎯", fontSize = 20.sp)
                        Text(
                            text = "ZİHNİYET & SPOR PROFİLİ",
                            style = MaterialTheme.typography.titleSmall.copy(
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
                            text = "Kişiselleştirilmiş",
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
                    text = "En başta yanıtladığın anket sorularına göre 7 günlük antrenman hareketlerin, set hacimleri ve motive edici disiplin sözleri dinamik olarak adapte edilmiştir.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 17.sp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("📍 İdman Alanı / Ekipman:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(userProfile?.workoutLocation ?: "Spor Salonu", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⚡ Seviye & Hacim:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(userProfile?.fitnessLevel ?: "Orta Düzey", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("💪 Fiziksel Hedef:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(userProfile?.targetGoal ?: "Kas Kütlesi & Hipertrofi", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🛡️ Disiplin Felsefesi:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(userProfile?.motivationStyle ?: "Stoik Felsefe", style = MaterialTheme.typography.bodySmall.copy(color = TacticalGreenBright, fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⚠️ Aşılacak Zayıf Nokta:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(userProfile?.biggestProblem ?: "Devam etmek", style = MaterialTheme.typography.bodySmall.copy(color = WarningOrange, fontWeight = FontWeight.Bold))
                    }
                }

                Button(
                    onClick = onResetOnboarding,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "⚡ TÜM SORULARI & PROGRAMI YENİDEN YAPILANDIR",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Onboarding / Başlangıç Sorularını Güncelle
        OutlinedButton(
            onClick = onResetOnboarding,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Başlangıç Anketi Cevaplarını Yenile", fontWeight = FontWeight.SemiBold)
        }

        // Tüm İlerlemeyi 0'a Sıfırla (Kullanıcı Talebi: Level ve İstatistikler 0'dan başlasın)
        OutlinedButton(
            onClick = { showResetStatsConfirmDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("reset_stats_to_zero_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = OldYouRed),
            border = androidx.compose.foundation.BorderStroke(1.dp, OldYouRed.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = OldYouRed,
                    modifier = Modifier.size(18.dp)
                )
                Text("İstatistikleri ve Seviyeyi 0'a Sıfırla", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showResetStatsConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetStatsConfirmDialog = false },
            title = {
                Text(
                    text = "0'dan Başla (Sıfırla)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = "Tüm seri, toplam antrenman, süre ve Seviye/XP 0 değerine sıfırlanacaktır. Devam etmek istiyor musun?",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetStats()
                        showResetStatsConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OldYouRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("EVET, 0'A SIFIRLA", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetStatsConfirmDialog = false }) {
                    Text("İptal", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    if (showEditGoalDialog) {
        var newGoal by remember { mutableStateOf(userProfile?.mainGoal ?: "Fit olmak") }
        AlertDialog(
            onDismissRequest = { showEditGoalDialog = false },
            title = { Text("Ana Hedefi Değiştir", color = TextPrimary) },
            text = {
                OutlinedTextField(
                    value = newGoal,
                    onValueChange = { newGoal = it },
                    label = { Text("Hedef") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = TacticalGreen,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateGoal(newGoal)
                        showEditGoalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalGreen)
                ) {
                    Text("KAYDET", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditGoalDialog = false }) {
                    Text("İptal", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }

    if (showEditIdentityDialog) {
        var newIdentity by remember { mutableStateOf(userProfile?.identity ?: "Disiplinli biri") }
        AlertDialog(
            onDismissRequest = { showEditIdentityDialog = false },
            title = { Text("Kimliği Değiştir", color = TextPrimary) },
            text = {
                OutlinedTextField(
                    value = newIdentity,
                    onValueChange = { newIdentity = it },
                    label = { Text("Kimlik") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = TacticalGreen,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateIdentity(newIdentity)
                        showEditIdentityDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalGreen)
                ) {
                    Text("KAYDET", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditIdentityDialog = false }) {
                    Text("İptal", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
fun RealStruggleFactorRow(
    factor: RealStruggleFactor,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Surface(
        color = DarkBackground.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (factor.isPositive) TacticalGreenBright.copy(alpha = 0.3f) else DarkBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = factor.iconEmoji, fontSize = 20.sp)
                    Column {
                        Text(
                            text = factor.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = factor.statusText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (factor.isPositive) TacticalGreenBright else WarningOrange,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (factor.isPositive) TacticalGreenBright.copy(alpha = 0.15f) else ComparisonOldYouSurface,
                    border = BorderStroke(1.dp, if (factor.isPositive) TacticalGreenBright.copy(alpha = 0.35f) else ComparisonOldYouBorder)
                ) {
                    Text(
                        text = if (factor.impactPoints >= 0) "+${factor.impactPoints}%" else "${factor.impactPoints}%",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = if (factor.isPositive) TacticalGreenBright else ComparisonOldYouText
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = factor.explanation,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            )

            if (isExpanded) {
                Surface(
                    color = DarkSurfaceElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, TacticalGreen.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "💡", fontSize = 14.sp)
                        Text(
                            text = factor.tacticalAdvice,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BehaviorImpactRow(behavior: String, impact: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = behavior, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary))
        Text(
            text = impact,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (impact.contains("🟢")) TacticalGreenBright else Color(0xFFF87171)
            )
        )
    }
}

@Composable
fun ProfileStatRow(icon: String, label: String, value: String) {
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
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
    }
}
