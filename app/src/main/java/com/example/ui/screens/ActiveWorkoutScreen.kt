package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
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
import com.example.ui.ActiveWorkoutSessionState
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActiveWorkoutScreen(
    session: ActiveWorkoutSessionState,
    onCompleteSet: (exerciseIndex: Int, setIndex: Int, weight: Float, reps: Int) -> Unit,
    onNextExercise: () -> Unit,
    onPrevExercise: () -> Unit,
    onFinishWorkout: () -> Unit,
    onCancelWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val currentExerciseIndex = session.currentExerciseIndex
    val activeExercise = session.exercises.getOrNull(currentExerciseIndex)

    var restTimerSeconds by remember { mutableStateOf(0) }
    var restTimerActive by remember { mutableStateOf(false) }

    LaunchedEffect(restTimerActive, restTimerSeconds) {
        if (restTimerActive && restTimerSeconds > 0) {
            delay(1000L)
            restTimerSeconds -= 1
            if (restTimerSeconds == 0) {
                restTimerActive = false
            }
        }
    }

    if (activeExercise == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = TacticalGreen)
        }
        return
    }

    var weightState by remember(currentExerciseIndex) {
        mutableStateOf(activeExercise.currentWeightKg)
    }
    var repsState by remember(currentExerciseIndex) {
        mutableStateOf(activeExercise.currentReps)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(scrollState)
            .testTag("active_workout_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCancelWorkout,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .size(40.dp)
                    .testTag("cancel_workout_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "İptal",
                    tint = TextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = session.plan.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "İlerleme: ${currentExerciseIndex + 1} / ${session.exercises.size} Egzersiz",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TacticalGreenBright,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            TextButton(
                onClick = onFinishWorkout,
                colors = ButtonDefaults.textButtonColors(contentColor = TacticalGreen),
                modifier = Modifier.testTag("finish_early_button")
            ) {
                Text(
                    text = "BİTİR",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)
                )
            }
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { (currentExerciseIndex + 1).toFloat() / session.exercises.size.coerceAtLeast(1) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = TacticalGreen,
            trackColor = DarkBorder
        )

        // Active Exercise Card
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = activeExercise.exercise.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${activeExercise.exercise.targetSets} × ${activeExercise.exercise.targetReps}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Progressive Overload indicator
                val lastWeight = activeExercise.exercise.lastWeightKg
                if (lastWeight > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Son Antrenmanın:",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = "${lastWeight.toInt()} KG × ${activeExercise.exercise.targetReps}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                // Interactive Weight & Reps adjustment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Weight control
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "AĞIRLIK",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = "${weightState.toInt()} KG",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = { weightState = (weightState - 2.5f).coerceAtLeast(0f) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = TextSecondary)
                                }
                                IconButton(
                                    onClick = { weightState += 2.5f },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Arttır", tint = TacticalGreen)
                                }
                            }
                        }
                    }

                    // Reps control
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "TEKRAR",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = "$repsState",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = { repsState = (repsState - 1).coerceAtLeast(1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = TextSecondary)
                                }
                                IconButton(
                                    onClick = { repsState += 1 },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Arttır", tint = TacticalGreen)
                                }
                            }
                        }
                    }
                }

                // Sets List (SET 1, SET 2, SET 3...)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SET LİSTESİ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TacticalGreenBright,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    activeExercise.completedSets.forEachIndexed { setIdx, isDone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDone) DarkSurfaceElevated else DarkSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (isDone) TacticalGreenDark else DarkBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SET ${setIdx + 1}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )

                            Text(
                                text = "${weightState.toInt()} KG × $repsState",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDone) TacticalGreenBright else TextSecondary
                                )
                            )

                            if (isDone) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = TacticalGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Tamam",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TacticalGreen)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        onCompleteSet(currentExerciseIndex, setIdx, weightState, repsState)
                                        restTimerSeconds = 60
                                        restTimerActive = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TacticalGreen,
                                        contentColor = TacticalGreenButtonText
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("complete_set_${setIdx + 1}")
                                ) {
                                    Text(
                                        text = "✓ TAMAMLA",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = TacticalGreenButtonText,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Rest Timer Banner
                AnimatedVisibility(visible = restTimerActive) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorderAccent, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = WarningOrange)
                            Text(
                                text = "Dinlenme Süresi: ${restTimerSeconds}s",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = WarningOrange,
                                    fontWeight = FontWeight.Black
                                )
                            )
                            TextButton(onClick = { restTimerActive = false }) {
                                Text("Atla", color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Navigation between exercises & Finish
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPrevExercise,
                enabled = currentExerciseIndex > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkSurfaceVariant,
                    disabledContainerColor = DarkSurface
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("prev_exercise_button")
            ) {
                Icon(Icons.Default.NavigateBefore, contentDescription = null, tint = TextPrimary)
                Text("Önceki", color = TextPrimary)
            }

            if (currentExerciseIndex < session.exercises.size - 1) {
                Button(
                    onClick = onNextExercise,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("next_exercise_button")
                ) {
                    Text("Sonraki Egzersiz", color = TacticalGreenButtonText, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.NavigateNext, contentDescription = null, tint = TacticalGreenButtonText)
                }
            } else {
                Button(
                    onClick = onFinishWorkout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreen,
                        contentColor = TacticalGreenButtonText
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("finish_workout_button")
                ) {
                    Text("ANTRENMANI TAMAMLA", color = TacticalGreenButtonText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
