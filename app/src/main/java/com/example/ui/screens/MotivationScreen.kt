package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Work
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
import com.example.ui.screens.nutrition.StructuredRecipeCard
import com.example.ui.theme.*
import com.example.util.ShareHelper
import kotlinx.coroutines.delay

enum class MotivationAiSubTab(val label: String, val icon: String) {
    QUOTES("Zihniyet & Sözler", "⚡"),
    NUTRITION("Beslenme & Tarifler", "🥗"),
    COACH("AI Meslek Koçu", "🎯")
}

@Composable
fun MotivationScreen(
    quotes: List<MotivationQuote>,
    currentQuoteIndex: Int,
    favoriteQuoteIds: Set<String>,
    dailyHabits: List<DisciplineHabit>,
    onNextQuote: () -> Unit,
    onSelectQuote: (Int) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleHabit: (String) -> Unit,
    userName: String,
    userProfession: String = "Masa Başı & Ofis / Yazılımcı",
    userProfile: UserProfile? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var activeSubTab by remember { mutableStateOf(MotivationAiSubTab.QUOTES) }

    // Quotes category & filter
    var selectedCategory by remember { mutableStateOf("⭐ Sana Özel") }
    val quoteCategories = listOf("⭐ Sana Özel", "Tümü", "Sert Gerçekler", "Mamba", "Stoik", "İrade", "Bahaneleri Sustur", "Odak")

    val filteredQuotes = remember(selectedCategory, quotes, userProfile) {
        when (selectedCategory) {
            "⭐ Sana Özel" -> DisciplineEngine.getPersonalizedQuotes(userProfile)
            "Tümü" -> quotes
            else -> quotes.filter { it.category == selectedCategory }
        }
    }

    val activeQuote = remember(currentQuoteIndex, filteredQuotes, quotes) {
        if (filteredQuotes.isNotEmpty()) {
            val safeIdx = currentQuoteIndex.coerceIn(0, filteredQuotes.size - 1)
            filteredQuotes[safeIdx]
        } else {
            quotes.firstOrNull() ?: MotivationQuote(
                id = "def",
                quote = "Disiplin özgürlüktür.",
                author = "Jocko Willink",
                context = "Temel İlke",
                takeaway = "Canının istemesini bekleme, yap.",
                category = "İrade"
            )
        }
    }

    val isFavorite = favoriteQuoteIds.contains(activeQuote.id)

    // 5-Second Rule State
    var fiveSecondCountdown by remember { mutableIntStateOf(0) }
    var fiveSecondRunning by remember { mutableStateOf(false) }
    var fiveSecondTriggered by remember { mutableStateOf(false) }

    // Breathing Focus State
    var breathingActive by remember { mutableStateOf(false) }
    var breathSecondsLeft by remember { mutableIntStateOf(60) }
    var breathPhase by remember { mutableStateOf("Nefes Al (4s)") }

    // Nutrition Recipe detail modal & filter
    var selectedRecipe by remember { mutableStateOf<NutritionRecipe?>(null) }
    var selectedNutritionCategory by remember { mutableStateOf("Tümü") }

    val filteredRecipes = remember(selectedNutritionCategory) {
        NutritionEngine.getRecipesByCategory(selectedNutritionCategory)
    }

    // Profession advice for active user
    val professionAdvice = remember(userProfession) {
        ProfessionCoachEngine.getAdviceForProfession(userProfession)
    }

    // Timer effect for 5-second rule
    LaunchedEffect(fiveSecondRunning) {
        if (fiveSecondRunning) {
            fiveSecondCountdown = 5
            fiveSecondTriggered = false
            while (fiveSecondCountdown > 0) {
                delay(1000)
                fiveSecondCountdown -= 1
            }
            fiveSecondRunning = false
            fiveSecondTriggered = true
        }
    }

    // Timer effect for breathing
    LaunchedEffect(breathingActive) {
        if (breathingActive) {
            breathSecondsLeft = 60
            while (breathSecondsLeft > 0 && breathingActive) {
                val cyclePos = (60 - breathSecondsLeft) % 12
                breathPhase = when {
                    cyclePos < 4 -> "Nefes Al (4s)... 🌬️"
                    cyclePos < 8 -> "Nefesini Tut (4s)... 🧘"
                    else -> "Yavaşça Ver (4s)... 🍃"
                }
                delay(1000)
                breathSecondsLeft -= 1
            }
            breathingActive = false
            breathPhase = "Harika! Zihnin odaklandı. 🎯"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp)
            .testTag("motivation_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 36.dp)
    ) {
        // 🚀 MOTIVASYON AI HEADER
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "MOTİVASYON AI",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = TacticalGreenContainer
                    ) {
                        Text(
                            text = "AI GÜÇLÜ",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Text(
                    text = "Stoik Zihniyet • Sağlıklı Tarifler & Tatlılar • Mesleğe Özel Koçluk",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // 🔘 SEGMENTED NAVIGATION TABS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MotivationAiSubTab.entries.forEach { tab ->
                    val isSelected = activeSubTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) TacticalGreen else Color.Transparent)
                            .clickable { activeSubTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = tab.icon, fontSize = 14.sp)
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = if (isSelected) TacticalGreenButtonText else TextSecondary,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // SUB-TAB 1: ZİHNİYET & SÖZLER (Quotes, Stoic, Tools)
        // ==========================================
        if (activeSubTab == MotivationAiSubTab.QUOTES) {
            // Kategori Seçici
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quoteCategories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) TacticalGreenContainer else DarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) TacticalGreen else DarkBorder),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TacticalGreenBright else TextSecondary
                                )
                            )
                        }
                    }
                }
            }

            // 🎯 KİŞİSEL ZİHNİYET DİREKTİFİ
            if (userProfile != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, TacticalGreenDark)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TacticalGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🎯", fontSize = 18.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "ZİHNİYET: ${userProfile.motivationStyle.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                                Text(
                                    text = "Aşılacak Zayıf Nokta: “${userProfile.biggestProblem}” • Sözler buna göre filtrelenmiştir.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // ⭐ ÖNE ÇIKAN SÖZ (HERO CARD)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("featured_quote_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(26.dp),
                    border = BorderStroke(1.dp, TacticalGreenDark)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TacticalGreenContainer
                            ) {
                                Text(
                                    text = activeQuote.context.uppercase(),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = { onToggleFavorite(activeQuote.id) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("fav_quote_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Favoriye Ekle",
                                        tint = if (isFavorite) WarningOrange else TextSecondary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Motivasyon Sözü", "\"${activeQuote.quote}\" — ${activeQuote.author}")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Söz panoya kopyalandı! 📋", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Kopyala",
                                        tint = TextSecondary
                                    )
                                }
                            }
                        }

                        Text(
                            text = "“${activeQuote.quote}”",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary,
                                lineHeight = 30.sp
                            )
                        )

                        Text(
                            text = "— ${activeQuote.author}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        // Çıkarım / Eylem Kutusu
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceVariant)
                                .padding(14.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "💡", fontSize = 18.sp)
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "EYLEME DÖKÜLECEK İLKE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TacticalGreenBright,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                    Text(
                                        text = activeQuote.takeaway,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onNextQuote,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("next_quote_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TacticalGreen,
                                contentColor = TacticalGreenButtonText
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Text(
                                    text = "YENİ MOTİVASYON GETİR",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            // ⏱️ 5 SANİYE İRADE PROTOKOLÜ
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "⚡", fontSize = 22.sp)
                            Column {
                                Text(
                                    text = "5-SANİYE KURALI (ERTELEME KIRICI)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Beynin bahane üretmeden önce hemen harekete geç.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        if (fiveSecondRunning) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(TacticalGreenContainer)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$fiveSecondCountdown",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TacticalGreenBright
                                    )
                                )
                                Text(
                                    text = "Geri sayım bittiğinde hemen ayağa kalk ve başla!",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        } else if (fiveSecondTriggered) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🔥 VAKİT TAMAM! BAHANELERİ BIRAK VE HAREKETE GEÇ!",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = WarningOrange
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Button(
                            onClick = { fiveSecondRunning = true },
                            enabled = !fiveSecondRunning,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("five_sec_rule_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceVariant,
                                contentColor = TacticalGreenBright
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (fiveSecondRunning) "SAYILIYOR..." else "5 SANİYE PROTOKOLÜNÜ BAŞLAT",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 🧘 60 SANİYE ZİHİN SIFIRLAMA & ODAK NEFESİ
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = TacticalGreenBright
                            )
                            Column {
                                Text(
                                    text = "60 SANİYE STOİK ODAK NEFESİ",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Kortizolü düşür, dikkati toparla ve iradeni merkezle.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        if (breathingActive) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceElevated)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = breathPhase,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TacticalGreenBright
                                    )
                                )
                                Text(
                                    text = "Kalan: $breathSecondsLeft saniye",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Button(
                            onClick = { breathingActive = !breathingActive },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (breathingActive) OldYouRed else DarkSurfaceVariant,
                                contentColor = if (breathingActive) Color.White else TacticalGreenBright
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (breathingActive) "EGZERSİZİ DURDUR" else "ODAK NEFESİNE BAŞLA (1 DK)",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // SUB-TAB 2: BESLENME & FIT TARİFLER (Nutrition & Desserts)
        // ==========================================
        if (activeSubTab == MotivationAiSubTab.NUTRITION) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(1.dp, TacticalGreenDark)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🥗", fontSize = 24.sp)
                            Column {
                                Text(
                                    text = "DİSİPLİNLİ BESLENME & FIT MUTFAK",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Şekersiz tatlılar, yüksek protein ve pratik sağlıklı menüler",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "“Formunu antrenmanda kazanırsın, iradeni ise canın tatlı çektiğinde yaptığın seçimlerle inşa edersin.”",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            // Kategori Seçici (Tatlılar, Protein, Pratik, vb.)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NutritionEngine.categories.forEach { cat ->
                        val isSelected = selectedNutritionCategory == cat
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) TacticalGreenContainer else DarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) TacticalGreen else DarkBorder),
                            modifier = Modifier.clickable { selectedNutritionCategory = cat }
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TacticalGreenBright else TextSecondary
                                )
                            )
                        }
                    }
                }
            }

            // Tarif Kartları (Structured Recipe Cards)
            items(filteredRecipes) { recipe ->
                StructuredRecipeCard(
                    recipe = recipe,
                    defaultExpanded = false,
                    onShare = {
                        val shareText = """
                            🥗 ${recipe.title} (${recipe.category})
                            ⏱️ Süre: ${recipe.prepMinutes} dk | 🔥 ${recipe.calories} kcal
                            💪 Protein: ${recipe.proteinG}g | 🌾 Karb: ${recipe.carbsG}g | 🥑 Yağ: ${recipe.fatG}g
                            
                            🛒 MALZEMELER:
                            ${recipe.ingredients.joinToString("\n") { "• $it" }}
                            
                            👨‍🍳 HAZIRLANIŞ:
                            ${recipe.instructions.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n")}
                            
                            💡 DİSİPLİN NOTU:
                            ${recipe.disciplineTip}
                        """.trimIndent()
                        ShareHelper.shareText(context, shareText, "Fit Tarif: ${recipe.title}")
                    },
                    onCopyIngredients = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Malzemeler", recipe.ingredients.joinToString("\n") { "• $it" })
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Malzemeler panoya kopyalandı!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Beslenme Taktikleri Bölümü
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📌 ALTIN DİSİPLİN BESLENME KURALLARI",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TacticalGreenBright
                            )
                        )

                        NutritionEngine.disciplineTips.forEach { tip ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "✓", color = TacticalGreenBright, fontWeight = FontWeight.Black)
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // SUB-TAB 3: AI MESLEK KOÇU (Profession Adaptive Advice)
        // ==========================================
        if (activeSubTab == MotivationAiSubTab.COACH) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(1.5.dp, TacticalGreenDark)
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
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TacticalGreenContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Work,
                                        contentDescription = null,
                                        tint = TacticalGreenBright,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "KAYITLI ÇALIŞMA TARZIN",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TacticalGreenBright,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Text(
                                text = "AI Analizi",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(professionAdvice.iconEmoji, fontSize = 24.sp)
                            Text(
                                text = professionAdvice.professionTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                        }

                        Text(
                            text = professionAdvice.tagLine,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Postür ve Ergonomi Tavsiyesi
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🧘", fontSize = 22.sp)
                            Text(
                                text = "POSTÜR & ERGONOMİ",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                        }
                        Text(
                            text = professionAdvice.postureAndBodyAdvice,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Antrenman Zamanlaması & Rutini
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "⏰", fontSize = 22.sp)
                            Text(
                                text = "İDEAL İDMAN ZAMANLAMASI",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                        }
                        Text(
                            text = professionAdvice.timingAndRoutineAdvice,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Mesai İçi Mikro Egzersiz
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "⚡", fontSize = 22.sp)
                            Text(
                                text = "GÜN İÇİ HIZLI MİKRO HAREKET",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = professionAdvice.quickDeskExercise,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TacticalGreenBright,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }

            // Beslenme & Enerji Yönetimi
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🥗", fontSize = 22.sp)
                            Text(
                                text = "MESLEĞİNE ÖZEL BESLENME PLANI",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                        }
                        Text(
                            text = professionAdvice.nutritionTip,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Meslek İrade İlkesi
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔥", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "GÜNLÜK ZİHNİYET İLKESİ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TacticalGreenBright
                                )
                            )
                            Text(
                                text = professionAdvice.mindsetRule,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // TARİF DETAY MODALI (Ingredients & Steps)
    // ==========================================
    selectedRecipe?.let { recipe ->
        AlertDialog(
            onDismissRequest = { selectedRecipe = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(recipe.emoji, fontSize = 26.sp)
                    Column {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${recipe.category} • ${recipe.prepMinutes} dk",
                            style = MaterialTheme.typography.labelSmall.copy(color = TacticalGreenBright)
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Makrolar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TacticalGreenContainer,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${recipe.proteinG}g",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TacticalGreenBright
                                    )
                                )
                                Text(
                                    text = "Protein",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TacticalGreenBright)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${recipe.calories}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Kalori",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }
                    }

                    // Malzemeler
                    Text(
                        text = "🛒 MALZEMELER:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright
                        )
                    )
                    recipe.ingredients.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("•", color = TacticalGreenBright, fontWeight = FontWeight.Bold)
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                            )
                        }
                    }

                    Divider(color = DarkBorder, thickness = 0.5.dp)

                    // Hazırlanış Adımları
                    Text(
                        text = "👨‍🍳 HAZIRLANIŞ ADIMLARI:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TacticalGreenBright
                        )
                    )
                    recipe.instructions.forEachIndexed { idx, step ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                color = TacticalGreenBright,
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }

                    // Disiplin Notu
                    if (recipe.disciplineTip.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "💡 ${recipe.disciplineTip}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TacticalGreenBright,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedRecipe = null },
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalGreen)
                ) {
                    Text("KAPAT", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }
}
