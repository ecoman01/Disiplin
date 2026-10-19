package com.example.ui.screens.coach

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.example.util.ShareHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoachChatScreen(
    messages: List<CoachChatMessage>,
    isCoachTyping: Boolean,
    currentPersona: CoachPersona,
    currentCategory: String,
    onSelectPersona: (CoachPersona) -> Unit,
    onSelectCategory: (String) -> Unit,
    onSendPromptOption: (CoachPromptOption) -> Unit,
    onSendCustomText: (String) -> Unit,
    onResetChat: () -> Unit,
    onNavigateToWorkout: () -> Unit = {},
    onNavigateToRecipes: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var customInputText by remember { mutableStateOf("") }

    // 5-Second Rule In-Chat Dialog
    var showFiveSecondDialog by remember { mutableStateOf(false) }
    var fiveSecondCount by remember { mutableIntStateOf(5) }
    var fiveSecondRunning by remember { mutableStateOf(false) }

    // 60-Second Breathing In-Chat Dialog
    var showBreathingDialog by remember { mutableStateOf(false) }
    var breathSecondsLeft by remember { mutableIntStateOf(60) }
    var breathPhase by remember { mutableStateOf("Nefes Al (4s)") }
    var breathingRunning by remember { mutableStateOf(false) }

    // Auto-scroll to bottom on new message or typing state change
    LaunchedEffect(messages.size, isCoachTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // 5-Second Countdown Coroutine
    LaunchedEffect(fiveSecondRunning) {
        if (fiveSecondRunning) {
            fiveSecondCount = 5
            while (fiveSecondCount > 0) {
                delay(1000)
                fiveSecondCount--
            }
            fiveSecondRunning = false
        }
    }

    // Breathing Coroutine
    LaunchedEffect(breathingRunning) {
        if (breathingRunning) {
            breathSecondsLeft = 60
            while (breathSecondsLeft > 0 && breathingRunning) {
                val cycle = (60 - breathSecondsLeft) % 16
                breathPhase = when {
                    cycle < 4 -> "Nefes Al (4s)"
                    cycle < 8 -> "Tut (4s)"
                    cycle < 12 -> "Ver (4s)"
                    else -> "Bekle (4s)"
                }
                delay(1000)
                breathSecondsLeft--
            }
            breathingRunning = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // ==========================================
        // 1. TOP HEADER & PERSONA SELECTOR
        // ==========================================
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                .clip(CircleShape)
                                .background(TacticalGreenContainer)
                                .border(1.5.dp, TacticalGreenBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(currentPersona.iconEmoji, fontSize = 20.sp)
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = currentPersona.title.uppercase(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(TacticalGreenBright)
                                )
                            }
                            Text(
                                text = currentPersona.subtitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onResetChat,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sohbeti Sıfırla",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Persona Seçici Çipler (Demir Koç, Marcus, Mamba, Bilge Usta)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CoachPersona.values().forEach { persona ->
                        val isSelected = persona == currentPersona
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) TacticalGreenContainer else DarkSurfaceVariant,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) TacticalGreenBright else DarkBorder
                            ),
                            modifier = Modifier
                                .clickable { onSelectPersona(persona) }
                                .testTag("persona_chip_${persona.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(persona.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = persona.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TacticalGreenBright else TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 2. CHAT STREAM (MESSAGES LIST)
        // ==========================================
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubbleItem(
                        message = msg,
                        onTriggerAction = { action ->
                            when (action) {
                                CoachActionType.START_5_SEC_RULE -> {
                                    showFiveSecondDialog = true
                                    fiveSecondRunning = true
                                }
                                CoachActionType.NAVIGATE_WORKOUT -> {
                                    onNavigateToWorkout()
                                }
                                CoachActionType.BREATHING_EXERCISE -> {
                                    showBreathingDialog = true
                                    breathingRunning = true
                                }
                                CoachActionType.COPY_ADVICE -> {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Koç Tavsiyesi", msg.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Koç sözü panoya kopyalandı!", Toast.LENGTH_SHORT).show()
                                }
                                CoachActionType.SHARE_ADVICE -> {
                                    ShareHelper.shareText(
                                        context,
                                        "⚡ DİSİPLİN KOÇU TAVSİYESİ:\n\n${msg.text}\n\n— ${msg.persona.title}",
                                        "Koç Motivasyonu"
                                    )
                                }
                            }
                        },
                        onSelectFollowUpOption = { option ->
                            if (option.payloadKey == "SHOW_ROOT") {
                                // Root seçenekleri zaten aşağıda var
                            } else if (option.payloadKey == "START_WORKOUT_NOW") {
                                onNavigateToWorkout()
                            } else if (option.payloadKey == "OPEN_RECIPES") {
                                onNavigateToRecipes()
                            } else if (option.payloadKey == "TRIGGER_5SEC") {
                                showFiveSecondDialog = true
                                fiveSecondRunning = true
                            } else if (option.payloadKey == "START_BREATHING") {
                                showBreathingDialog = true
                                breathingRunning = true
                            } else {
                                onSendPromptOption(option)
                            }
                        }
                    )
                }

                if (isCoachTyping) {
                    item {
                        CoachTypingIndicator(currentPersona)
                    }
                }
            }
        }

        // ==========================================
        // 3. INTERACTIVE CHOICE PANEL (OPTIONS OVER TYPING)
        // ==========================================
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                // Konu Kategorileri (Bahaneleri Yık, Sert Gerçekler, Beslenme, İdman...)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CoachChatEngine.categories.forEach { cat ->
                        val isSelected = cat == currentCategory
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) TacticalGreen else DarkSurfaceElevated,
                            border = BorderStroke(1.dp, if (isSelected) TacticalGreenBright else DarkBorder),
                            modifier = Modifier.clickable { onSelectCategory(cat) }
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                    color = if (isSelected) TacticalGreenButtonText else TextSecondary,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Seçili Kategorideki Hazır Sorular / Seçenek Butonları (Yazma yerine doğrudan tıkla)
                val categoryOptions = remember(currentCategory) {
                    CoachChatEngine.getOptionsForCategory(currentCategory)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoryOptions.forEach { opt ->
                        OutlinedButton(
                            onClick = { onSendPromptOption(opt) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkSurfaceElevated,
                                contentColor = TextPrimary
                            ),
                            border = BorderStroke(1.dp, TacticalGreenDark),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("prompt_opt_${opt.payloadKey}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(opt.icon, fontSize = 15.sp)
                                Text(
                                    text = opt.label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // İsteğe bağlı sade metin kutusu (Opsiyonel alternatif)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = { customInputText = it },
                        placeholder = {
                            Text(
                                "Veya aklındaki durumu yaz...",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TacticalGreenBright,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceElevated,
                            unfocusedContainerColor = DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("coach_custom_input")
                    )

                    IconButton(
                        onClick = {
                            if (customInputText.isNotBlank()) {
                                onSendCustomText(customInputText)
                                customInputText = ""
                            }
                        },
                        enabled = customInputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (customInputText.isNotBlank()) TacticalGreen else DarkSurfaceVariant)
                            .testTag("coach_send_custom_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Gönder",
                            tint = if (customInputText.isNotBlank()) TacticalGreenButtonText else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // ==========================================
    // 4. IN-CHAT 5-SECOND PROTOCOL OVERLAY DIALOG
    // ==========================================
    if (showFiveSecondDialog) {
        AlertDialog(
            onDismissRequest = {
                showFiveSecondDialog = false
                fiveSecondRunning = false
            },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "⏱️ 5 SANİYE PROTOKOLÜ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = WarningOrange
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (fiveSecondCount > 0) "$fiveSecondCount" else "🔥 KALK VE YAP!",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = if (fiveSecondCount > 0) TacticalGreenBright else OldYouRed
                        )
                    )
                    Text(
                        text = if (fiveSecondCount > 0)
                            "Beynin sana bahane üretmeden önce hemen ayağa kalk!"
                        else
                            "Süre bitti! Hiçbir şey düşünmeden hemen harekete geç!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFiveSecondDialog = false
                        fiveSecondRunning = false
                        onNavigateToWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    )
                ) {
                    Text("İDMANI BAŞLAT", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFiveSecondDialog = false
                        fiveSecondRunning = false
                    }
                ) {
                    Text("Kapat", color = TextSecondary)
                }
            }
        )
    }

    // ==========================================
    // 5. IN-CHAT 60S BREATHING DIALOG
    // ==========================================
    if (showBreathingDialog) {
        AlertDialog(
            onDismissRequest = {
                showBreathingDialog = false
                breathingRunning = false
            },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "🧘 60 SANİYE STOİK ODAK NEFESİ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TacticalGreenBright
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = breathPhase,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright
                        )
                    )
                    Text(
                        text = "Kalan: $breathSecondsLeft saniye",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    LinearProgressIndicator(
                        progress = { breathSecondsLeft / 60f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TacticalGreenBright,
                        trackColor = DarkSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBreathingDialog = false
                        breathingRunning = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    )
                ) {
                    Text("Tamamla", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun ChatBubbleItem(
    message: CoachChatMessage,
    onTriggerAction: (CoachActionType) -> Unit,
    onSelectFollowUpOption: (CoachPromptOption) -> Unit
) {
    val isCoach = message.sender == MessageSender.COACH

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCoach) Arrangement.Start else Arrangement.End
    ) {
        if (isCoach) {
            // Coach Avatar
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
                    .border(1.dp, TacticalGreenBright, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(message.persona.iconEmoji, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (isCoach) Alignment.Start else Alignment.End
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isCoach) 4.dp else 16.dp,
                    bottomEnd = if (isCoach) 16.dp else 4.dp
                ),
                color = if (isCoach) DarkSurfaceElevated else TacticalGreenContainer,
                border = BorderStroke(
                    1.dp,
                    if (isCoach) DarkBorder else TacticalGreen
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isCoach) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = message.persona.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TacticalGreenBright,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isCoach) TextPrimary else Color.White,
                            lineHeight = 20.sp,
                            fontSize = 13.5.sp
                        )
                    )

                    // Vurgulu Tokat Sözü (Key Punchline)
                    if (message.keyPunchline != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkBackground,
                            border = BorderStroke(1.dp, TacticalGreenDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "“${message.keyPunchline}”",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    lineHeight = 16.sp
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // Aksiyon Butonları (Paylaş, Kopyala, 5-Sec, Workout)
                    if (isCoach) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (message.actionType != null) {
                                when (message.actionType) {
                                    CoachActionType.START_5_SEC_RULE -> {
                                        TextButton(
                                            onClick = { onTriggerAction(CoachActionType.START_5_SEC_RULE) },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("⏱️ 5s Kuralı", color = WarningOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    CoachActionType.NAVIGATE_WORKOUT -> {
                                        TextButton(
                                            onClick = { onTriggerAction(CoachActionType.NAVIGATE_WORKOUT) },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("🏋️‍♂️ İdmana Git", color = TacticalGreenBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    CoachActionType.BREATHING_EXERCISE -> {
                                        TextButton(
                                            onClick = { onTriggerAction(CoachActionType.BREATHING_EXERCISE) },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("🧘 Nefes Al", color = TacticalGreenBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    else -> {}
                                }
                            }

                            IconButton(
                                onClick = { onTriggerAction(CoachActionType.COPY_ADVICE) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Kopyala", tint = TextSecondary, modifier = Modifier.size(13.dp))
                            }

                            IconButton(
                                onClick = { onTriggerAction(CoachActionType.SHARE_ADVICE) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Paylaş", tint = TextSecondary, modifier = Modifier.size(13.dp))
                            }
                        }
                    }
                }
            }

            // Takip Seçenekleri (Follow-up Option Chips directly beneath coach response)
            if (isCoach && message.followUpOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    message.followUpOptions.forEach { followUp ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurface,
                            border = BorderStroke(1.dp, TacticalGreenDark),
                            modifier = Modifier
                                .clickable { onSelectFollowUpOption(followUp) }
                                .testTag("follow_up_${followUp.payloadKey}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(followUp.icon, fontSize = 12.sp)
                                Text(
                                    text = followUp.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoachTypingIndicator(persona: CoachPersona) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(DarkSurfaceElevated)
                .border(1.dp, TacticalGreenBright, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(persona.iconEmoji, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkSurfaceElevated,
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    color = TacticalGreenBright,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "${persona.title} düşünüyor...",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }
    }
}
