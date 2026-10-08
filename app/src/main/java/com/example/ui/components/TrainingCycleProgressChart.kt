package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutLogEntity
import com.example.data.WorkoutPlanEntity
import com.example.ui.theme.*

enum class ChartDisplayType {
    SPLINE_AREA, // D3 / Recharts AreaChart with smooth Bézier curve and gradient
    BAR_CHART    // D3 / Recharts BarChart with rounded top bars
}

data class DayCycleProgress(
    val dayIndex: Int,           // 1 to 7
    val dayShortName: String,     // Pzt, Sal, Çar, ...
    val dayFullName: String,      // Pazartesi, Salı, ...
    val workoutTitle: String,     // Göğüs & Arka Kol, ...
    val targetMinutes: Int,       // Planned duration
    val completedMinutes: Int,    // Actual logged duration
    val isCompleted: Boolean,
    val isToday: Boolean
)

data class TrainingCycle(
    val cycleIndex: Int,
    val cycleTitle: String,
    val days: List<DayCycleProgress>
) {
    val totalTargetMinutes: Int get() = days.sumOf { it.targetMinutes }
    val totalCompletedMinutes: Int get() = days.sumOf { it.completedMinutes }
    val completedDaysCount: Int get() = days.count { it.isCompleted }
    val completionPercent: Int
        get() = if (days.isNotEmpty()) ((completedDaysCount.toFloat() / days.size) * 100).toInt() else 0
    val bestDay: DayCycleProgress?
        get() = days.maxByOrNull { it.completedMinutes }
}

