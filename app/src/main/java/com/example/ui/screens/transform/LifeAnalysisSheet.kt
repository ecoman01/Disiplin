package com.example.ui.screens.transform

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LifeAnalysisProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeAnalysisDiagnosticDialog(
    onDismiss: () -> Unit,
    onSaveProfile: (LifeAnalysisProfile) -> Unit,
    isMandatory: Boolean = false,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(0) }

    // Selected answers
    var disciplineAns by remember { mutableStateOf(1) } // 0: Düşük, 1: Orta, 2: Yüksek
    var procAns by remember { mutableStateOf("Yüksek") }
    var sportAns by remember { mutableStateOf("Orta") }
    var sleepAns by remember { mutableStateOf("Düşük") }
    var phoneAns by remember { mutableStateOf("4-6 Saat (Riskli)") }
    var motivationAns by remember { mutableStateOf(2) } // 0..3
    var routineAns by remember { mutableStateOf("Düzensiz, gününe göre") }
    var problemAns by remember { mutableStateOf("İstikrar (Başlayıp 2 hafta sonra bırakma)") }

    val questionsCount = 8

    ModalBottomSheet(
        onDismissRequest = {
            if (!isMandatory) {
                onDismiss()
            }
        },
        containerColor = DarkCardBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
            )
        },
        modifier = modifier.testTag("life_analysis_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TacticalGreenBright.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = TacticalGreenBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isMandatory) "HAYAT ANALİZİ (ZORUNLU)" else "KİŞİSEL HAYAT ANALİZİ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = if (isMandatory) "TRANSFORM'u başlatmak için: Adım ${step + 1} / $questionsCount" else "Adım ${step + 1} / $questionsCount",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                if (!isMandatory) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
                    }
                } else {
                    Surface(
                        color = TacticalGreenBright.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "GİRİŞ ADIMI",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // Progress bar
            LinearProgressIndicator(
                progress = { (step + 1) / questionsCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = TacticalGreenBright,
                trackColor = DarkSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // QUESTIONS
            when (step) {
                0 -> QuestionCard(
                    category = "DİSİPLİN",
                    question = "Kendine verdiğin sözleri tutma ve zorlandığında devam etme oranın nedir?",
                    options = listOf(
                        "Genelde ilk zorlukta ertelerim veya vazgeçerim" to 0,
                        "Bazen tutarım ama stresli günlerde dağılırım" to 1,
                        "Çoğu zaman kararlıyım ama istikrarım dalgalı" to 2,
                        "Tavizsizim, planladığım her şeyi yaparım" to 3
                    ),
                    selected = disciplineAns,
                    onSelect = { disciplineAns = it }
                )
                1 -> QuestionCard(
                    category = "ERTELEME",
                    question = "Önemli görevleri (spor, iş, ders) erteleme alışkanlığın ne seviyede?",
                    options = listOf(
                        "Düşük: Aklıma koyduğum an eyleme geçerim" to "Düşük",
                        "Orta: Birkaç saat ertelerim ama sonunda yaparım" to "Orta",
                        "Yüksek: Günlerce ertelerim ve suçluluk duyarım" to "Yüksek",
                        "Kritik: Sürekli son dakikaya bırakırım" to "Kritik"
                    ),
                    selected = procAns,
                    onSelect = { procAns = it }
                )
                2 -> QuestionCard(
                    category = "SPOR & HAREKET",
                    question = "Haftada kaç gün düzenli ve disiplinli şekilde spor yapıyorsun?",
                    options = listOf(
                        "Hiç yapmıyorum veya ayda 1-2 kez" to "Düşük",
                        "Haftada 1-2 gün düzensiz yapıyorum" to "Orta",
                        "Haftada 3-4 gün düzenli gidiyorum" to "Yüksek",
                        "Haftada 5+ gün disiplinli idmandayım" to "Zirve"
                    ),
                    selected = sportAns,
                    onSelect = { sportAns = it }
                )
                3 -> QuestionCard(
                    category = "UYKU KALİTESİ",
                    question = "Sabahları uyandığında enerjin nasıl ve uyku düzenin ne durumda?",
                    options = listOf(
                        "Düşük: Gece geç yatarım, sabah yorgun ve halsiz uyanırım" to "Düşük",
                        "Orta: Hafta içi idare eder ama hafta sonu dağılır" to "Orta",
                        "İyi: Günde 7-8 saat kaliteli uyur, dinç kalkarım" to "Yüksek"
                    ),
                    selected = sleepAns,
                    onSelect = { sleepAns = it }
                )
                4 -> QuestionCard(
                    category = "TELEFON VE EKRAN SÜRESİ",
                    question = "Günde sosyal medya, reels veya telefonda harcadığın süre ortalama ne kadar?",
                    options = listOf(
                        "1-2 saat (Kontrollü ve bilinçli)" to "Kontrollü",
                        "2-4 saat (Bazen kontrolü kaybediyorum)" to "Orta",
                        "4-6 saat (Ciddi dopamin tuzağı, zamanı yutuyor)" to "Riskli (4-6 Saat)",
                        "6+ saat (Telefon bağımlılığı hissediyorum)" to "Kritik Bağımlılık (6+ Saat)"
                    ),
                    selected = phoneAns,
                    onSelect = { phoneAns = it }
                )
                5 -> QuestionCard(
                    category = "MOTİVASYON VE ENERJİ",
                    question = "Gün içindeki zihinsel motivasyonun ve odaklanma gücün nasıl?",
                    options = listOf(
                        "Çok çabuk sıkılırım, odaklanmakta zorlanırım" to 0,
                        "Dalgalı, kahve ve son dakika stresiyle odaklanırım" to 1,
                        "İyi, belirlediğim saatlerde derin çalışırım" to 2,
                        "Üst düzey, zihnim berrak ve hedefe kilitli" to 3
                    ),
                    selected = motivationAns,
                    onSelect = { motivationAns = it }
                )
                6 -> QuestionCard(
                    category = "GÜNLÜK DÜZEN",
                    question = "Güne başlarken ve bitirirken sabit bir sabah/gece rutinin var mı?",
                    options = listOf(
                        "Hiçbir rutinim yok, rastgele yaşıyorum" to "Yok",
                        "Sadece kahve içip işe/okula koşturuyorum" to "Düzensiz",
                        "Basit bir sabah rutinim var ama gece düzensiz" to "Kısmi Düzen",
                        "Sabah ve akşam tavizsiz bir disiplin sistemim var" to "Mükemmel Düzen"
                    ),
                    selected = routineAns,
                    onSelect = { routineAns = it }
                )
                7 -> QuestionCard(
                    category = "EN BÜYÜK PROBLEMİN",
                    question = "Seni şu an hedeflerinden en çok alıkoyan asıl engel hangisi?",
                    options = listOf(
                        "İstikrar (Başlayıp 2-3 hafta sonra motivasyon bitince bırakmak)" to "İstikrar",
                        "Erteleme ve Odak Dağınıklığı (Zamanı telefonda harcamak)" to "Erteleme",
                        "Yetersiz Enerji ve Düzensiz Uyku" to "Uyku & Enerji",
                        "Net bir sistem ve plana sahip olmamak" to "Plansızlık"
                    ),
                    selected = problemAns,
                    onSelect = { problemAns = it }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 0) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = BorderStroke(1.dp, DarkBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Geri", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        if (step < questionsCount - 1) {
                            step++
                        } else {
                            // Calculate profile
                            val discScore = 45 + (disciplineAns * 15)
                            val motScore = 50 + (motivationAns * 14)
                            val calculated = LifeAnalysisProfile(
                                isCompleted = true,
                                disciplineScore = discScore.coerceIn(30, 95),
                                procrastinationLevel = procAns,
                                sportLevel = sportAns,
                                sleepLevel = sleepAns,
                                phoneUsageLevel = phoneAns,
                                motivationScore = motScore.coerceIn(40, 95),
                                dailyRoutineScore = if (routineAns.contains("Mükemmel")) 90 else if (routineAns.contains("Kısmi")) 65 else 45,
                                goalClarityScore = 75,
                                biggestProblem = problemAns,
                                completedDate = System.currentTimeMillis()
                            )
                            onSaveProfile(calculated)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(if (step > 0) 1.5f else 1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalGreenBright,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (step < questionsCount - 1) "Sonraki Adım" else "Analizi Tamamla & Profili Gör",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> QuestionCard(
    category: String,
    question: String,
    options: List<Pair<String, T>>,
    selected: T,
    onSelect: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            color = TacticalGreenBright.copy(alpha = 0.12f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TacticalGreenBright,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        options.forEach { (label, value) ->
            val isChosen = selected == value
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(value) }
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isChosen) TacticalGreenBright else DarkBorder
                        ),
                        RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChosen) TacticalGreenBright.copy(alpha = 0.08f) else DarkSurface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (isChosen) TacticalGreenBright else Color.Transparent)
                            .border(1.5.dp, if (isChosen) TacticalGreenBright else TextSecondary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChosen) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isChosen) TextPrimary else TextSecondary,
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}
