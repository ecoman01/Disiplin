package com.example.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.NotificationHelper
import com.example.data.NotificationSettings
import com.example.data.UserProfile
import com.example.ui.theme.*

@Composable
fun DisciplineNotificationDialog(
    notificationSettings: NotificationSettings,
    userProfile: UserProfile?,
    todayPlanTitle: String? = null,
    todayDayName: String? = null,
    onUpdateSettings: (NotificationSettings) -> Unit,
    onSendWorkoutNotification: () -> Unit,
    onSendMotivationNotification: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val currentDeviceDayName = todayDayName ?: NotificationHelper.getDeviceDayName()

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                NotificationHelper.areNotificationsEnabled(context)
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (isGranted) {
                Toast.makeText(context, "Bildirim izni verildi!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Bildirim gönderebilmek için izin gereklidir.", Toast.LENGTH_LONG).show()
            }
        }
    )

    var workoutHour by remember(notificationSettings.workoutHour) { mutableIntStateOf(notificationSettings.workoutHour) }
    var workoutMinute by remember(notificationSettings.workoutMinute) { mutableIntStateOf(notificationSettings.workoutMinute) }
    var motivationHour by remember(notificationSettings.motivationHour) { mutableIntStateOf(notificationSettings.motivationHour) }
    var motivationMinute by remember(notificationSettings.motivationMinute) { mutableIntStateOf(notificationSettings.motivationMinute) }
    var workoutEnabled by remember(notificationSettings.workoutEnabled) { mutableStateOf(notificationSettings.workoutEnabled) }
    var motivationEnabled by remember(notificationSettings.motivationEnabled) { mutableStateOf(notificationSettings.motivationEnabled) }

    fun adjustMinutes(h: Int, m: Int, delta: Int): Pair<Int, Int> {
        var total = h * 60 + m + delta
        while (total < 0) total += 24 * 60
        total %= (24 * 60)
        return Pair(total / 60, total % 60)
    }

    fun openTimePicker(initialH: Int, initialM: Int, onSet: (Int, Int) -> Unit) {
        TimePickerDialog(
            context,
            { _, h, m -> onSet(h, m) },
            initialH,
            initialM,
            true
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .testTag("discipline_notification_dialog"),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, DarkBorderAccent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // DIALOG HEADER
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
                                .background(TacticalGreenContainer)
                                .border(1.dp, TacticalGreenDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = TacticalGreenBright,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Bildirim & Hatırlatmalar",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Günlük İrade ve Antrenman Alarmları",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_notification_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DarkBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // SCROLLABLE CONTENT
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // PERMISSION BANNER (IF NEEDED ON ANDROID 13+)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1616)),
                            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = Color(0xFFF87171)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Bildirim İzni Gerekli",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Telefona bildirim düşmesi için sistem izni verin.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Button(
                                    onClick = {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEF4444),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("İzin Ver", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // DEVICE DAY CALENDAR SYNC CARD
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, TacticalGreen.copy(alpha = 0.45f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TacticalGreenContainer)
                                    .border(1.dp, TacticalGreenDark, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = TacticalGreenBright,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Cihaz Takvimi: $currentDeviceDayName",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            color = TacticalGreenBright
                                        )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(TacticalGreen.copy(alpha = 0.25f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "SENKRONİZE",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TacticalGreenBright
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = "Bugünün İdmanı: ${todayPlanTitle ?: "$currentDeviceDayName Planı"}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    text = "Bildirimler ve alarmlar cihazınızın güncel takvim gününe ($currentDeviceDayName) göre eşleşir.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    // SECTION 1: INSTANT TEST NOTIFICATIONS (DIRECT TO PHONE TRAY)
                    Text(
                        text = "📲 TELEFONA ANLIK BİLDİRİM AT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 0.8.sp
                        )
                    )

                    // Workout notification button
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(TacticalGreenContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = TacticalGreenBright,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "$currentDeviceDayName Antrenmanı Bildirimi",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Bugünün ($currentDeviceDayName) antrenman planını anında telefon paneline gönderir.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        onSendWorkoutNotification()
                                        Toast.makeText(context, "$currentDeviceDayName antrenman bildirimi telefona gönderildi!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("send_workout_notification_btn"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TacticalGreen,
                                    contentColor = TacticalGreenButtonText
                                ),
                                shape = RoundedCornerShape(10.dp)
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
                                        "ŞİMDİ $currentDeviceDayName İDMANINI BİLDİR",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Motivation Quote notification button
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF3B2800)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Günün Motivasyon Sözü",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Seçili stoik zihniyet ve disiplin sözünü bildirim çubuğuna düşürür.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        onSendMotivationNotification()
                                        Toast.makeText(context, "Motivasyon sözü telefona bildirildi!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("send_motivation_notification_btn"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF59E0B),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "ŞİMDİ MOTİVASYON SÖZÜ GÖNDER",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // SECTION 2: SCHEDULED DAILY NOTIFICATIONS (LOCAL ALARM)
                    Text(
                        text = "⏰ GÜNLÜK OTOMATİK HATIRLATICILAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright,
                            letterSpacing = 0.8.sp
                        )
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Motivation Schedule
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Sabah Zihniyet & Motivasyon Dozu",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Her gün %02d:%02d vaktinde ilham verici söz".format(motivationHour, motivationMinute),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Switch(
                                    checked = motivationEnabled,
                                    onCheckedChange = {
                                        motivationEnabled = it
                                        onUpdateSettings(
                                            notificationSettings.copy(
                                                motivationEnabled = it,
                                                motivationHour = motivationHour,
                                                motivationMinute = motivationMinute
                                            )
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DarkBackground,
                                        checkedTrackColor = TacticalGreen
                                    )
                                )
                            }

                            if (motivationEnabled) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    // Big digital time banner with direct Picker button
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DarkSurface)
                                            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                                            .clickable {
                                                openTimePicker(motivationHour, motivationMinute) { h, m ->
                                                    motivationHour = h
                                                    motivationMinute = m
                                                    onUpdateSettings(
                                                        notificationSettings.copy(
                                                            motivationEnabled = true,
                                                            motivationHour = h,
                                                            motivationMinute = m
                                                        )
                                                    )
                                                    Toast.makeText(context, "Motivasyon saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF3B2800)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AccessTime,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFBBF24),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = "%02d:%02d".format(motivationHour, motivationMinute),
                                                    style = MaterialTheme.typography.titleLarge.copy(
                                                        fontWeight = FontWeight.Black,
                                                        color = Color(0xFFFBBF24),
                                                        letterSpacing = 1.sp
                                                    )
                                                )
                                                Text(
                                                    text = "Ayarlı Sabah Saati",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = TextSecondary,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                openTimePicker(motivationHour, motivationMinute) { h, m ->
                                                    motivationHour = h
                                                    motivationMinute = m
                                                    onUpdateSettings(
                                                        notificationSettings.copy(
                                                            motivationEnabled = true,
                                                            motivationHour = h,
                                                            motivationMinute = m
                                                        )
                                                    )
                                                    Toast.makeText(context, "Motivasyon saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color(0xFFFBBF24)
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Saati Seç",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    // Quick Adjusters & Presets
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Hızlı:",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(DarkSurface)
                                                .border(1.dp, DarkBorder, RoundedCornerShape(6.dp))
                                                .clickable {
                                                    val (h, m) = adjustMinutes(motivationHour, motivationMinute, -15)
                                                    motivationHour = h
                                                    motivationMinute = m
                                                    onUpdateSettings(notificationSettings.copy(motivationEnabled = true, motivationHour = h, motivationMinute = m))
                                                }
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Text("-15dk", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(DarkSurface)
                                                .border(1.dp, DarkBorder, RoundedCornerShape(6.dp))
                                                .clickable {
                                                    val (h, m) = adjustMinutes(motivationHour, motivationMinute, 15)
                                                    motivationHour = h
                                                    motivationMinute = m
                                                    onUpdateSettings(notificationSettings.copy(motivationEnabled = true, motivationHour = h, motivationMinute = m))
                                                }
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Text("+15dk", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                                        }

                                        listOf(Pair(7, 0), Pair(8, 0), Pair(8, 30), Pair(9, 0)).forEach { (h, m) ->
                                            val isSelected = motivationHour == h && motivationMinute == m
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isSelected) Color(0xFFF59E0B) else DarkSurface)
                                                    .border(1.dp, if (isSelected) Color(0xFFFBBF24) else DarkBorder, RoundedCornerShape(6.dp))
                                                    .clickable {
                                                        motivationHour = h
                                                        motivationMinute = m
                                                        onUpdateSettings(notificationSettings.copy(motivationEnabled = true, motivationHour = h, motivationMinute = m))
                                                        Toast.makeText(context, "Motivasyon saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                    }
                                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                            ) {
                                                Text(
                                                    text = "%02d:%02d".format(h, m),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = if (isSelected) Color.Black else TextSecondary,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "🔔 Sıradaki bildirim: ${NotificationHelper.getNextTriggerDescription(motivationHour, motivationMinute)}'da",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFFFBBF24),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }

                            Divider(color = DarkBorder, thickness = 1.dp)

                            // Workout Schedule
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Akşam Antrenman & Zincir Uyarısı",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Her gün %02d:%02d vaktinde antrenman çağrısı".format(workoutHour, workoutMinute),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Switch(
                                    checked = workoutEnabled,
                                    onCheckedChange = {
                                        workoutEnabled = it
                                        onUpdateSettings(
                                            notificationSettings.copy(
                                                workoutEnabled = it,
                                                workoutHour = workoutHour,
                                                workoutMinute = workoutMinute
                                            )
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DarkBackground,
                                        checkedTrackColor = TacticalGreen
                                    )
                                )
                            }

                            if (workoutEnabled) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    // Big digital time banner with direct Picker button
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DarkSurface)
                                            .border(1.dp, TacticalGreen.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                            .clickable {
                                                openTimePicker(workoutHour, workoutMinute) { h, m ->
                                                    workoutHour = h
                                                    workoutMinute = m
                                                    onUpdateSettings(
                                                        notificationSettings.copy(
                                                            workoutEnabled = true,
                                                            workoutHour = h,
                                                            workoutMinute = m
                                                        )
                                                    )
                                                    Toast.makeText(context, "Antrenman saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(TacticalGreenContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AccessTime,
                                                    contentDescription = null,
                                                    tint = TacticalGreenBright,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = "%02d:%02d".format(workoutHour, workoutMinute),
                                                    style = MaterialTheme.typography.titleLarge.copy(
                                                        fontWeight = FontWeight.Black,
                                                        color = TacticalGreenBright,
                                                        letterSpacing = 1.sp
                                                    )
                                                )
                                                Text(
                                                    text = "Ayarlı Antrenman Saati",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = TextSecondary,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                openTimePicker(workoutHour, workoutMinute) { h, m ->
                                                    workoutHour = h
                                                    workoutMinute = m
                                                    onUpdateSettings(
                                                        notificationSettings.copy(
                                                            workoutEnabled = true,
                                                            workoutHour = h,
                                                            workoutMinute = m
                                                        )
                                                    )
                                                    Toast.makeText(context, "Antrenman saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            border = BorderStroke(1.dp, TacticalGreenBright),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = TacticalGreenBright
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Saati Seç",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    // Quick Adjusters & Presets
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Hızlı:",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(DarkSurface)
                                                .border(1.dp, DarkBorder, RoundedCornerShape(6.dp))
                                                .clickable {
                                                    val (h, m) = adjustMinutes(workoutHour, workoutMinute, -15)
                                                    workoutHour = h
                                                    workoutMinute = m
                                                    onUpdateSettings(notificationSettings.copy(workoutEnabled = true, workoutHour = h, workoutMinute = m))
                                                }
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Text("-15dk", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(DarkSurface)
                                                .border(1.dp, DarkBorder, RoundedCornerShape(6.dp))
                                                .clickable {
                                                    val (h, m) = adjustMinutes(workoutHour, workoutMinute, 15)
                                                    workoutHour = h
                                                    workoutMinute = m
                                                    onUpdateSettings(notificationSettings.copy(workoutEnabled = true, workoutHour = h, workoutMinute = m))
                                                }
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Text("+15dk", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                                        }

                                        listOf(Pair(17, 0), Pair(18, 0), Pair(18, 30), Pair(19, 30), Pair(20, 0)).forEach { (h, m) ->
                                            val isSelected = workoutHour == h && workoutMinute == m
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isSelected) TacticalGreen else DarkSurface)
                                                    .border(1.dp, if (isSelected) TacticalGreenBright else DarkBorder, RoundedCornerShape(6.dp))
                                                    .clickable {
                                                        workoutHour = h
                                                        workoutMinute = m
                                                        onUpdateSettings(notificationSettings.copy(workoutEnabled = true, workoutHour = h, workoutMinute = m))
                                                        Toast.makeText(context, "Antrenman saati %02d:%02d olarak ayarlandı".format(h, m), Toast.LENGTH_SHORT).show()
                                                    }
                                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                            ) {
                                                Text(
                                                    text = "%02d:%02d".format(h, m),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = if (isSelected) TacticalGreenButtonText else TextSecondary,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "🔔 Sıradaki bildirim: ${NotificationHelper.getNextTriggerDescription(workoutHour, workoutMinute)}'da",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TacticalGreenBright,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // BOTTOM CLOSE BUTTON
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("TAMAM / KAPAT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
