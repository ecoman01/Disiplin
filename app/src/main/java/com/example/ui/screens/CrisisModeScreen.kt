package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun CrisisModeScreen(
    onClose: () -> Unit,
    onCompleteCrisisAction: (actionType: String, actionTitle: String, minutes: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAction by remember { mutableStateOf<CrisisAction?>(null) }
    var isExecuting by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    val actions = remember {
        listOf(
            CrisisAction(
                id = "walk",
                emoji = "🚶",
                title = "10 Dakika Yürü",
                subtitle = "Temiz hava al, adımlarını hisset. Sadece yürü.",
                minutes = 10
            ),
            CrisisAction(
                id = "mini",
                emoji = "💪",
                title = "Mini Antrenman Yap",
                subtitle = "15 Şınav + 20 Squat. 4 dakika yeter.",
                minutes = 4
            ),
            CrisisAction(
                id = "stretch",
                emoji = "🧘",
                title = "5 Dakika Hareket Et",
                subtitle = "Esneme ve mobilite. Vücudunu uyandır.",
                minutes = 5
            )
        )
    }

    LaunchedEffect(isExecuting, secondsRemaining) {
        if (isExecuting && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
            if (secondsRemaining == 0) {
                isExecuting = false
                isCompleted = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("crisis_mode_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with close button
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
                            .background(WarningOrange)
                    )
                    Text(
                        text = "KRİZ YÖNETİM MODU",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = WarningOrange,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .size(40.dp)
                        .testTag("crisis_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = TextSecondary
                    )
                }
            }

            // Main Content
            if (isCompleted) {
                // Success state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = TacticalGreen,
                        modifier = Modifier.size(72.dp)
                    )

                    Text(
                        text = "Bugün minimumunu yaptın.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TacticalGreenDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🧠 SİSTEM YORUMU",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "“Bugün motivasyon kazanmadın. Kontrolü geri aldın.”",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }

                    Text(
                        text = "Serin korundu. Kimliğine bir oy daha verdin.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            selectedAction?.let {
                                onCompleteCrisisAction(it.id, it.title, it.minutes)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("crisis_finish_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "GÜNÜ GÜVENLE TAMAMLA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TacticalGreenButtonText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            } else if (isExecuting && selectedAction != null) {
                // Timer / Action in progress
                val action = selectedAction!!
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = action.emoji,
                        fontSize = 54.sp
                    )

                    Text(
                        text = action.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )

                    Text(
                        text = action.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    )

                    // Countdown
                    val mins = secondsRemaining / 60
                    val secs = secondsRemaining % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright
                        )
                    )

                    Button(
                        onClick = {
                            isExecuting = false
                            isCompleted = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("crisis_mark_done_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacticalGreen,
                            contentColor = TacticalGreenButtonText
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "✓ TAMAMLADIM",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TacticalGreenButtonText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            } else {
                // Choice selection screen (as described by user)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Tamam.",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Bugün mükemmel olmak zorunda değilsin.",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                        )
                        Text(
                            text = "Ama tamamen bırakmak zorunda da değilsin.",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TacticalGreenBright
                            )
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        actions.forEach { action ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedAction = action
                                        secondsRemaining = action.minutes * 60
                                        isExecuting = true
                                    }
                                    .testTag("crisis_action_${action.id}"),
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(DarkSurfaceVariant)
                                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = action.emoji, fontSize = 24.sp)
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = action.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = action.subtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary
                                            )
                                        )
                                    }

                                    Text(
                                        text = "${action.minutes} dk",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = TacticalGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom reminder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "“Spor yapamadım” yerine “Bugün minimumumu yaptım.”",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextTertiary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

data class CrisisAction(
    val id: String,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val minutes: Int
)