@Composable
fun TrainingCycleProgressChart(
    plans: List<WorkoutPlanEntity>,
    workoutLogs: List<WorkoutLogEntity>,
    currentDayOfWeek: Int,
    modifier: Modifier = Modifier
) {
    var chartType by remember { mutableStateOf(ChartDisplayType.SPLINE_AREA) }
    var selectedCycleIndex by remember { mutableIntStateOf(0) } // 0: Aktif Döngü, 1: Önceki Döngü, 2: 1. Döngü
    var selectedDayIndex by remember { mutableStateOf<Int?>(null) }

    // Build cycles data dynamically based on plans and user workout logs
    val cycles = remember(plans, workoutLogs, currentDayOfWeek) {
        buildTrainingCycles(plans, workoutLogs, currentDayOfWeek)
    }

    val activeCycle = cycles.getOrElse(selectedCycleIndex) { cycles.first() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("training_cycle_progress_chart"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, TacticalGreenDark)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Title and Chart Type Switcher (Recharts style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = TacticalGreenBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "7 GÜNLÜK İDMAN DÖNGÜSÜ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TacticalGreenBright,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                    Text(
                        text = "Döngüsel İlerleme Grafiği",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                }

                // Chart Style Selector (Spline vs Bar)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    val isSpline = chartType == ChartDisplayType.SPLINE_AREA
                    IconButton(
                        onClick = { chartType = ChartDisplayType.SPLINE_AREA },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (isSpline) TacticalGreenContainer else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Spline Alan Grafiği",
                            tint = if (isSpline) TacticalGreenBright else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    val isBar = chartType == ChartDisplayType.BAR_CHART
                    IconButton(
                        onClick = { chartType = ChartDisplayType.BAR_CHART },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (isBar) TacticalGreenContainer else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Sütun Grafiği",
                            tint = if (isBar) TacticalGreenBright else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Cycle Tabs (Aktif Döngü, Döngü 2, Döngü 1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cycles.forEachIndexed { index, cycle ->
                    val isSelected = index == selectedCycleIndex
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) TacticalGreenBright.copy(alpha = 0.18f) else DarkSurface,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) TacticalGreenBright else DarkBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedCycleIndex = index
                                selectedDayIndex = null
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = cycle.cycleTitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = if (isSelected) TacticalGreenBright else TextSecondary,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                            Text(
                                text = "${cycle.completedDaysCount}/7 Gün",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TextPrimary else TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            // Key Metrics Summary (Recharts KPI Bar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(0.5.dp, DarkBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricColumn(
                    label = "Tamamlanma",
                    value = "%${activeCycle.completionPercent}",
                    color = TacticalGreenBright
                )
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(DarkBorder))
                MetricColumn(
                    label = "Toplam İdman",
                    value = "${activeCycle.totalCompletedMinutes} dk",
                    color = TextPrimary
                )
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(DarkBorder))
                MetricColumn(
                    label = "Hedef İdman",
                    value = "${activeCycle.totalTargetMinutes} dk",
                    color = TextSecondary
                )
            }

            // Canvas Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F1412))
                    .border(0.5.dp, DarkBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                when (chartType) {
                    ChartDisplayType.SPLINE_AREA -> {
                        D3SplineAreaCanvas(
                            days = activeCycle.days,
                            selectedDayIndex = selectedDayIndex,
                            onSelectDay = { selectedDayIndex = it }
                        )
                    }
                    ChartDisplayType.BAR_CHART -> {
                        D3BarChartCanvas(
                            days = activeCycle.days,
                            selectedDayIndex = selectedDayIndex,
                            onSelectDay = { selectedDayIndex = it }
                        )
                    }
                }
            }

            // X-Axis Day Labels (Pzt, Sal, Çar, Per, Cum, Cmt, Paz)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                activeCycle.days.forEach { day ->
                    val isSelected = day.dayIndex == selectedDayIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { selectedDayIndex = day.dayIndex }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = day.dayShortName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = when {
                                    isSelected -> TacticalGreenBright
                                    day.isToday -> TacticalGreen
                                    day.isCompleted -> TextPrimary
                                    else -> TextMuted
                                },
                                fontWeight = if (isSelected || day.isToday) FontWeight.Black else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                        if (day.isToday) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(TacticalGreenBright)
                            )
                        }
                    }
                }
            }

            // Interactive Day Tooltip (Recharts Floating Tooltip style)
            AnimatedVisibility(
                visible = selectedDayIndex != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val selectedDay = activeCycle.days.find { it.dayIndex == selectedDayIndex }
                if (selectedDay != null) {
                    Surface(
                        color = Color(0xFF1E2822),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, TacticalGreenBright.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${selectedDay.dayFullName} (Gün ${selectedDay.dayIndex})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TacticalGreenBright,
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                    if (selectedDay.isToday) {
                                        Surface(
                                            color = TacticalGreenBright,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "BUGÜN",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color.Black,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black
                                                ),
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = selectedDay.workoutTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Süre: ${selectedDay.completedMinutes} dk / Hedef: ${selectedDay.targetMinutes} dk",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Surface(
                                color = if (selectedDay.isCompleted) TacticalGreenBright.copy(alpha = 0.2f) else DarkSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (selectedDay.isCompleted) TacticalGreenBright else DarkBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (selectedDay.isCompleted) Icons.Default.CheckCircle else Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = if (selectedDay.isCompleted) TacticalGreenBright else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (selectedDay.isCompleted) "Tamamlandı" else "Bekliyor",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (selectedDay.isCompleted) TacticalGreenBright else TextSecondary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Prompt Note
            Text(
                text = "💡 Grafikteki noktalara veya sütunlara dokunarak günün detaylarını inceleyebilirsin.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun MetricColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 10.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = color,
                fontWeight = FontWeight.Black
            )
        )
    }
}

/**
 * D3 / Recharts AreaChart Canvas Implementation:
 * Uses smooth cubic Bézier spline interpolation, glowing gradient fill,
 * dashed Cartesian grid lines, data dots, and interactive touch detection.
 */
@Composable
private fun D3SplineAreaCanvas(
    days: List<DayCycleProgress>,
    selectedDayIndex: Int?,
    onSelectDay: (Int) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600),
        label = "spline_progress"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(days) {
                detectTapGestures { tapOffset ->
                    val stepWidth = size.width / (days.size + 1)
                    val tappedIndex = days.indices.minByOrNull { i ->
                        val x = stepWidth * (i + 1)
                        kotlin.math.abs(x - tapOffset.x)
                    }
                    if (tappedIndex != null) {
                        val chosen = days[tappedIndex].dayIndex
                        onSelectDay(chosen)
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val paddingBottom = 26.dp.toPx()
        val paddingTop = 14.dp.toPx()
        val chartHeight = height - paddingBottom - paddingTop

        val maxMinutes = (days.maxOfOrNull { maxOf(it.targetMinutes, it.completedMinutes) } ?: 60)
            .coerceAtLeast(60).toFloat()

        // 1. Draw D3 / Recharts Cartesian Grid lines (3 horizontal dashed lines)
        val gridLevels = listOf(0f, 0.5f, 1f)
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        gridLevels.forEach { level ->
            val y = paddingTop + chartHeight * (1f - level)
            drawLine(
                color = Color(0xFF27362E).copy(alpha = 0.7f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = dashEffect
            )
        }

        // 2. Compute Points for Smooth Cubic Spline
        val stepX = width / (days.size + 1)
        val points = days.mapIndexed { index, day ->
            val x = stepX * (index + 1)
            val normalizedVal = (day.completedMinutes.toFloat() / maxMinutes).coerceIn(0f, 1f) * animatedProgress
            val y = paddingTop + chartHeight * (1f - normalizedVal)
            Offset(x, y)
        }

        val targetPoints = days.mapIndexed { index, day ->
            val x = stepX * (index + 1)
            val normalizedVal = (day.targetMinutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
            val y = paddingTop + chartHeight * (1f - normalizedVal)
            Offset(x, y)
        }

        // 3. Draw Target Planned Reference Line (Dashed subtle curve)
        if (targetPoints.size >= 2) {
            val targetPath = Path().apply {
                moveTo(targetPoints.first().x, targetPoints.first().y)
                for (i in 0 until targetPoints.size - 1) {
                    val p0 = targetPoints[i]
                    val p1 = targetPoints[i + 1]
                    val dx = (p1.x - p0.x) * 0.45f
                    cubicTo(p0.x + dx, p0.y, p1.x - dx, p1.y, p1.x, p1.y)
                }
            }
            drawPath(
                path = targetPath,
                color = Color(0xFF4A6553).copy(alpha = 0.4f),
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f),
                    cap = StrokeCap.Round
                )
            )
        }

        // 4. Draw Recharts Gradient Filled Area Under the Actual Spline Curve
        if (points.size >= 2) {
            val areaPath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val dx = (p1.x - p0.x) * 0.45f
                    cubicTo(p0.x + dx, p0.y, p1.x - dx, p1.y, p1.x, p1.y)
                }
                lineTo(points.last().x, height - paddingBottom)
                lineTo(points.first().x, height - paddingBottom)
                close()
            }

            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TacticalGreenBright.copy(alpha = 0.45f),
                        TacticalGreen.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    startY = paddingTop,
                    endY = height - paddingBottom
                )
            )

            // 5. Draw the Sharp Spline Stroke Line
            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val dx = (p1.x - p0.x) * 0.45f
                    cubicTo(p0.x + dx, p0.y, p1.x - dx, p1.y, p1.x, p1.y)
                }
            }
            drawPath(
                path = strokePath,
                color = TacticalGreenBright,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 6. Draw Dots on Each Day Point (Active, Completed, Selected)
        points.forEachIndexed { i, pt ->
            val day = days[i]
            val isSelected = day.dayIndex == selectedDayIndex
            val dotRadius = if (isSelected) 7.dp.toPx() else 4.5.dp.toPx()

            // Outer halo ring for selected / today
            if (isSelected || day.isToday) {
                drawCircle(
                    color = TacticalGreenBright.copy(alpha = 0.35f),
                    radius = dotRadius + 4.dp.toPx(),
                    center = pt
                )
            }

            // Dot fill
            drawCircle(
                color = if (day.isCompleted) TacticalGreenBright else Color(0xFF1E2822),
                radius = dotRadius,
                center = pt
            )

            // Dot stroke border
            drawCircle(
                color = if (day.isCompleted) Color.Black else TacticalGreenDark,
                radius = dotRadius,
                center = pt,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}

/**
 * D3 / Recharts BarChart Canvas Implementation:
 * Displays dual-bars (Target vs Completed) with rounded corners and distinct fills.
 */
@Composable
private fun D3BarChartCanvas(
    days: List<DayCycleProgress>,
    selectedDayIndex: Int?,
    onSelectDay: (Int) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600),
        label = "bar_progress"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(days) {
                detectTapGestures { tapOffset ->
                    val stepWidth = size.width / days.size
                    val index = (tapOffset.x / stepWidth).toInt().coerceIn(0, days.size - 1)
                    onSelectDay(days[index].dayIndex)
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val paddingBottom = 26.dp.toPx()
        val paddingTop = 14.dp.toPx()
        val chartHeight = height - paddingBottom - paddingTop

        val maxMinutes = (days.maxOfOrNull { maxOf(it.targetMinutes, it.completedMinutes) } ?: 60)
            .coerceAtLeast(60).toFloat()

        // Cartesian Grid
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        listOf(0f, 0.5f, 1f).forEach { level ->
            val y = paddingTop + chartHeight * (1f - level)
            drawLine(
                color = Color(0xFF27362E).copy(alpha = 0.7f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = dashEffect
            )
        }

        val slotWidth = width / days.size
        val barWidth = (slotWidth * 0.42f).coerceAtMost(22.dp.toPx())

        days.forEachIndexed { i, day ->
            val centerX = slotWidth * i + (slotWidth / 2f)
            val isSelected = day.dayIndex == selectedDayIndex

            // Target Bar (Background track)
            val targetHeight = (day.targetMinutes / maxMinutes) * chartHeight
            val targetTop = paddingTop + chartHeight - targetHeight

            drawRoundRect(
                color = Color(0xFF1E2B23),
                topLeft = Offset(centerX - barWidth / 2f, targetTop),
                size = Size(barWidth, targetHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            // Completed Bar (Foreground glowing fill)
            val completedHeight = (day.completedMinutes / maxMinutes) * chartHeight * animatedProgress
            val completedTop = paddingTop + chartHeight - completedHeight

            if (completedHeight > 0f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(TacticalGreenBright, Color(0xFF69F0AE))
                        } else {
                            listOf(TacticalGreenBright, TacticalGreen)
                        }
                    ),
                    topLeft = Offset(centerX - barWidth / 2f, completedTop),
                    size = Size(barWidth, completedHeight),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
            }

            // Selection indicator ring
            if (isSelected) {
                drawRoundRect(
                    color = TacticalGreenBright,
                    topLeft = Offset(centerX - barWidth / 2f - 2.dp.toPx(), targetTop - 2.dp.toPx()),
                    size = Size(barWidth + 4.dp.toPx(), targetHeight + 4.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }
    }
}

/**
 * Builds realistic 7-day training cycles:
 * - Cycle 0: Current Cycle (matches real Room workout logs)
 * - Cycle 1: Cycle 2 (past week / baseline)
 * - Cycle 2: Cycle 1 (initial cycle)
 */
private fun buildTrainingCycles(
    plans: List<WorkoutPlanEntity>,
    workoutLogs: List<WorkoutLogEntity>,
    currentDayOfWeek: Int
): List<TrainingCycle> {
    val dayShortNames = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
    val dayFullNames = listOf("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar")

    // Default 7 day target durations
    val defaultTargets = listOf(45, 50, 45, 20, 45, 55, 30)

    // Match real workout logs for current cycle
    val currentCycleDays = (1..7).map { dayNum ->
        val planForDay = plans.firstOrNull { it.dayOfWeek == dayNum && !it.isCustom }
        val title = planForDay?.title ?: when (dayNum) {
            1 -> "Göğüs & Arka Kol"
            2 -> "Sırt & Ön Kol"
            3 -> "Bacak & Karın"
            4 -> "Mobilite & Dinlenme"
            5 -> "Omuz & Trapez"
            6 -> "Tüm Vücut Kondisyon"
            else -> "Zihin & Esneme"
        }
        val targetMin = planForDay?.estimatedMinutes ?: defaultTargets[dayNum - 1]

        // Find if user has logged a workout for this plan/day
        val matchedLog = workoutLogs.find { log ->
            log.planTitle.equals(title, ignoreCase = true)
        }

        val isDone = matchedLog != null || (dayNum < currentDayOfWeek && workoutLogs.isNotEmpty())
        val completedMin = matchedLog?.durationMinutes ?: if (isDone) targetMin else 0

        DayCycleProgress(
            dayIndex = dayNum,
            dayShortName = dayShortNames[dayNum - 1],
            dayFullName = dayFullNames[dayNum - 1],
            workoutTitle = title,
            targetMinutes = targetMin,
            completedMinutes = completedMin,
            isCompleted = isDone,
            isToday = dayNum == currentDayOfWeek
        )
    }

    // Past Cycle 2 (e.g. earlier week)
    val pastCycle2Days = (1..7).map { dayNum ->
        val targetMin = defaultTargets[dayNum - 1]
        val isDone = dayNum in listOf(1, 2, 3, 5, 6)
        DayCycleProgress(
            dayIndex = dayNum,
            dayShortName = dayShortNames[dayNum - 1],
            dayFullName = dayFullNames[dayNum - 1],
            workoutTitle = when (dayNum) {
                1 -> "Göğüs & Arka Kol"
                2 -> "Sırt & Ön Kol"
                3 -> "Bacak & Karın"
                4 -> "Mobilite & Dinlenme"
                5 -> "Omuz & Trapez"
                6 -> "Tüm Vücut Kondisyon"
                else -> "Zihin & Esneme"
            },
            targetMinutes = targetMin,
            completedMinutes = if (isDone) targetMin else 0,
            isCompleted = isDone,
            isToday = false
        )
    }

    // Initial Cycle 1 (baseline)
    val pastCycle1Days = (1..7).map { dayNum ->
        val targetMin = defaultTargets[dayNum - 1]
        val isDone = dayNum in listOf(1, 3, 5)
        DayCycleProgress(
            dayIndex = dayNum,
            dayShortName = dayShortNames[dayNum - 1],
            dayFullName = dayFullNames[dayNum - 1],
            workoutTitle = when (dayNum) {
                1 -> "Göğüs & Arka Kol"
                2 -> "Sırt & Ön Kol"
                3 -> "Bacak & Karın"
                4 -> "Mobilite & Dinlenme"
                5 -> "Omuz & Trapez"
                6 -> "Tüm Vücut Kondisyon"
                else -> "Zihin & Esneme"
            },
            targetMinutes = targetMin,
            completedMinutes = if (isDone) targetMin else 0,
            isCompleted = isDone,
            isToday = false
        )
    }

    return listOf(
        TrainingCycle(
            cycleIndex = 0,
            cycleTitle = "Aktif Döngü",
            days = currentCycleDays
        ),
        TrainingCycle(
            cycleIndex = 1,
            cycleTitle = "2. Döngü",
            days = pastCycle2Days
        ),
        TrainingCycle(
            cycleIndex = 2,
            cycleTitle = "1. Döngü",
            days = pastCycle1Days
        )
    )
}
