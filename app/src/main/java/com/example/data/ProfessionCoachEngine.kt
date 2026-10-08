package com.example.data

data class ProfessionAdvice(
    val professionKey: String,
    val professionTitle: String,
    val iconEmoji: String,
    val tagLine: String,
    val postureAndBodyAdvice: String,
    val timingAndRoutineAdvice: String,
    val nutritionTip: String,
    val mindsetRule: String,
    val quickDeskExercise: String
)

data class ProfessionExerciseSpec(
    val name: String,
    val targetSets: Int,
    val targetReps: Int,
    val defaultWeightKg: Float,
    val orderIndex: Int
)

data class ProfessionDayPlan(
    val dayOfWeek: Int,
    val dayName: String,
    val title: String,
    val targetMuscles: String,
    val estimatedMinutes: Int,
    val iconType: String,
    val focusDescription: String,
    val exercises: List<ProfessionExerciseSpec>
)

object ProfessionCoachEngine {

    val availableProfessions = listOf(
        "Masa Başı & Ofis / Yazılımcı",
        "Ayakta / Saha / Fiziksel İş",
        "Vardiyalı / Gece Çalışanı",
        "Öğrenci / Yoğun Ders",
        "Serbest / Girişimci / Esnek"
    )

    fun getAdviceForProfession(profession: String?): ProfessionAdvice {
        val clean = profession?.trim() ?: ""
        return when {
            clean.contains("Masa", ignoreCase = true) || clean.contains("Ofis", ignoreCase = true) || clean.contains("Yazılımcı", ignoreCase = true) -> {
                ProfessionAdvice(
                    professionKey = "desk_office",
                    professionTitle = "Masa Başı & Ofis / Yazılımcı",
                    iconEmoji = "💻",
                    tagLine = "Postürü Koru, Beyin Sisini Dağıt, Oturma Yorgunluğunu Kır",
                    postureAndBodyAdvice = "Günde 6-10 saat oturmak kalça fleksörlerini (psoas) kısaltır ve torasik omurgayı öne büker. İdmanlarında arka omuz, sırt ve glute (kalça) aktivasyonuna 2 kat ağırlık ver.",
                    timingAndRoutineAdvice = "Mesai bitiminde doğrudan antrenmana geç veya öğle arasında 15 dakikalık hızlı bir tempolu yürüyüş yap. Eve gidip koltuğa oturursan irade direncin yarıya iner.",
                    nutritionTip = "Masa başında farkında olmadan atıştırmayı kes. Masanda sadece su ve yeşil çay bulundur. Öğleden sonra şekerli kahve yerine şekersiz fit tatlı veya çiğ badem tercih et.",
                    mindsetRule = "Klavye başındaki zihinsel yorgunluk fiziksel yorgunluk değildir. Vücudun hareket etmek için can atıyor; beyninin ürettiği 'yorgunum' yalanına inanma.",
                    quickDeskExercise = "Her 50 dakikada: Sandalyeden kalk, 10 adet omuz retraksiyonu (kürek kemiklerini birbirine sıkıştır) ve 20 saniye göğüs germe yap."
                )
            }
            clean.contains("Ayakta", ignoreCase = true) || clean.contains("Saha", ignoreCase = true) || clean.contains("Fiziksel", ignoreCase = true) -> {
                ProfessionAdvice(
                    professionKey = "standing_field",
                    professionTitle = "Ayakta / Saha / Fiziksel İş",
                    iconEmoji = "🚶",
                    tagLine = "Eklemleri Koru, Toparlanmayı Hızlandır, Gücü Stratejik Kullan",
                    postureAndBodyAdvice = "Tüm gün ayakta olmak bel ve dizlere statik yük bindirir. Ağır omurga sıkıştırmalı hareketler (ağır squat/deadlift) yerine unileteral (tek bacak lunge, step-up) ve mobilite hareketlerine odaklan.",
                    timingAndRoutineAdvice = "Fiziksel mesainin üzerine 2 saatlik maraton idmanlar yapma; 35-40 dakikalık odaklı, dinlendirici ve hipertrofi odaklı seanslar senin için çok daha etkilidir.",
                    nutritionTip = "Terleme ve sürekli ayakta kalma elektrolit ve sodyum kaybına yol açar. Günde en az 3-3.5 litre su ve 1 şişe maden suyu iç. Her öğünde temiz protein miktarını yüksek tut.",
                    mindsetRule = "Fiziksel çalışmak disiplin kasını zaten eğitiyor. İdman ise bedenin yıpranan taraflarını dengelemek ve zırh gibi sağlamlaştırmak içindir.",
                    quickDeskExercise = "Akşamları eve geldiğinde bacaklarını 10 dakika boyunca duvara dik yasla (bacak elevasyonu); biriken venöz kan kalbe döner ve bacak ağırlığın anında kalkar."
                )
            }
            clean.contains("Vardiyalı", ignoreCase = true) || clean.contains("Gece", ignoreCase = true) -> {
                ProfessionAdvice(
                    professionKey = "shift_worker",
                    professionTitle = "Vardiyalı / Gece Çalışanı",
                    iconEmoji = "🌙",
                    tagLine = "Sirkadiyen Ritmi Yönet, Uyku Hormonunu Koru, Düzeni Sen Belirle",
                    postureAndBodyAdvice = "Düzensiz uyku kortizolü artırır ve kas yıkımını tetikleyebilir. Vücudunu aşırı yıpratmayacak RPE 7-8 şiddetinde tutarlı ağırlık idmanları planla.",
                    timingAndRoutineAdvice = "Antrenmanını vardiya sonrasına değil, ana uykundan uyandıktan 1-2 saat sonraya koy. Enerjin en yüksekken bas.",
                    nutritionTip = "Gece vardiyasında ağır yağlı yemeklerden kaçın; sindirim sistemi gece yavaşlar. Kolay sindirilen proteinli salatalar, haşlanmış yumurta ve yulaflı tarifleri çantanda taşı.",
                    mindsetRule = "Saatlerin herkesle aynı olmayabilir ama hedefin aynı. Saat kaç olursa olsun, günün senin kontrolünde olduğunu antrenmanınla göster.",
                    quickDeskExercise = "Vardiya ortasında esneme: Kedi-deve esnemesi ve derin diyafram nefesiyle sempatik sinir sistemini yatıştır."
                )
            }
            clean.contains("Öğrenci", ignoreCase = true) || clean.contains("Ders", ignoreCase = true) -> {
                ProfessionAdvice(
                    professionKey = "student",
                    professionTitle = "Öğrenci / Yoğun Ders",
                    iconEmoji = "📚",
                    tagLine = "Zihinsel Keskinlik, Beyin Büyüme Faktörü (BDNF) ve Zaman Yönetimi",
                    postureAndBodyAdvice = "Masa başında ders çalışmak boyun düzleşmesine zemin hazırlar. İdmanlarında yüz çekişleri (face pull) ve sırt kaslarını çalıştırarak dik duruşunu koru.",
                    timingAndRoutineAdvice = "Sınav dönemlerinde sporu bırakma! Sadece 30 dakikaya indir. İdman sonrası salgılanan dopamin ve BDNF ders anlama ve ezberleme hızını %40 artırır.",
                    nutritionTip = "Kantin fast-food'u ve abur cubur beyin sisi yapar. Yanında çiğ kuruyemiş, meyve ve fıstık ezmeli fit bar taşı. Bütçe dostu lor peyniri ve yumurta en iyi dostundur.",
                    mindsetRule = "Zamanım yok bahanesini reddet. Günde 30 dakikanı sağlığına ayırmayan biri ileride saatlerini hastalıklara harcar.",
                    quickDeskExercise = "Pomodoro molalarında (her 25 dk ders sonrası): 10 şınav veya 15 hava squatı yaparak beynine taze kan pompala."
                )
            }
            else -> {
                ProfessionAdvice(
                    professionKey = "freelance_flexible",
                    professionTitle = "Serbest / Girişimci / Esnek",
                    iconEmoji = "⚡",
                    tagLine = "Öz-Disiplin Zirvesi, Rutin İnşası ve Zihinsel Netlik",
                    postureAndBodyAdvice = "Patronun sen olduğun için çalışma saatlerin dağılabilir. Postürünü ve enerjini korumak için haftalık 4-5 gün sabit idman çizelgesi uygula.",
                    timingAndRoutineAdvice = "Antrenmanını günün ilk başarısı olarak sabah saatlerine kilitle (07:30 - 09:00). Güne bir zaferle başlayan girişimci gün boyu yenilmez hisseder.",
                    nutritionTip = "Evden çalışırken sürekli buzdolabını açma refleksini kır. Öğün saatlerini alarm gibi netleştir ve porsiyonlarını önceden tartıp hazırla.",
                    mindsetRule = "Özgürlük programsızlık değildir. Gerçek özgürlük, kendi belirlediğin yüksek standartlara kimse bakmıyorken bile sadık kalmaktır.",
                    quickDeskExercise = "Öğleden sonra rehavet çöktüğünde: 60 saniye boyunca soğuk suyla yüzünü yıka ve 1 dakikalık plank duruşuna geç."
                )
            }
        }
    }

