package com.example.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.NotificationsActive
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
import com.example.data.NotificationHelper
import com.example.data.NotificationPreferences
import com.example.ui.theme.*

data class OnboardingOption(
    val title: String,
    val subtitle: String? = null,
    val rawValue: String = title
)

@Composable
fun OnboardingScreen(
    initialName: String = "",
    onComplete: (
        name: String,
        reason: String,
        problem: String,
        desiredPerson: String,
        profession: String,
        fitnessLevel: String,
        workoutLocation: String,
        targetGoal: String,
        motivationStyle: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 9

    var name by remember { mutableStateOf(if (initialName == "Emre") "" else initialName) }
    var selectedProfession by remember { mutableStateOf("Masa Başı & Ofis / Yazılımcı") }
    var selectedFitnessLevel by remember { mutableStateOf("Orta Düzey") }
    var selectedWorkoutLocation by remember { mutableStateOf("Spor Salonu") }
    var selectedTargetGoal by remember { mutableStateOf("Kas Kütlesi & Hipertrofi") }
    var selectedProblem by remember { mutableStateOf("Devam etmek") }
    var selectedMotivationStyle by remember { mutableStateOf("Stoik Felsefe") }
    var selectedDesiredPerson by remember { mutableStateOf("Demir İradeli Bir Savaşçı") }
    var selectedReason by remember { mutableStateOf("🔥 Disiplin kazanmak") }

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
            NotificationPreferences.setAskedPermission(context, true)
            if (isGranted) {
                val settings = NotificationPreferences.getSettings(context)
                NotificationPreferences.saveSettings(context, settings)
                Toast.makeText(context, "Bildirim izni verildi! Hatırlatıcılar aktif.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val nameSuggestions = listOf("Savaşçı", "Demir İrade", "Kararlı", "Şampiyon", "Yenilmez")

    val professionOptions = listOf(
        OnboardingOption("💻 Masa Başı & Ofis / Yazılımcı", "Postür bozuklukları, boyun gerginliği ve oturma yorgunluğunu kır."),
        OnboardingOption("🚶 Ayakta / Saha / Fiziksel İş", "Diz ve omurga yükünü hafiflet, toparlanmayı hızlandır."),
        OnboardingOption("🌙 Vardiyalı / Gece Çalışanı", "Düzensiz saatlerde sirkadiyen ritmi ve hormonları koru."),
        OnboardingOption("📚 Öğrenci / Yoğun Ders", "Sınav stresi ve beyin sisini dağıt; konsantrasyonu artır."),
        OnboardingOption("⚡ Serbest / Girişimci / Esnek", "Kendi öz-disiplin ve sabah zaferi rutinini inşa et.")
    )

    val fitnessLevelOptions = listOf(
        OnboardingOption("🟢 Başlangıç (Sıfırdan)", "Temel form, 2-3 kontrollü set, eklem güvenliği ve düzenli alışkanlık inşası.", "Başlangıç"),
        OnboardingOption("🟡 Orta Düzey (1-2 Yıl)", "Aşamalı aşırı yüklenme (progressive overload), 3-4 set ve bölgesel split.", "Orta Düzey"),
        OnboardingOption("🔴 İleri Düzey / Demir İrade", "Yüksek hacim, 4-5 set, süpersetler ve RPE 8-10 yoğunluk seviyesi.", "İleri Düzey")
    )

    val locationOptions = listOf(
        OnboardingOption("🏋️‍♂️ Tam Donanımlı Spor Salonu", "Barbell, dumbbell, kablo istasyonları ve bacak pres makineleri.", "Spor Salonu"),
        OnboardingOption("🏠 Evde & Vücut Ağırlığı (Calisthenics)", "Sıfır ekipmanla şınav varyasyonları, kapı/masa çekişleri, squat & karın.", "Evde & Vücut Ağırlığı"),
        OnboardingOption("⚡ Hibrit / Dambıl & Direnç Bandı", "Ayarlanabilir dambıllar ve direnç bantlarıyla evde veya salonda çok yönlü idman.", "Hibrit & Dambıl")
    )

    val targetGoalOptions = listOf(
        OnboardingOption("💪 Kas Kütlesi & Hipertrofi", "Maksimum kas hacmi, dolgunluk ve estetik zırh inşası (8-12 tekrar).", "Kas Kütlesi & Hipertrofi"),
        OnboardingOption("🔥 Yağ Yakımı & Definasyon", "Parçalı kaslar, yüksek kalori yakımı ve seans sonu metabolik kardiyo yakıcıları.", "Yağ Yakımı & Kilo Verme"),
        OnboardingOption("🧘 Postür Düzeltme & Ağrısız Beden", "Kamburluk onarımı, torasik omurga açıcılar ve gluteal aktivasyon.", "Postür & Ağrısız Beden"),
        OnboardingOption("⚡ Kondisyon & Atletik Güç", "Patlayıcı kuvvet, çeviklik, yüksek laktat eşiği ve fonksiyonel kondisyon.", "Kondisyon & Atletik Güç")
    )

    val problemOptions = listOf(
        OnboardingOption("🛌 Çok yorgunum / üşeniyorum", "Beyninin ürettiği sahte yorgunluk ve konfor alanı tuzağı.", "Üşenmek"),
        OnboardingOption("⏳ Vaktim hiç yok / gün yetmiyor", "Zaman eksikliği değil, öncelik ve sistem eksikliği.", "Vaktim hiç yok"),
        OnboardingOption("📱 Telefona dalıyorum / dikkatim dağılıyor", "Dopamin bağımlılığı ve anlık zevklere yenilme refleksi.", "Telefona dalıyorum"),
        OnboardingOption("🌪️ 1 gün aksatınca komple bırakıyorum", "Ya hep ya hiç sendromu; zinciri kırma disiplini kazan.", "1 gün aksatınca komple bırakıyorum"),
        OnboardingOption("🥱 Motivasyon gelmesini bekliyorum", "Heves peşinde koşmak yerine irade sistemine teslim ol.", "Motivasyon bekliyorum")
    )

    val motivationStyleOptions = listOf(
        OnboardingOption("🛡️ Stoik & Soğukkanlı Felsefe", "Marcus Aurelius, Seneca, Epictetus: 'Sadece kontrol edebildiklerine odaklan ve görevini yap.'", "Stoik Felsefe"),
        OnboardingOption("⚔️ Sert & Acımasız Gerçekler", "David Goggins & Jocko: 'Bahanelerin hepsi yalan. Canın istemese de o demiri kaldıracaksın.'", "Sert & Acımasız Gerçekler"),
        OnboardingOption("🏆 Şampiyon & Mamba Zihniyeti", "Kobe Bryant & Michael Jordan: 'Karanlıkta dökülen ter, ışıklar altında taçlanır.'", "Şampiyon & Mamba Zihniyeti"),
        OnboardingOption("🎯 Odak & Sade Eylem", "Miyamoto Musashi & Bruce Lee: 'Zihnini boşalt, dikkatini topla, tek bir hedefe kilitlen.'", "Sade Eylem & Odak")
    )

    val personaOptions = listOf(
        OnboardingOption("🦁 Demir İradeli Bir Savaşçı", "Duygularından bağımsız olarak her gün planına sadık kalan biri."),
        OnboardingOption("⚡ Durdurulamaz Bir İcraatçı", "Düşünmek yerine eyleme geçen ve bahanelere kulak asmayan biri."),
        OnboardingOption("🧘 Zihnen Sarsılmaz Bir Stoik", "Zorlukları bir engel değil, karakterini bileyecek bir fırsat gören biri."),
        OnboardingOption("💎 Yüksek Standartlı Bir Lider", "Günün ilk başarısını bedeninde kazanan, kendine saygılı biri.")
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("onboarding_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Top Nav & Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    IconButton(
                        onClick = { step -= 1 },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = TacticalGreen
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Text(
                    text = "DİSİPLİN PROTOKOLÜ & KİŞİSELLEŞTİRME",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TacticalGreenBright,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )

                Text(
                    text = "$step / $totalSteps",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            LinearProgressIndicator(
                progress = { step.toFloat() / totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = TacticalGreen,
                trackColor = DarkBorder
            )

            Spacer(modifier = Modifier.height(4.dp))

            when (step) {
                1 -> {
                    Text(
                        text = "1. Sana nasıl hitap edelim?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Günün selamlama mesajları, bildirimleri ve koçluk raporları bu isimle seslenecek.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_name_input"),
                        placeholder = { Text("Adını veya unvanını gir (örn. Can, Savaşçı...)", color = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceElevated,
                            unfocusedContainerColor = DarkSurface,
                            focusedBorderColor = TacticalGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Veya hızlı bir unvan seç:",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        nameSuggestions.forEach { suggestion ->
                            val isChosen = name == suggestion
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isChosen) TacticalGreenContainer else DarkSurfaceVariant)
                                    .border(1.dp, if (isChosen) TacticalGreen else DarkBorder, RoundedCornerShape(10.dp))
                                    .clickable { name = suggestion }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isChosen) TacticalGreenBright else TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                2 -> {
                    Text(
                        text = "2. Mesleğin ve çalışma tarzın nedir?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Antrenman, postür koruma ve beslenme önerileri mesleki biyomekaniğine göre uyarlanır.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    professionOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedProfession == opt.title,
                            onClick = { selectedProfession = opt.title }
                        )
                    }
                }

                3 -> {
                    Text(
                        text = "3. Fitness seviyen ve spor tecrüben nedir?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "⚡ Spor programındaki set sayıları, dinlenme süreleri ve ağırlık yüklenmeleri buna göre ölçeklenir.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TacticalGreenBright)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    fitnessLevelOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedFitnessLevel == opt.rawValue,
                            onClick = { selectedFitnessLevel = opt.rawValue }
                        )
                    }
                }

                4 -> {
                    Text(
                        text = "4. Nerede idman yapacaksın?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "🏋️‍♂️ Spor programındaki egzersiz listesi (Salon makineleri vs. Ev Calisthenics vs. Dambıl) anında buna göre baştan yazılır.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TacticalGreenBright)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    locationOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedWorkoutLocation == opt.rawValue,
                            onClick = { selectedWorkoutLocation = opt.rawValue }
                        )
                    }
                }

                5 -> {
                    Text(
                        text = "5. Temel fiziksel hedefin nedir?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "🔥 İdman temposu, tekrar aralıkları ve seans sonu metabolik yakıcılar hedefinle senkronize edilir.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TacticalGreenBright)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    targetGoalOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedTargetGoal == opt.rawValue,
                            onClick = { selectedTargetGoal = opt.rawValue }
                        )
                    }
                }

                6 -> {
                    Text(
                        text = "6. Seni en çok zorlayan zayıf noktan ne?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "🧠 Sistem ve Motivasyon AI, tam olarak bu bahaneyi parçalamak üzere sana özel disiplin sözleri üretecek.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TacticalGreenBright)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    problemOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedProblem == opt.rawValue,
                            onClick = { selectedProblem = opt.rawValue }
                        )
                    }
                }

                7 -> {
                    Text(
                        text = "7. Hangi disiplin felsefesi seni ayağa kaldırır?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "⚡ Günlük disiplin sözleri, sabah bildirimleri ve koçluk dili seçtiğin bu felsefeye göre konuşur.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TacticalGreenBright)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    motivationStyleOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedMotivationStyle == opt.rawValue,
                            onClick = { selectedMotivationStyle = opt.rawValue }
                        )
                    }
                }

                8 -> {
                    Text(
                        text = "8. Nasıl biri olmak istiyorsun?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Her antrenman ve tuttuğun her söz ile inşa edeceğin yeni kimliğini seç.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    personaOptions.forEach { opt ->
                        SelectableDetailedCard(
                            title = opt.title,
                            subtitle = opt.subtitle,
                            isSelected = selectedDesiredPerson == opt.title,
                            onClick = { selectedDesiredPerson = opt.title }
                        )
                    }
                }

                9 -> {
                    Text(
                        text = "9. Günlük Bildirim İzni",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Demir iradeni korumak ve bahaneleri susturmak için telefonuna günlük kişiselleştirilmiş bildirim gönderilsin.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF3B2800)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sabah Zihniyet Dozu (09:00)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "$selectedMotivationStyle dilinde güne başlarken stoik irade sözü.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TacticalGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = TacticalGreenBright,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Kişisel İdman Uyarısı (18:00)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "$selectedWorkoutLocation • $selectedTargetGoal programını tamamlama uyarısı.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("request_notification_permission_btn"),
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "BİLDİRİMLERE İZİN VER 🔔",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = TacticalGreenContainer),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TacticalGreenDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = TacticalGreenBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Bildirim izni aktif • Hatırlatıcılar kuruldu",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TacticalGreenBright,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        Button(
            onClick = {
                if (step == 1 && name.isBlank()) {
                    name = "Savaşçı"
                }
                if (step < totalSteps) {
                    if (step == totalSteps - 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    step += 1
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission && !NotificationPreferences.hasAskedPermission(context)) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    val finalName = name.trim().ifBlank { "Savaşçı" }
                    onComplete(
                        finalName,
                        selectedReason,
                        selectedProblem,
                        selectedDesiredPerson,
                        selectedProfession,
                        selectedFitnessLevel,
                        selectedWorkoutLocation,
                        selectedTargetGoal,
                        selectedMotivationStyle
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_continue_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = TacticalGreen,
                contentColor = TacticalGreenButtonText
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (step < totalSteps) "İLERLE" else "KİŞİSEL PROGRAMI & PROTOKOLÜ OLUŞTUR",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TacticalGreenButtonText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TacticalGreenButtonText
                )
            }
        }
    }
}

@Composable
fun SelectableDetailedCard(
    title: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .testTag("option_$title"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DarkSurfaceElevated else DarkSurface
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) TacticalGreen else DarkBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TacticalGreenBright else TextPrimary
                    )
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✓",
                    color = TacticalGreen,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}