    fun get7DayWorkoutPlanForProfession(profession: String?): List<ProfessionDayPlan> {
        val clean = profession?.trim() ?: ""
        return when {
            clean.contains("Masa", ignoreCase = true) || clean.contains("Ofis", ignoreCase = true) || clean.contains("Yazılımcı", ignoreCase = true) -> {
                listOf(
                    ProfessionDayPlan(
                        dayOfWeek = 1,
                        dayName = "Pazartesi",
                        title = "Pazartesi: Torasik Sırt & Postür Kurtarma",
                        targetMuscles = "Üst Sırt • Kanat • Arka Omuz (Kamburluk Onarımı)",
                        estimatedMinutes = 45,
                        iconType = "pull",
                        focusDescription = "Masa başında öne yuvarlanan omuzları ve sıkışan kürek kemiklerini açar.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Face Pull (Yüz Çekişi)", 3, 15, 20f, 1),
                            ProfessionExerciseSpec("Lat Pulldown (Geniş Tutuş)", 3, 10, 55f, 2),
                            ProfessionExerciseSpec("Göğüs Destekli Dumbbell Row", 3, 10, 18f, 3),
                            ProfessionExerciseSpec("Y-Raise / Scapular Wall Slide", 3, 12, 5f, 4),
                            ProfessionExerciseSpec("Incline Dumbbell Biceps Curl", 3, 12, 12f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 2,
                        dayName = "Salı",
                        title = "Salı: Göğüs Kafesi Açıcı & Triceps",
                        targetMuscles = "Üst Göğüs • Göğüs Kafesi • Triceps",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Klavye başında kısalan pektoral kasları esnetip göğüs kafesini genişletir.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Incline Dumbbell Bench Press", 3, 10, 22f, 1),
                            ProfessionExerciseSpec("Dumbbell Pullover (Göğüs Açıcı)", 3, 12, 16f, 2),
                            ProfessionExerciseSpec("Kablo Göğüs Açış / Şınav", 3, 12, 14f, 3),
                            ProfessionExerciseSpec("Overhead Triceps Uzatma", 3, 12, 18f, 4),
                            ProfessionExerciseSpec("Halat Triceps Pushdown", 3, 12, 20f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 3,
                        dayName = "Çarşamba",
                        title = "Çarşamba: Glute & Hamstring Aktivasyonu",
                        targetMuscles = "Kalça • Arka Bacak • Bel Koruması",
                        estimatedMinutes = 45,
                        iconType = "legs",
                        focusDescription = "Uzun oturma sonucu uyuyan kalça kaslarını (gluteal amnezi) uyandırır.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barbell / Dumbbell Hip Thrust", 3, 12, 45f, 1),
                            ProfessionExerciseSpec("Romanian Deadlift (Hamstring)", 3, 10, 50f, 2),
                            ProfessionExerciseSpec("Bulgarian Split Squat", 3, 10, 12f, 3),
                            ProfessionExerciseSpec("Deadbug (Bel Korumalı Core)", 3, 15, 0f, 4),
                            ProfessionExerciseSpec("Glute Bridge Tek Bacak", 3, 12, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 4,
                        dayName = "Perşembe",
                        title = "Perşembe: Omuz & Boyun Stabilizasyonu",
                        targetMuscles = "Yan Omuz • Ön Omuz • Boyun Hattı",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Ekrana bakmaktan gerilen boyun omurlarını güçlendirip dik duruşu kilitler.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Dumbbell Overhead Press", 3, 10, 16f, 1),
                            ProfessionExerciseSpec("Kablo Lateral Raise", 4, 12, 8f, 2),
                            ProfessionExerciseSpec("Reverse Pec Deck (Arka Omuz)", 3, 12, 35f, 3),
                            ProfessionExerciseSpec("Band Pull-Apart", 3, 15, 0f, 4),
                            ProfessionExerciseSpec("Trapez Dumbbell Shrug", 3, 12, 22f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 5,
                        dayName = "Cuma",
                        title = "Cuma: Alt Gövde Gücü & Core Çelikleştirme",
                        targetMuscles = "Ön Bacak • Kalf • Karın Duvarı",
                        estimatedMinutes = 45,
                        iconType = "legs",
                        focusDescription = "Diz ve omurga sağlığı için stabil ön bacak ve dayanıklı karın zırhı.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Goblet Squat (Dambıl Göğüste)", 3, 10, 24f, 1),
                            ProfessionExerciseSpec("Leg Extension", 3, 12, 45f, 2),
                            ProfessionExerciseSpec("Lying Leg Curl", 3, 12, 40f, 3),
                            ProfessionExerciseSpec("Standing Calf Raise", 4, 15, 50f, 4),
                            ProfessionExerciseSpec("Side Plank (Yan Karın)", 3, 30, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 6,
                        dayName = "Cumartesi",
                        title = "Cumartesi: Dinamik Dekompresyon & Terleme",
                        targetMuscles = "Tüm Vücut Fonksiyonel • Kardiyovasküler",
                        estimatedMinutes = 35,
                        iconType = "fullbody",
                        focusDescription = "Haftanın birikmiş statik oturma stresini metabolik hızlanmayla atar.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Kettlebell / Dambıl Swing", 4, 15, 20f, 1),
                            ProfessionExerciseSpec("Şınav (Göğüs & Core)", 3, 15, 0f, 2),
                            ProfessionExerciseSpec("Asılı Diz Çekme (Hanging Knee)", 3, 12, 0f, 3),
                            ProfessionExerciseSpec("Barfiks / Bant Destekli", 3, 8, 0f, 4),
                            ProfessionExerciseSpec("Mountain Climber", 3, 20, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 7,
                        dayName = "Pazar",
                        title = "Pazar: Omurga Mobilite & Dinamik Esneme",
                        targetMuscles = "Omurga • Kalça Fleksör • Göğüs Germe",
                        estimatedMinutes = 30,
                        iconType = "recovery",
                        focusDescription = "Yeni çalışma haftasına sıfır ağrı ve dik omurgayla başlamak için restorasyon.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Kedi - Deve Esnemesi (Cat-Cow)", 3, 12, 0f, 1),
                            ProfessionExerciseSpec("Kapı Eşiğinde Göğüs Germe", 3, 30, 0f, 2),
                            ProfessionExerciseSpec("Güvercin Duruşu (Kalça Açıcı)", 3, 30, 0f, 3),
                            ProfessionExerciseSpec("Torasik Omurga Rotasyonu", 3, 10, 0f, 4),
                            ProfessionExerciseSpec("30 Dk Tempolu Yürüyüş", 1, 30, 0f, 5)
                        )
                    )
                )
            }
            clean.contains("Ayakta", ignoreCase = true) || clean.contains("Saha", ignoreCase = true) || clean.contains("Fiziksel", ignoreCase = true) -> {
                listOf(
                    ProfessionDayPlan(
                        dayOfWeek = 1,
                        dayName = "Pazartesi",
                        title = "Pazartesi: Eklem Dostu Üst Gövde İtiş",
                        targetMuscles = "Göğüs • Ön Omuz • Triceps",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Ayakta yorulan omurgaya minimum eksenel yük bindiren göğüs odaklı itiş.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Dumbbell Bench Press", 3, 10, 24f, 1),
                            ProfessionExerciseSpec("Incline Machine Press", 3, 10, 45f, 2),
                            ProfessionExerciseSpec("Cable Göğüs Sıkıştırma", 3, 12, 15f, 3),
                            ProfessionExerciseSpec("Dips / Makine Dips", 3, 10, 0f, 4),
                            ProfessionExerciseSpec("Kablo Triceps Halat", 3, 12, 20f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 2,
                        dayName = "Salı",
                        title = "Salı: Bel Korumalı Sırt Çekiş",
                        targetMuscles = "Kanat • Trapez • Biceps • Arka Omuz",
                        estimatedMinutes = 40,
                        iconType = "pull",
                        focusDescription = "Beli koruyan göğüs destekli sıralar ile sırt güçlendirme.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Göğüs Destekli T-Bar / Dambıl Row", 3, 10, 20f, 1),
                            ProfessionExerciseSpec("Lat Pulldown (Geniş)", 3, 10, 55f, 2),
                            ProfessionExerciseSpec("Seated Cable Row (Oturarak)", 3, 12, 45f, 3),
                            ProfessionExerciseSpec("Incline Dumbbell Biceps Curl", 3, 10, 14f, 4),
                            ProfessionExerciseSpec("Face Pull (Eklem Dostu)", 3, 15, 20f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 3,
                        dayName = "Çarşamba",
                        title = "Çarşamba: Düşük Darbeli Bacak & Eklem Rahatlatma",
                        targetMuscles = "Bacak • Kalça • Diz Sağlığı",
                        estimatedMinutes = 40,
                        iconType = "legs",
                        focusDescription = "Ayakta durmanın diz ve bel yükünü nötrleyen güvenli makineler.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Leg Press (Bele Yüksüz)", 3, 12, 90f, 1),
                            ProfessionExerciseSpec("Seated Leg Curl", 3, 12, 45f, 2),
                            ProfessionExerciseSpec("Dumbbell Step-Up (Basamak)", 3, 10, 12f, 3),
                            ProfessionExerciseSpec("Oturarak Kalf Kaldırma (Seated)", 4, 15, 35f, 4),
                            ProfessionExerciseSpec("Deadbug Karın Kontrolü", 3, 12, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 4,
                        dayName = "Perşembe",
                        title = "Perşembe: Omuz & Kol Hipertrofisi",
                        targetMuscles = "Yan Omuz • Ön Kol • Triceps • Biceps",
                        estimatedMinutes = 35,
                        iconType = "push",
                        focusDescription = "Oturarak yapılan eklem dostu omuz ve kol pompası.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Seated Dumbbell Shoulder Press", 3, 10, 16f, 1),
                            ProfessionExerciseSpec("Kablo Lateral Raise", 3, 12, 8f, 2),
                            ProfessionExerciseSpec("Hammer Curl (Çekiç Kol)", 3, 12, 14f, 3),
                            ProfessionExerciseSpec("Triceps Halat Pushdown", 3, 12, 20f, 4),
                            ProfessionExerciseSpec("Dumbbell Shrug (Omuz Silkme)", 3, 12, 22f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 5,
                        dayName = "Cuma",
                        title = "Cuma: Çelik Core & Bel Zırhı",
                        targetMuscles = "Karın • Bel Kasları • Denge",
                        estimatedMinutes = 35,
                        iconType = "core",
                        focusDescription = "Ağır kaldırma ve yürüyüşlerde omurgayı çelik gibi saran koruyucu core.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Suitcase Carry (Tek Dambıl Taşıma)", 3, 40, 20f, 1),
                            ProfessionExerciseSpec("Pallof Press (Dönüş Direnci)", 3, 12, 15f, 2),
                            ProfessionExerciseSpec("Plank (Statik Duruş)", 3, 45, 0f, 3),
                            ProfessionExerciseSpec("Kuş-Köpek (Bird-Dog Bel Egzersizi)", 3, 12, 0f, 4),
                            ProfessionExerciseSpec("Hanging Knee Raise", 3, 10, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 6,
                        dayName = "Cumartesi",
                        title = "Cumartesi: Fonksiyonel Güç & Mobilite",
                        targetMuscles = "Üst Gövde Fonksiyonel • Dayanıklılık",
                        estimatedMinutes = 35,
                        iconType = "fullbody",
                        focusDescription = "Eklemleri yıpratmadan fonksiyonel kapasiteyi artıran akıcı seans.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Dumbbell Floor Press (Yerde İtiş)", 3, 10, 20f, 1),
                            ProfessionExerciseSpec("Tek Kol Dumbbell Row", 3, 10, 20f, 2),
                            ProfessionExerciseSpec("Vücut Ağırlığı Squat", 3, 15, 0f, 3),
                            ProfessionExerciseSpec("Karın Crunch", 3, 20, 0f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 7,
                        dayName = "Pazar",
                        title = "Pazar: Bacak Elevasyonu & Tam Eklem Restorasyonu",
                        targetMuscles = "Bacak Damarları • Bel • Ayak Tabanı",
                        estimatedMinutes = 25,
                        iconType = "recovery",
                        focusDescription = "Biriken venöz kanı boşaltır, ayak tabanı ve baldır gerginliğini sıfırlar.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Bacakları Duvara Yaslama (Elevasyon)", 1, 10, 0f, 1),
                            ProfessionExerciseSpec("Baldır & Aşil Tendonu Germesi", 3, 30, 0f, 2),
                            ProfessionExerciseSpec("Köpük Rulo / Topla Ayak Masajı", 2, 5, 0f, 3),
                            ProfessionExerciseSpec("Derin Diyafram Nefes Protokolü", 1, 10, 0f, 4)
                        )
                    )
                )
            }
            clean.contains("Vardiyalı", ignoreCase = true) || clean.contains("Gece", ignoreCase = true) -> {
                listOf(
                    ProfessionDayPlan(
                        dayOfWeek = 1,
                        dayName = "Pazartesi",
                        title = "Pazartesi: Kompakt İtiş (35 Dk)",
                        targetMuscles = "Göğüs • Omuz • Triceps",
                        estimatedMinutes = 35,
                        iconType = "push",
                        focusDescription = "Vardiya öncesi veya sonrası sinir sistemini tüketmeyen odaklı itiş.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Dumbbell Bench Press", 3, 10, 22f, 1),
                            ProfessionExerciseSpec("Arnold Omuz Press", 3, 10, 14f, 2),
                            ProfessionExerciseSpec("Triceps Halat Pushdown", 3, 12, 18f, 3),
                            ProfessionExerciseSpec("Standart Şınav", 2, 15, 0f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 2,
                        dayName = "Salı",
                        title = "Salı: Kompakt Çekiş (35 Dk)",
                        targetMuscles = "Sırt • Biceps • Arka Omuz",
                        estimatedMinutes = 35,
                        iconType = "pull",
                        focusDescription = "Yorgunluk eşiğini aşmayan, duruşu güçlendiren pratik çekiş.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Lat Pulldown", 3, 10, 50f, 1),
                            ProfessionExerciseSpec("Seated Cable Row", 3, 10, 45f, 2),
                            ProfessionExerciseSpec("Dumbbell Hammer Curl", 3, 12, 12f, 3),
                            ProfessionExerciseSpec("Face Pull", 3, 15, 18f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 3,
                        dayName = "Çarşamba",
                        title = "Çarşamba: Stratejik Bacak & Dolaşım",
                        targetMuscles = "Bacak • Kalça • Metabolizma",
                        estimatedMinutes = 35,
                        iconType = "legs",
                        focusDescription = "Düzensiz uykunun yarattığı ödemi bacak pompasıyla atar.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Goblet Squat (Dambıl)", 3, 10, 20f, 1),
                            ProfessionExerciseSpec("Romanian Deadlift", 3, 10, 40f, 2),
                            ProfessionExerciseSpec("Walking Lunge (Adımlama)", 3, 10, 10f, 3),
                            ProfessionExerciseSpec("Standing Calf Raise", 3, 15, 40f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 4,
                        dayName = "Perşembe",
                        title = "Perşembe: Üst Vücut Süperset Seansı",
                        targetMuscles = "Göğüs + Sırt Süperset",
                        estimatedMinutes = 30,
                        iconType = "fullbody",
                        focusDescription = "Zaman tasarruflu zıt kas grubu süperseti; 30 dakikada maksimum pompa.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Incline Dambıl Press + Row Süperset", 3, 10, 18f, 1),
                            ProfessionExerciseSpec("Lateral Raise + Biceps Curl Süperset", 3, 12, 10f, 2),
                            ProfessionExerciseSpec("Şınav + Barfiks Asılma", 2, 12, 0f, 3)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 5,
                        dayName = "Cuma",
                        title = "Cuma: Kardiyovasküler Denge & Karın",
                        targetMuscles = "Kardiyo • Dolaşım • Core",
                        estimatedMinutes = 30,
                        iconType = "core",
                        focusDescription = "Kortizol düşürücü hafif tempo yürüyüş ve sağlam karın duruşları.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Hafif Eğimli Yürüyüş", 1, 20, 0f, 1),
                            ProfessionExerciseSpec("Plank", 3, 45, 0f, 2),
                            ProfessionExerciseSpec("Mekik / Crunch", 3, 15, 0f, 3)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 6,
                        dayName = "Cumartesi",
                        title = "Cumartesi: Dinamik Güç & Terleme",
                        targetMuscles = "Omuz • Bacak • Kondisyon",
                        estimatedMinutes = 30,
                        iconType = "fullbody",
                        focusDescription = "Kısa ve patlayıcı güç seansı.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Dumbbell Clean & Press", 3, 8, 14f, 1),
                            ProfessionExerciseSpec("Dambıl Squat to Press", 3, 10, 12f, 2),
                            ProfessionExerciseSpec("Mountain Climber", 3, 20, 0f, 3)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 7,
                        dayName = "Pazar",
                        title = "Pazar: Melatonin & Uyku Restorasyonu",
                        targetMuscles = "Sinir Sistemi • Diyafram • Esneme",
                        estimatedMinutes = 25,
                        iconType = "recovery",
                        focusDescription = "Vardiya geçişlerinde derin uykuya geçişi hızlandıran parasempatik ritim.",
                        exercises = listOf(
                            ProfessionExerciseSpec("4-7-8 Diyafram Nefes Seansı", 1, 8, 0f, 1),
                            ProfessionExerciseSpec("Omurga Kedi-Deve Hareketi", 3, 10, 0f, 2),
                            ProfessionExerciseSpec("Bel Rahatlatıcı Diz Göğse Çekme", 3, 30, 0f, 3),
                            ProfessionExerciseSpec("Ilık Duş Sonrası Tam Esneme", 1, 10, 0f, 4)
                        )
                    )
                )
            }
            clean.contains("Öğrenci", ignoreCase = true) || clean.contains("Ders", ignoreCase = true) -> {
                listOf(
                    ProfessionDayPlan(
                        dayOfWeek = 1,
                        dayName = "Pazartesi",
                        title = "Pazartesi: Göğüs & Biceps (Ders Stresi Boşaltma)",
                        targetMuscles = "Göğüs • Ön Omuz • Biceps",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Haftaya güçlü bir dopamin patlamasıyla başlamak için klasik hipertrofi.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barbell / Dambıl Bench Press", 3, 10, 50f, 1),
                            ProfessionExerciseSpec("Incline Dumbbell Press", 3, 10, 20f, 2),
                            ProfessionExerciseSpec("Barbell Biceps Curl", 3, 10, 22f, 3),
                            ProfessionExerciseSpec("Hammer Curl (Çekiç)", 3, 12, 12f, 4),
                            ProfessionExerciseSpec("Şınav (Sonlandırma)", 2, 15, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 2,
                        dayName = "Salı",
                        title = "Salı: Sırt & Boyun Dikleştirme",
                        targetMuscles = "Sırt • Kanat • Boyun Hattı",
                        estimatedMinutes = 40,
                        iconType = "pull",
                        focusDescription = "Ders çalışırken öne eğilen baş ve boyun hattını düzeltir.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Lat Pulldown / Barfiks", 3, 10, 55f, 1),
                            ProfessionExerciseSpec("Barbell Row (Eğilerek Çekiş)", 3, 10, 45f, 2),
                            ProfessionExerciseSpec("Face Pull (Arka Omuz & Boyun)", 3, 15, 20f, 3),
                            ProfessionExerciseSpec("Seated Cable Row", 3, 12, 40f, 4),
                            ProfessionExerciseSpec("Plank (Karın)", 3, 45, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 3,
                        dayName = "Çarşamba",
                        title = "Çarşamba: Bacak Gücü & Dopamin Yükseltme",
                        targetMuscles = "Kuadriseps • Kalça • Hamstring",
                        estimatedMinutes = 40,
                        iconType = "legs",
                        focusDescription = "Bacak idmanı beyne BDNF salgılatır; sınav odaklanmasını 2 katına çıkarır.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barbell / Goblet Squat", 3, 10, 55f, 1),
                            ProfessionExerciseSpec("Walking Lunge", 3, 10, 12f, 2),
                            ProfessionExerciseSpec("Romanian Deadlift", 3, 10, 45f, 3),
                            ProfessionExerciseSpec("Standing Calf Raise", 3, 15, 45f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 4,
                        dayName = "Perşembe",
                        title = "Perşembe: Omuz & Triceps (Odak Patlaması)",
                        targetMuscles = "Omuz Başları • Triceps",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Geniş omuzlar ve güçlü kollar için hedefli hacim çalışması.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Overhead Dumbbell Press", 3, 10, 16f, 1),
                            ProfessionExerciseSpec("Dumbbell Lateral Raise", 4, 12, 8f, 2),
                            ProfessionExerciseSpec("Triceps Halat Pushdown", 3, 12, 20f, 3),
                            ProfessionExerciseSpec("Dips / Sandalye Dips", 3, 12, 0f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 5,
                        dayName = "Cuma",
                        title = "Cuma: Hızlı Tüm Vücut Pompalaması (30 Dk)",
                        targetMuscles = "Tüm Vücut Hızlı Hacim",
                        estimatedMinutes = 30,
                        iconType = "fullbody",
                        focusDescription = "Hafta sonu öncesi 30 dakikada tüm ana kas gruplarını uyarır.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Şınav", 3, 15, 0f, 1),
                            ProfessionExerciseSpec("Dumbbell Row", 3, 10, 20f, 2),
                            ProfessionExerciseSpec("Goblet Squat", 3, 12, 22f, 3),
                            ProfessionExerciseSpec("Dumbbell Biceps Curl", 3, 12, 12f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 6,
                        dayName = "Cumartesi",
                        title = "Cumartesi: Dinamik Güç & Açık Hava",
                        targetMuscles = "Kondisyon • Bacak • Akciğer",
                        estimatedMinutes = 35,
                        iconType = "fullbody",
                        focusDescription = "Kapalı ders ortamından çıkıp açık havada ciğerleri temizleyen dinamik güç.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Açık Hava Tempolu Koşu", 1, 20, 0f, 1),
                            ProfessionExerciseSpec("Park Barfiksi / Asılma", 3, 8, 0f, 2),
                            ProfessionExerciseSpec("Zıplamalı Squat (Jump Squat)", 3, 12, 0f, 3),
                            ProfessionExerciseSpec("Karın Leg Raise", 3, 15, 0f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 7,
                        dayName = "Pazar",
                        title = "Pazar: Zihin Tazeleme & Yeni Hafta Vizyonu",
                        targetMuscles = "Genel Rahatlama & Postür",
                        estimatedMinutes = 30,
                        iconType = "recovery",
                        focusDescription = "Sınav ve ders stresinden zihni arındırma ve tam toparlanma.",
                        exercises = listOf(
                            ProfessionExerciseSpec("30 Dk Açık Hava Doğa Yürüyüşü", 1, 30, 0f, 1),
                            ProfessionExerciseSpec("Tam Vücut Esneme Rutini", 1, 15, 0f, 2),
                            ProfessionExerciseSpec("Derin Odaklanma Nefesi", 1, 10, 0f, 3)
                        )
                    )
                )
            }
            else -> {
                // Serbest / Girişimci / Esnek
                listOf(
                    ProfessionDayPlan(
                        dayOfWeek = 1,
                        dayName = "Pazartesi",
                        title = "Pazartesi: Sabah Zaferi: Göğüs & Triceps İtiş",
                        targetMuscles = "Göğüs • Ön Omuz • Triceps",
                        estimatedMinutes = 45,
                        iconType = "push",
                        focusDescription = "Güne erken bir zaferle başlayıp gün boyu yenilmez hissettiren itiş.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barbell / Dambıl Bench Press", 3, 10, 55f, 1),
                            ProfessionExerciseSpec("Incline Dumbbell Press", 3, 10, 22f, 2),
                            ProfessionExerciseSpec("Dips (Ağırlıklı/Vücut)", 3, 10, 0f, 3),
                            ProfessionExerciseSpec("Triceps Halat Pushdown", 3, 12, 20f, 4),
                            ProfessionExerciseSpec("Şınav (Bitirici)", 2, 15, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 2,
                        dayName = "Salı",
                        title = "Salı: Odak & Dayanıklılık: Sırt & Biceps Çekiş",
                        targetMuscles = "Kanat • Trapez • Arka Omuz • Biceps",
                        estimatedMinutes = 45,
                        iconType = "pull",
                        focusDescription = "Yüksek odak ve zihinsel direnç inşa eden yoğun çekiş protokolü.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barfiks / Lat Pulldown", 3, 10, 60f, 1),
                            ProfessionExerciseSpec("Barbell / Dambıl Row", 3, 10, 50f, 2),
                            ProfessionExerciseSpec("Seated Cable Row", 3, 12, 45f, 3),
                            ProfessionExerciseSpec("Barbell Biceps Curl", 3, 10, 24f, 4),
                            ProfessionExerciseSpec("Face Pull", 3, 15, 20f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 3,
                        dayName = "Çarşamba",
                        title = "Çarşamba: İrade Sınavı: Bacak & Çelik Core",
                        targetMuscles = "Kuadriseps • Hamstring • Karın",
                        estimatedMinutes = 50,
                        iconType = "legs",
                        focusDescription = "En zor görevleri ilk sıraya koyma prensibini bacak idmanında test et.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Barbell Back Squat", 3, 10, 60f, 1),
                            ProfessionExerciseSpec("Romanian Deadlift", 3, 10, 55f, 2),
                            ProfessionExerciseSpec("Bulgarian Split Squat", 3, 10, 14f, 3),
                            ProfessionExerciseSpec("Standing Calf Raise", 4, 15, 50f, 4),
                            ProfessionExerciseSpec("Hanging Leg Raise (Karın)", 3, 12, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 4,
                        dayName = "Perşembe",
                        title = "Perşembe: Zirve Performans: Omuz & Üst Sırt",
                        targetMuscles = "Omuz Başları • Üst Sırt • Boyun",
                        estimatedMinutes = 40,
                        iconType = "push",
                        focusDescription = "Lider duruşu ve güçlü postür sağlayan 3 boyutlu omuz çalışması.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Standing Military Overhead Press", 3, 10, 35f, 1),
                            ProfessionExerciseSpec("Dumbbell Lateral Raise", 4, 12, 10f, 2),
                            ProfessionExerciseSpec("Reverse Pec Deck Fly", 3, 12, 35f, 3),
                            ProfessionExerciseSpec("Dumbbell Shrug", 3, 12, 24f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 5,
                        dayName = "Cuma",
                        title = "Cuma: Metabolik Güç & Kondisyon Pompası",
                        targetMuscles = "Tüm Vücut Hibrit • Kollar",
                        estimatedMinutes = 40,
                        iconType = "fullbody",
                        focusDescription = "Girişimci enerjisini tavana çıkaran metabolik fonksiyonel güç.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Kettlebell / Dambıl Clean & Press", 3, 10, 16f, 1),
                            ProfessionExerciseSpec("Goblet Squat", 3, 12, 24f, 2),
                            ProfessionExerciseSpec("Incline Dambıl Curl", 3, 10, 14f, 3),
                            ProfessionExerciseSpec("Triceps Dips", 3, 12, 0f, 4),
                            ProfessionExerciseSpec("Plank", 3, 60, 0f, 5)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 6,
                        dayName = "Cumartesi",
                        title = "Cumartesi: Fonksiyonel Zindelik & Açık Alan",
                        targetMuscles = "Kondisyon • Fonksiyonel Dayanıklılık",
                        estimatedMinutes = 35,
                        iconType = "fullbody",
                        focusDescription = "Zihni şarj eden, açık alan veya stüdyo fonksiyonel antrenmanı.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Tempolu Koşu veya Sprint", 1, 20, 0f, 1),
                            ProfessionExerciseSpec("Dambıl Yürüyüşü (Farmer's Walk)", 3, 50, 24f, 2),
                            ProfessionExerciseSpec("Burpee veya Zıplamalı Şınav", 3, 10, 0f, 3),
                            ProfessionExerciseSpec("Hanging Knee Raise", 3, 12, 0f, 4)
                        )
                    ),
                    ProfessionDayPlan(
                        dayOfWeek = 7,
                        dayName = "Pazar",
                        title = "Pazar: Stoik Zihin & Aktif Toparlanma",
                        targetMuscles = "Tam Vücut Mobilite & Odak",
                        estimatedMinutes = 30,
                        iconType = "recovery",
                        focusDescription = "Gelecek haftanın stratejilerini düşünürken bedeni tazeleyen toparlanma.",
                        exercises = listOf(
                            ProfessionExerciseSpec("Doğa Yürüyüşü / Hafif Bisiklet", 1, 30, 0f, 1),
                            ProfessionExerciseSpec("Tüm Vücut Derin Esneme", 1, 15, 0f, 2),
                            ProfessionExerciseSpec("Haftalık Vizyon & Zihinsel Reset", 1, 10, 0f, 3)
                        )
                    )
                )
            }
        }
    }

    fun getHomeBodyweight7DayPlan(): List<ProfessionDayPlan> {
        return listOf(
            ProfessionDayPlan(
                dayOfWeek = 1,
                dayName = "Pazartesi",
                title = "Pazartesi: Üst Vücut İtiş & Göğüs Pompası (Evde)",
                targetMuscles = "Göğüs • Triceps • Ön Omuz",
                estimatedMinutes = 40,
                iconType = "push",
                focusDescription = "Evde hiçbir ekipman gerekmeden üst vücut itiş gücünü ve göğüs kaslarını inşa eder.",
                exercises = listOf(
                    ProfessionExerciseSpec("Standart Şınav (Push-Up)", 3, 12, 0f, 1),
                    ProfessionExerciseSpec("Sandalye / Koltuk Dips", 3, 12, 0f, 2),
                    ProfessionExerciseSpec("Pike Push-Up (Omuz Açısı)", 3, 10, 0f, 3),
                    ProfessionExerciseSpec("Yavaş Negatif Geniş Şınav", 3, 10, 0f, 4),
                    ProfessionExerciseSpec("Plank to Push-Up", 3, 10, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 2,
                dayName = "Salı",
                title = "Salı: Sırt, Çekiş & Biceps Aktivasyonu (Evde)",
                targetMuscles = "Sırt • Kanat • Biceps • Arka Omuz",
                estimatedMinutes = 40,
                iconType = "pull",
                focusDescription = "Masa altı veya kapı eşiği ile evde çekiş gücünü ve duruş dikliğini sağlar.",
                exercises = listOf(
                    ProfessionExerciseSpec("Masa Altı / Kapı Ters Row", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Süperman Sırt & Bel Sıkıştırma", 3, 15, 0f, 2),
                    ProfessionExerciseSpec("Havlu ile İzometrik Biceps Çekiş", 3, 12, 0f, 3),
                    ProfessionExerciseSpec("Y-W-T Yerde Skapula Aktivasyonu", 3, 12, 0f, 4),
                    ProfessionExerciseSpec("Kapı Eşiği Statik Çekiş Duruşu", 3, 30, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 3,
                dayName = "Çarşamba",
                title = "Çarşamba: Çelik Alt Beden & Kalça (Evde)",
                targetMuscles = "Kuadriseps • Kalça • Hamstring • Baldır",
                estimatedMinutes = 45,
                iconType = "legs",
                focusDescription = "Vücut ağırlığıyla bacakları cayır cayır yakan, diz dostu bacak seansı.",
                exercises = listOf(
                    ProfessionExerciseSpec("Bulgar Split Squat (Koltuk Destekli)", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Patlayıcı Air Squat (Çömelme)", 3, 15, 0f, 2),
                    ProfessionExerciseSpec("Tek Bacak Glute Bridge (Kalça Köprüsü)", 3, 12, 0f, 3),
                    ProfessionExerciseSpec("Yürüyüş Lunge (Adımlama)", 3, 12, 0f, 4),
                    ProfessionExerciseSpec("Duvar Destekli Baldır Yükseltme", 4, 20, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 4,
                dayName = "Perşembe",
                title = "Perşembe: Omurga Açıcı, Mobilite & Aktif Dinlenme",
                targetMuscles = "Omurga • Kalça Eklemi • Boyun",
                estimatedMinutes = 30,
                iconType = "recovery",
                focusDescription = "Evde kas ağrılarını çözen, esneklik kazandıran derin toparlanma protokolü.",
                exercises = listOf(
                    ProfessionExerciseSpec("Kedi-Deve & Torasik Omurga Açma", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Dünyanın En İyi Esnemesi", 3, 8, 0f, 2),
                    ProfessionExerciseSpec("Kalça Fleksör & Hamstring Germe", 3, 45, 0f, 3),
                    ProfessionExerciseSpec("Derin Çömelme (Deep Squat Hold)", 3, 60, 0f, 4),
                    ProfessionExerciseSpec("25 Dakika Açık Hava Tempolu Yürüyüş", 1, 25, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 5,
                dayName = "Cuma",
                title = "Cuma: Tüm Vücut Calisthenics Hipertrofi",
                targetMuscles = "Tüm Vücut • Kollar • Omuz",
                estimatedMinutes = 40,
                iconType = "fullbody",
                focusDescription = "Vücut ağırlığıyla tam kontrol sağlayan fonksiyonel dayanıklılık seansı.",
                exercises = listOf(
                    ProfessionExerciseSpec("Elmas Şınav (Diamond Push-Up)", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Sandalye Step-Up (Basamak Çıkma)", 3, 12, 0f, 2),
                    ProfessionExerciseSpec("Dağ Tırmanışı (Mountain Climber)", 3, 20, 0f, 3),
                    ProfessionExerciseSpec("Süperman Plank Geçişi", 3, 10, 0f, 4),
                    ProfessionExerciseSpec("Şınav Duruşunda Omuz Dokunma", 3, 16, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 6,
                dayName = "Cumartesi",
                title = "Cumartesi: Zırh Core & Yağ Yakıcı Karın",
                targetMuscles = "Karın • Bel • Metabolik Kondisyon",
                estimatedMinutes = 35,
                iconType = "fullbody",
                focusDescription = "Merkez bölgeyi (core) kaya gibi sertleştiren ve yağ yakan yüksek tempo.",
                exercises = listOf(
                    ProfessionExerciseSpec("Burpee (Zıplamalı Şınav)", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Hollow Body Hold (Karın Kilidi)", 3, 30, 0f, 2),
                    ProfessionExerciseSpec("Yatarak Bacak Kaldırma (Leg Raise)", 3, 15, 0f, 3),
                    ProfessionExerciseSpec("Russian Twist (Gövde Dönüşü)", 3, 20, 0f, 4),
                    ProfessionExerciseSpec("Plank (Demir Karın)", 3, 60, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 7,
                dayName = "Pazar",
                title = "Pazar: Zihinsel Reset, Nefes & Yenilenme",
                targetMuscles = "Zihin • Dolaşım • Sinir Sistemi",
                estimatedMinutes = 25,
                iconType = "recovery",
                focusDescription = "Yeni haftaya sıfır stres ve yüksek odakla başlamak için dinginlik ritüeli.",
                exercises = listOf(
                    ProfessionExerciseSpec("Tüm Vücut Akış Esnemesi", 1, 15, 0f, 1),
                    ProfessionExerciseSpec("4-7-8 Diyafram Nefes Egzersizi", 1, 10, 0f, 2),
                    ProfessionExerciseSpec("Haftalık İrade Muhasebesi & Planlama", 1, 10, 0f, 3)
                )
            )
        )
    }

    fun getHybridDumbbell7DayPlan(): List<ProfessionDayPlan> {
        return listOf(
            ProfessionDayPlan(
                dayOfWeek = 1,
                dayName = "Pazartesi",
                title = "Pazartesi: Dambıl İtiş & Göğüs-Omuz (Hibrit)",
                targetMuscles = "Göğüs • Ön Omuz • Triceps",
                estimatedMinutes = 45,
                iconType = "push",
                focusDescription = "Dambıllarla göğüs hacmini ve omuz genişliğini artıran güçlü açılış.",
                exercises = listOf(
                    ProfessionExerciseSpec("Dumbbell Floor / Bench Press", 3, 10, 16f, 1),
                    ProfessionExerciseSpec("Dumbbell Overhead Shoulder Press", 3, 10, 12f, 2),
                    ProfessionExerciseSpec("Dumbbell Lateral Raise (Yan Omuz)", 3, 12, 7f, 3),
                    ProfessionExerciseSpec("Dumbbell Floor Fly (Göğüs Açış)", 3, 12, 10f, 4),
                    ProfessionExerciseSpec("Dumbbell Overhead Triceps Extension", 3, 12, 12f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 2,
                dayName = "Salı",
                title = "Salı: Dambıl Sırt, Kanat & Çift Kol Biceps",
                targetMuscles = "Üst Sırt • Kanat • Biceps • Arka Omuz",
                estimatedMinutes = 45,
                iconType = "pull",
                focusDescription = "Dambıl çekişleriyle V-şeklinde kanat ve güçlü pazular inşa eder.",
                exercises = listOf(
                    ProfessionExerciseSpec("Tek Kol Dumbbell Row", 3, 10, 16f, 1),
                    ProfessionExerciseSpec("Çift Dumbbell Bent-Over Row", 3, 10, 14f, 2),
                    ProfessionExerciseSpec("Dumbbell Biceps Hammer Curl", 3, 12, 10f, 3),
                    ProfessionExerciseSpec("Eğilerek Arka Omuz Dumbbell Fly", 3, 12, 7f, 4),
                    ProfessionExerciseSpec("Konsantrasyon Biceps Curl", 3, 10, 10f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 3,
                dayName = "Çarşamba",
                title = "Çarşamba: Dambıl Bacak, Kalça & Çelik Core",
                targetMuscles = "Kuadriseps • Hamstring • Kalça • Karın",
                estimatedMinutes = 45,
                iconType = "legs",
                focusDescription = "Dambıl yüküyle bacakları ve kalçayı hedef alan fonksiyonel güç.",
                exercises = listOf(
                    ProfessionExerciseSpec("Dambıl Goblet Squat", 3, 12, 18f, 1),
                    ProfessionExerciseSpec("Dumbbell Romanian Deadlift (RDL)", 3, 10, 16f, 2),
                    ProfessionExerciseSpec("Dumbbell Walking Lunge", 3, 10, 12f, 3),
                    ProfessionExerciseSpec("Dumbbell Calf Raise (Baldır)", 4, 15, 14f, 4),
                    ProfessionExerciseSpec("Dumbbell Yan Karın & Plank", 3, 12, 10f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 4,
                dayName = "Perşembe",
                title = "Perşembe: Eklem Sağlığı, Mobilite & Aktif Dinlenme",
                targetMuscles = "Omurga • Eklem Kapsülleri • Esneklik",
                estimatedMinutes = 30,
                iconType = "recovery",
                focusDescription = "Ağırlık sonrası kas boyunu uzatan ve sakatlıkları önleyen toparlanma.",
                exercises = listOf(
                    ProfessionExerciseSpec("Torasik Omurga Açıcı & Kedi-Deve", 3, 10, 0f, 1),
                    ProfessionExerciseSpec("Dünyanın En İyi Esnemesi", 3, 8, 0f, 2),
                    ProfessionExerciseSpec("Kalça & Psoas Açıcı Esneme", 3, 45, 0f, 3),
                    ProfessionExerciseSpec("25 Dakika Tempolu Yürüyüş", 1, 25, 0f, 4)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 5,
                dayName = "Cuma",
                title = "Cuma: Dambıl Arnold Üst Vücut & Hipertrofi",
                targetMuscles = "Omuz • Göğüs • Sırt • Kollar",
                estimatedMinutes = 45,
                iconType = "fullbody",
                focusDescription = "Dönen açılarla omuz başlarını ve kolları dolduran estetik seans.",
                exercises = listOf(
                    ProfessionExerciseSpec("Arnold Press (Dönen Omuz Pres)", 3, 10, 12f, 1),
                    ProfessionExerciseSpec("Renegade Row (Dambıl Şınav Çekişi)", 3, 10, 12f, 2),
                    ProfessionExerciseSpec("Dambıl Incline / Düz Pres", 3, 10, 16f, 3),
                    ProfessionExerciseSpec("Dambıl Zottman Curl (Biceps & Ön Kol)", 3, 10, 10f, 4),
                    ProfessionExerciseSpec("Sandalye / Bench Dips", 3, 12, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 6,
                dayName = "Cumartesi",
                title = "Cumartesi: Dambıl Fonksiyonel Güç & Yağ Yakıcı",
                targetMuscles = "Metabolik Tüm Vücut • Kondisyon",
                estimatedMinutes = 40,
                iconType = "fullbody",
                focusDescription = "Kardiyo ile dambıl gücünü birleştiren metabolik yağ yakım canavarı.",
                exercises = listOf(
                    ProfessionExerciseSpec("Dambıl Thruster (Squat + Pres)", 3, 10, 12f, 1),
                    ProfessionExerciseSpec("Çift Dambıl Çiftçi Yürüyüşü (Farmer's Walk)", 3, 45, 18f, 2),
                    ProfessionExerciseSpec("Dambıl Swing (Salınım)", 3, 15, 14f, 3),
                    ProfessionExerciseSpec("Dumbbell Russian Twist", 3, 20, 8f, 4),
                    ProfessionExerciseSpec("Plank (Statik Güç)", 3, 60, 0f, 5)
                )
            ),
            ProfessionDayPlan(
                dayOfWeek = 7,
                dayName = "Pazar",
                title = "Pazar: Zihinsel Reset & Derin Esneme",
                targetMuscles = "Zihin • Dolaşım • Yenilenme",
                estimatedMinutes = 30,
                iconType = "recovery",
                focusDescription = "Bedenini ve iradeni haftanın zaferleri için yenileyen toparlanma.",
                exercises = listOf(
                    ProfessionExerciseSpec("Derin Çömelme & Kalça Esnetme", 1, 10, 0f, 1),
                    ProfessionExerciseSpec("Tüm Vücut Kas Gevşetme", 1, 15, 0f, 2),
                    ProfessionExerciseSpec("Gelecek Hafta Disiplin Stratejisi", 1, 10, 0f, 3)
                )
            )
        )
    }

    /**
     * Kullanıcının en baştaki tüm anket yanıtlarına (Meslek, Seviye, Ekipman/Alan, Hedef)
     * göre sıfırdan tamamen kişiselleştirilmiş 7 günlük spor programını üretir.
     */
    fun getPersonalized7DayPlan(
        profession: String?,
        fitnessLevel: String? = null,
        workoutLocation: String? = null,
        targetGoal: String? = null
    ): List<ProfessionDayPlan> {
        val cleanLoc = workoutLocation?.trim() ?: ""
        val cleanLevel = fitnessLevel?.trim() ?: "Orta Düzey"
        val cleanGoal = targetGoal?.trim() ?: "Kas Kütlesi & Hipertrofi"

        // 1. Temel Plan Havuzunu Seç (Ev / Hibrit / Salon-Meslek)
        val basePlans: List<ProfessionDayPlan> = when {
            cleanLoc.contains("Evde", ignoreCase = true) ||
            cleanLoc.contains("Vücut Ağırlığı", ignoreCase = true) ||
            cleanLoc.contains("Calisthenics", ignoreCase = true) -> {
                getHomeBodyweight7DayPlan()
            }
            cleanLoc.contains("Hibrit", ignoreCase = true) ||
            cleanLoc.contains("Dambıl", ignoreCase = true) -> {
                getHybridDumbbell7DayPlan()
            }
            else -> {
                // Varsayılan: Salonda mesleğe göre özel optimize edilmiş plan
                get7DayWorkoutPlanForProfession(profession)
            }
        }

        // 2. Seviye (Başlangıç / Orta / İleri) & Hedefe Göre Egzersizleri ve Setleri Dinamik Modifiye Et
        return basePlans.map { day ->
            val modifiedExercises = day.exercises.mapIndexed { idx, spec ->
                var targetSets = spec.targetSets
                var targetReps = spec.targetReps
                var weightKg = spec.defaultWeightKg

                // Fitness Seviyesi Etkisi
                if (cleanLevel.contains("Başlangıç", ignoreCase = true)) {
                    targetSets = (targetSets - 1).coerceAtLeast(2)
                    targetReps = if (targetReps > 15) 12 else targetReps
                    weightKg = (weightKg * 0.7f).coerceAtLeast(0f)
                } else if (cleanLevel.contains("İleri", ignoreCase = true) || cleanLevel.contains("Demir", ignoreCase = true)) {
                    targetSets = (targetSets + 1).coerceAtMost(5)
                    if (weightKg > 0f) {
                        weightKg = (weightKg * 1.25f) + 5f
                    }
                }

                // Hedef Etkisi
                if (cleanGoal.contains("Yağ Yakımı", ignoreCase = true) || cleanGoal.contains("Kilo", ignoreCase = true)) {
                    // Yüksek tekrar ve metabolik kalori yakımı
                    if (targetReps in 8..12) targetReps = 14
                } else if (cleanGoal.contains("Kuvvet", ignoreCase = true) || cleanGoal.contains("Güç", ignoreCase = true)) {
                    if (targetReps in 10..15 && weightKg > 0f) targetReps = 8
                }

                spec.copy(
                    targetSets = targetSets,
                    targetReps = targetReps,
                    defaultWeightKg = weightKg,
                    orderIndex = idx + 1
                )
            }.toMutableList()

            // Eğer hedef "Yağ Yakımı" ise gün sonuna metabolik yakıcı ekle (İstirahat günleri hariç)
            if ((cleanGoal.contains("Yağ Yakımı", ignoreCase = true) || cleanGoal.contains("Definasyon", ignoreCase = true)) && day.iconType != "recovery") {
                modifiedExercises.add(
                    ProfessionExerciseSpec(
                        name = "🔥 3 Dk Metabolik Yağ Yakıcı (Jumping Jack + Dağ Tırmanışı)",
                        targetSets = 3,
                        targetReps = 30,
                        defaultWeightKg = 0f,
                        orderIndex = modifiedExercises.size + 1
                    )
                )
            } else if (cleanGoal.contains("Postür", ignoreCase = true) && day.iconType != "recovery") {
                modifiedExercises.add(
                    ProfessionExerciseSpec(
                        name = "🧘 Torasik Omurga Açıcı & Y-Raise (Kamburluk Savar)",
                        targetSets = 3,
                        targetReps = 12,
                        defaultWeightKg = 0f,
                        orderIndex = modifiedExercises.size + 1
                    )
                )
            }

            day.copy(exercises = modifiedExercises)
        }
    }
}

