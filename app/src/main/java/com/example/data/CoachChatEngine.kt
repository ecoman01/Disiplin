package com.example.data

import java.util.UUID

object CoachChatEngine {

    val categories = listOf(
        "🔥 Bahaneleri Yık",
        "⚡ Sert Gerçekler",
        "🍫 Beslenme & Kaçamak",
        "💪 İdman Öncesi",
        "🧘 Zihin & Stres"
    )

    fun getRootPromptOptions(): List<CoachPromptOption> {
        return listOf(
            CoachPromptOption(
                id = "opt_fatigue",
                label = "Aşırı yorgunum, bugün pas geçsem ne olur?",
                category = "🔥 Bahaneleri Yık",
                icon = "🥱",
                payloadKey = "FATIGUE"
            ),
            CoachPromptOption(
                id = "opt_hard_truth",
                label = "Bana sert gerçekleri yüzüme vur!",
                category = "⚡ Sert Gerçekler",
                icon = "⚔️",
                payloadKey = "HARD_TRUTH"
            ),
            CoachPromptOption(
                id = "opt_cravings",
                label = "Canım fena halde şeker ve tatlı çekiyor!",
                category = "🍫 Beslenme & Kaçamak",
                icon = "🍰",
                payloadKey = "CRAVINGS"
            ),
            CoachPromptOption(
                id = "opt_phone_trap",
                label = "Telefona daldım, 2 saattir kalkamadım...",
                category = "🔥 Bahaneleri Yık",
                icon = "📱",
                payloadKey = "PHONE_TRAP"
            ),
            CoachPromptOption(
                id = "opt_preworkout",
                label = "İdman öncesi beni havaya sok (Pre-Workout)!",
                category = "💪 İdman Öncesi",
                icon = "🔥",
                payloadKey = "PREWORKOUT"
            ),
            CoachPromptOption(
                id = "opt_no_time",
                label = "Vaktim hiç yok, gün bitti yetişemiyorum",
                category = "🔥 Bahaneleri Yık",
                icon = "⏳",
                payloadKey = "NO_TIME"
            ),
            CoachPromptOption(
                id = "opt_cheat_regret",
                label = "Dün diyeti bozdum, pişman ve moralsizim",
                category = "🍫 Beslenme & Kaçamak",
                icon = "💔",
                payloadKey = "CHEAT_REGRET"
            ),
            CoachPromptOption(
                id = "opt_mind_full",
                label = "Zihnim çok dolu ve stresliyim, dağıldım",
                category = "🧘 Zihin & Stres",
                icon = "🧠",
                payloadKey = "MIND_FULL"
            )
        )
    }

    fun getOptionsForCategory(category: String): List<CoachPromptOption> {
        return when (category) {
            "🔥 Bahaneleri Yık" -> listOf(
                CoachPromptOption("opt_fatigue", "Aşırı yorgunum, bugün pas geçsem ne olur?", category, "🥱", "FATIGUE"),
                CoachPromptOption("opt_no_time", "Vaktim hiç yok, gün bitti yetişemiyorum", category, "⏳", "NO_TIME"),
                CoachPromptOption("opt_phone_trap", "Telefona daldım, 2 saattir kalkamadım...", category, "📱", "PHONE_TRAP"),
                CoachPromptOption("opt_broken_streak", "1 gün kaçırdım, hevesim kalmadı", category, "🌪️", "BROKEN_STREAK"),
                CoachPromptOption("opt_bad_weather", "Hava çok kötü / modum sıfır", category, "🌧️", "BAD_WEATHER")
            )
            "⚡ Sert Gerçekler" -> listOf(
                CoachPromptOption("opt_hard_truth", "Bana sert gerçekleri yüzüme vur!", category, "⚔️", "HARD_TRUTH"),
                CoachPromptOption("opt_remind_why", "Neden başladığımı ve vizyonumu hatırlat", category, "🎯", "REMIND_WHY"),
                CoachPromptOption("opt_fear_old_self", "Eski tembel halime dönmekten korkuyorum", category, "💀", "FEAR_OLD_SELF"),
                CoachPromptOption("opt_stop_procrastinating", "Sürekli erteliyorum, bu zinciri nasıl kırarım?", category, "🛑", "STOP_PROCRASTINATING")
            )
            "🍫 Beslenme & Kaçamak" -> listOf(
                CoachPromptOption("opt_cravings", "Canım fena halde şeker ve tatlı çekiyor!", category, "🍰", "CRAVINGS"),
                CoachPromptOption("opt_cheat_regret", "Dün diyeti bozdum, pişman ve moralsizim", category, "💔", "CHEAT_REGRET"),
                CoachPromptOption("opt_social_eating", "Dışarıdayım, herkes fast food yiyor ne yapayım?", category, "🍕", "SOCIAL_EATING"),
                CoachPromptOption("opt_night_snack", "Gece buzdolabının önündeyim, kendimi tutamıyorum", category, "🐺", "NIGHT_SNACK")
            )
            "💪 İdman Öncesi" -> listOf(
                CoachPromptOption("opt_preworkout", "İdman öncesi beni havaya sok (Pre-Workout)!", category, "🔥", "PREWORKOUT"),
                CoachPromptOption("opt_home_quick", "Salona gidemeyeceğim, evde hemen ne yapayım?", category, "🏠", "HOME_QUICK"),
                CoachPromptOption("opt_heavy_fear", "Bugün ağırlıklar gözümde büyüyor", category, "🏋️‍♂️", "HEAVY_FEAR"),
                CoachPromptOption("opt_start_5sec", "5 saniye kuralı başlat, hemen kalkıyorum!", category, "⏱️", "START_5SEC")
            )
            "🧘 Zihin & Stres" -> listOf(
                CoachPromptOption("opt_mind_full", "Zihnim çok dolu ve stresliyim, dağıldım", category, "🧠", "MIND_FULL"),
                CoachPromptOption("opt_posture_pain", "Masa başında belim ve boynum tutuldu", category, "💼", "POSTURE_PAIN"),
                CoachPromptOption("opt_bad_sleep", "Dün gece az uyudum, yine de spor yapmalı mıyım?", category, "💤", "BAD_SLEEP"),
                CoachPromptOption("opt_celebrate_win", "Bugünkü görevimi başarıyla tamamladım koç!", category, "🏆", "CELEBRATE_WIN")
            )
            else -> getRootPromptOptions()
        }
    }

    fun getDefaultWelcomeMessage(persona: CoachPersona, userProfile: UserProfile?): CoachChatMessage {
        val name = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        val profession = userProfile?.profession ?: "Masa Başı"
        val obstacle = userProfile?.biggestProblem ?: "Üşenmek"

        val introText = when (persona) {
            CoachPersona.DEMIR_KOC -> """
                Selam $name! Karşında bahane kabul etmeyen Demir Koç var.
                
                Mesleğin ($profession) veya günün yorgunluğu beni ilgilendirmez. Buraya konfor alanında uyumaya değil, demirden bir irade inşa etmeye geldin. Aşmak istediğin engel: "$obstacle".
                
                Aşağıdaki konulardan birini seç ya da hissettiğin engeli bana söyle, hemen stratejiyi belirleyelim!
            """.trimIndent()

            CoachPersona.STOIK_MARCUS -> """
                Hoş geldin $name. Ben Marcus, stoik irade rehberin.
                
                Günün karmaşası, mesleki stres veya zihnindeki iç sesler seni yanıltmasın. Dış dünyayı kontrol edemezsin, ancak şu an vereceğin tepkiler ve bedenine hükmetmen tamamen senin elinde.
                
                Zihnini hangi düşünce meşgul ediyor? Konuyu seç, akılla ve sakinlikle çözelim.
            """.trimIndent()

            CoachPersona.MAMBA_KOBE -> """
                Naber $name! Mamba Zihniyeti odasına hoş geldin.
                
                Sıradan insanlar yorulunca bırakır. Şampiyonlar ise iş bittiğinde bırakır! Sen ortalama olmak için doğmadın.
                
                Şu an seni zirveye çıkmaktan alıkoyan bahane nedir? Seçeneğini belirt, tempoyu iki katına çıkaralım!
            """.trimIndent()

            CoachPersona.BILGE_USTA -> """
                Huzurla selamlarım $name.
                
                Zihin sakinleştiğinde, en ağır yük bile hafifler. Bin millik bir yolculuk tek bir kararlı adımla başlar.
                
                Bugün aşman gereken tepe nedir? Seçimini yap, sade eylemin gücüyle aşalım.
            """.trimIndent()
        }

        return CoachChatMessage(
            id = "welcome_${System.currentTimeMillis()}",
            sender = MessageSender.COACH,
            text = introText,
            persona = persona,
            followUpOptions = getRootPromptOptions().take(4),
            keyPunchline = persona.quote
        )
    }

    fun generateResponse(
        option: CoachPromptOption,
        persona: CoachPersona,
        userProfile: UserProfile?
    ): CoachChatMessage {
        val name = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        val location = userProfile?.workoutLocation ?: "Spor Salonu"

        return when (option.payloadKey) {
            "FATIGUE" -> {
                val punchline = when (persona) {
                    CoachPersona.DEMIR_KOC -> "Yorgunluk beyninin seni korumaya çalışan bir yalanıdır. Kapasitenin henüz %40'ındasın!"
                    CoachPersona.STOIK_MARCUS -> "Beden yorgun olabilir ama zihin hâlâ hükmetme gücüne sahiptir."
                    CoachPersona.MAMBA_KOBE -> "Rakiplerin de yorgun. Fark, tam da yorgunken kimin sahaya çıktığıyla belirlenir."
                    CoachPersona.BILGE_USTA -> "Eylemsizlik yorgunluğu büyütür; doğru hareket bedene tazelik getirir."
                }

                val body = when (persona) {
                    CoachPersona.DEMIR_KOC -> """
                        Dinle beni $name!
                        "Aşırı yorgunum" cümlesi konfor canavarının en sevdiği maskedir. Bugün pas geçersen yarın da aynı yalanı satacak sana!
                        
                        Senden dünya rekoru kırmanı istemiyorum. Sadece spor kıyafetlerini giy ve ilk 10 dakikayı yap. Eğer 10 dakika sonra hâlâ ölüyorsan bırakırsın. Ama garanti veriyorum: Kanın ısındığında canavar uyanacak!
                    """.trimIndent()

                    CoachPersona.STOIK_MARCUS -> """
                        $name, hissettiğin yorgunluk fizyolojik mi yoksa günün zihinsel yıpranmışlığı mı?
                        
                        Çoğu zaman günün gürültüsü zihnimizi uyuşturur ve bunu bedensel tükenmişlik sanırız. Bugün idmanı ertelemek sana güç değil, yarın için pişmanlık getirecektir. İradeni koru, hafif tempoda bile olsa disiplin zincirini kırma.
                    """.trimIndent()

                    CoachPersona.MAMBA_KOBE -> """
                        Bunu herkes yaşar $name.
                        Fakat büyüklerle sıradanları ayıran çizgi tam burasıdır: Canın istemezken, gözlerin kapanırken bile spor çantana uzanmak. Salona adım attığın an o yorgunluk yerini zafere bırakacak. Kalk ve ayakkabılarını bağla!
                    """.trimIndent()

                    CoachPersona.BILGE_USTA -> """
                        Durgun su yosun tutar, akan su berraklaşır $name.
                        Yorgunluğu oturarak dinlendiremezsin. Derin bir nefes al, 1 bardak su iç ve ilk seti tamamla. Beden hareket ettikçe enerjisini bulacaktır.
                    """.trimIndent()
                }

                val followUps = listOf(
                    CoachPromptOption("fu_accept_fatigue", "Haklısın koç, 5 saniye kuralıyla hemen kalkıyorum!", "Seçenek", "⚡", "TRIGGER_5SEC"),
                    CoachPromptOption("fu_light_session", "Peki ya çok zorlanırsam seansı hafifleteyim mi?", "Seçenek", "💡", "ADVICE_LIGHT_SESSION"),
                    CoachPromptOption("fu_other", "Başka bir bahane / konu seçelim", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    actionType = CoachActionType.START_5_SEC_RULE,
                    keyPunchline = punchline
                )
            }

            "HARD_TRUTH" -> {
                val body = when (persona) {
                    CoachPersona.DEMIR_KOC -> """
                        İşte duymak istediğin acımasız gerçek $name:
                        
                        Kimse seni kurtarmaya gelmeyecek! Hiçbir motivasyon videosu, hiçbir mucize takviye senin yerine ağırlığı kaldırmayacak.
                        Eğer bugün de ertelemeye devam edersen, 1 yıl sonra bugünkü haline bakıp "keşke o gün başlasaydım" diyeceksin. Eski halin seni geriye çekmek için her gün pusuda bekliyor. Ya acıyı şimdi çekeceksin ya da pişmanlığın zehrini ömür boyu yudumlayacaksın!
                    """.trimIndent()

                    CoachPersona.STOIK_MARCUS -> """
                        Sana gerçeği söyleyeyim $name:
                        Hayat çok kısa ve hızla tükeniyor. Kendine verdiğin sözleri tutmamak, kendi benliğine saygısızlıktır. Dışarıdaki şartlar hiçbir zaman mükemmel olmayacak; mükemmeli yaratacak olan senin kararlı duruşundur.
                    """.trimIndent()

                    CoachPersona.MAMBA_KOBE -> """
                        Gerçek şu: İkinci olanı kimse hatırlamaz.
                        Bugün yapmadığın her tekrar, rakibine verdiğin bir avanstır. Potansiyelini çöpe atmak kendine yapabileceğin en büyük haksızlıktır. Standardını hemen yükselt!
                    """.trimIndent()

                    CoachPersona.BILGE_USTA -> """
                        Gerçek yalındır $name:
                        Zaman ne durur ne geri döner. Bugün ertelediğin şey, yarın taşıması iki kat ağır olan bir yüke dönüşür. Şimdi eyleme geç.
                    """.trimIndent()
                }

                val followUps = listOf(
                    CoachPromptOption("fu_ready_action", "Bu tokat yetti koç! İdmana başlıyorum!", "Seçenek", "🔥", "START_WORKOUT_NOW"),
                    CoachPromptOption("fu_why_restart", "Neden başladığımı da hatırlat", "Seçenek", "🎯", "REMIND_WHY"),
                    CoachPromptOption("fu_other", "Başka bir konu seçelim", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    actionType = CoachActionType.NAVIGATE_WORKOUT,
                    keyPunchline = "Pişmanlığın ağırlığı tonlarcadır, disiplinin ağırlığı ise sadece gramlardır."
                )
            }

            "CRAVINGS" -> {
                val body = """
                    O tatlı isteği $name, beyninin ucuz dopamin tuzağıdır!
                    
                    Vücudunun gerçekte şekere değil; enerjiye, suya ve zihinsel sakinliğe ihtiyacı var. O şekeri yediğinde alacağın keyif sadece 3 dakika sürecek; arkasından gelen pişmanlık, kan şekeri çöküşü ve suçluluk hissi saatlerce peşini bırakmayacak.
                    
                    👉 ŞİMDİ YAPACAKLARIN:
                    1. Hemen 1 büyük bardak soğuk su iç (içine birkaç damla limon sık).
                    2. Mutfağı terk et ve 10 derin nefes al.
                    3. Eğer tatlı krizin devam ederse fit tarifler bölümündeki şekersiz kakaolu yulaf toplarından yap!
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_drank_water", "Suyu içtim, mutfaktan uzaklaştım!", "Seçenek", "💧", "WATER_CONFIRMED"),
                    CoachPromptOption("fu_fit_recipes", "Fit şekersiz tarifleri göster", "Seçenek", "🥗", "OPEN_RECIPES"),
                    CoachPromptOption("fu_other", "Farklı bir konu seç", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    keyPunchline = "3 dakikalık dil zevki için haftalarca döktüğün teri heba etme!"
                )
            }

            "PHONE_TRAP" -> {
                val body = """
                    $name, başkalarının hayatını izlerken kendi hayatının kontrolünü kaybediyorsun!
                    
                    O parmağınla sonsuz kaydırdığın ekran seni uyuşturuyor, irade bataryanı tüketiyor. 2 saattir beynine çöp bilgi boca ettin ve şimdi kendine yorgunum diyorsun. Yorgun değilsin; dopamin zehirlenmesi yaşıyorsun!
                    
                    🚨 ŞU AN PROTOKOLÜ:
                    Telefonu ters çevir, ekranı kilitle ve hemen yere uzanıp 15 şınav çek. Beynindeki kan dolaşımını ekrandan kaslarına yönlendir!
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_pushups_done", "15 şınavı çektim koç, telefon kapalı!", "Seçenek", "💪", "PUSHUPS_DONE"),
                    CoachPromptOption("fu_5sec_now", "Bana 5 saniye kuralı başlat hemen kalkayım", "Seçenek", "⏱️", "TRIGGER_5SEC"),
                    CoachPromptOption("fu_other", "Başka bir konu", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    actionType = CoachActionType.START_5_SEC_RULE,
                    keyPunchline = "Başkalarının başarılarını alkışlamayı bırak, kendi hikayeni inşa et!"
                )
            }

            "PREWORKOUT" -> {
                val body = """
                    İŞTE BU AN SENİN SAHNE ALDIĞIN AN $name! 🔥
                    
                    Kulaklığını tak, sevdiğin sert parçayı aç. Bugün o salona veya o odaya ($location) sıradan biri olarak girmeyeceksin. Her bir sette eski zayıf halinin sınırlarını paramparça edeceksin!
                    
                    Aynaya bak: O gözlerde bahane arayan biri mi var, yoksa hedefine kilitlenmiş bir savaşçı mı? 
                    Derin bir nefes al, omuzlarını dikleştir ve ilk sete odaklan. Bu antrenman senin irade anıtın olacak!
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_start_plan", "İdmanı Başlat! Ağırlıkların başına geçiyorum!", "Seçenek", "🏋️‍♂️", "START_WORKOUT_NOW"),
                    CoachPromptOption("fu_breathing", "Odak nefesi alıp nabzı dengeleyelim", "Seçenek", "🧘", "START_BREATHING"),
                    CoachPromptOption("fu_other", "Farklı bir konu", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    actionType = CoachActionType.NAVIGATE_WORKOUT,
                    keyPunchline = "Ağırlıklar sana acımaz; sen ağırlıklara hükmet!"
                )
            }

            "NO_TIME" -> {
                val body = """
                    $name, "Zamanım yok" demek "Bu benim için öncelikli değil" demektir!
                    
                    Bir günde tam 1440 dakika var. Senden 2 saat istemiyorum. Günde sadece 25-30 dakikalık yoğun bir seans bile hayatını baştan aşağı değiştirmeye yeter!
                    Telefonundaki ekran sürene baksan en az 1 saat boş kaydırma bulursun. 
                    
                    Zaman bulunmaz $name, zaman yaratılır! Eğer tam idmana vaktin yoksa, hemen şimdi 15 dakikalık yüksek tempolu hızlı seansı başlat. 0'dan her zaman büyüktür!
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_quick_workout", "Haklısın, 15 dakikalık hızlı idman yapacağım!", "Seçenek", "⚡", "START_WORKOUT_NOW"),
                    CoachPromptOption("fu_5sec_now", "5 saniye say kalkayım", "Seçenek", "⏱️", "TRIGGER_5SEC"),
                    CoachPromptOption("fu_other", "Başka bir konu", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    keyPunchline = "Zamanım yok deme, önceliğim değildi de ve kendine dürüst ol."
                )
            }

            "CHEAT_REGRET" -> {
                val body = """
                    Dinle beni $name: Dün bitti! Dünü değiştiremezsin ama dünün bugününü mahvetmesine izin vermek senin hatan olur.
                    
                    Tek bir kötü öğün seni şişmanlatmaz, tıpkı tek bir salata seni fit yapmadığı gibi. Önemli olan "Zaten bozdum" deyip yangına körükle gitmemek!
                    Suçluluk duygusunu çöpe at. O kaçamak artık geride kaldı. Şimdi 1 büyük bardak su iç, bugün porsiyonlarını tertemiz tut ve akşamki idmanda fazladan aldığın o enerjiyi demirlere dök!
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_clean_today", "Anladım koç, bugün tertemiz devam!", "Seçenek", "🥗", "CLEAN_TODAY_CONFIRMED"),
                    CoachPromptOption("fu_burn_calories", "Akşam idmanda o enerjiyi yakacağım!", "Seçenek", "🔥", "START_WORKOUT_NOW"),
                    CoachPromptOption("fu_other", "Başka bir konu", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    keyPunchline = "Araba sürerken dikiz aynasına değil, ön camına bakılır. Önüne bak!"
                )
            }

            "MIND_FULL" -> {
                val body = """
                    Zihnin bir fırtına gibi estiğinde $name, düşünerek sakinleşemezsin; eylemle sakinleşirsin.
                    
                    Fiziksel hareket zihinsel düğümleri çözer. Ağırlık kaldırdığında veya terlediğinde beynin gereksiz gevezelikleri susturmak zorunda kalır. Çünkü o an hayatta kalmaya ve kası kasmaya odaklanır.
                    
                    Hemen 60 saniyelik stoik odak nefesini yapalım veya 10 dakikalık yürüyüşe çık. Zihnini rahatlatmanın yolu bedenini harekete geçirmektir.
                """.trimIndent()

                val followUps = listOf(
                    CoachPromptOption("fu_breathing", "60 saniyelik odak nefesini başlat", "Seçenek", "🧘", "START_BREATHING"),
                    CoachPromptOption("fu_workout", "Ağırlık kaldırıp kafamı boşaltacağım!", "Seçenek", "🏋️‍♂️", "START_WORKOUT_NOW"),
                    CoachPromptOption("fu_other", "Başka bir konu", "Seçenek", "🔄", "SHOW_ROOT")
                )

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = followUps,
                    actionType = CoachActionType.BREATHING_EXERCISE,
                    keyPunchline = "Zihnini susturmak istiyorsan, bedenini yor."
                )
            }

            "REMIND_WHY" -> {
                val problem = userProfile?.biggestProblem ?: "erteleme"
                val goal = userProfile?.targetGoal ?: "Kas Kütlesi ve İrade"
                val body = """
                    Hatırla $name!
                    Bu yola başlarken kendine bir söz vermiştin. Hedefin: "$goal".
                    Aşmak istediğin en büyük zayıflık: "$problem".
                    
                    Aynaya baktığında gurur duyacağın, saygı duyulacak bir beden ve sarsılmaz bir irade istedin. Sıradanlığa razı olmak istemedin. Şimdi birkaç anlık zorluk yüzünden o kutsal sözü çöpe mi atacaksın? 
                    Hayır! Başladığın işi bitir!
                """.trimIndent()

                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = body,
                    persona = persona,
                    followUpOptions = listOf(
                        CoachPromptOption("fu_start_plan", "Sözümü tutuyorum koç, idmana başlıyorum!", "Seçenek", "⚔️", "START_WORKOUT_NOW"),
                        CoachPromptOption("fu_other", "Farklı bir konu seç", "Seçenek", "🔄", "SHOW_ROOT")
                    ),
                    keyPunchline = "Başladığın işi bitirmeden asla arkana bakma!"
                )
            }

            "TRIGGER_5SEC" -> {
                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = "5 saniye kuralı devrede! Gözlerini kapat: 5 - 4 - 3 - 2 - 1... BEYNİN DÜŞÜNMEDEN HEMEN AYAĞA KALK VE BAŞLA!",
                    persona = persona,
                    followUpOptions = listOf(
                        CoachPromptOption("fu_start_plan", "Kalktım ve idmanı açtım!", "Seçenek", "🔥", "START_WORKOUT_NOW"),
                        CoachPromptOption("fu_other", "Başka bir soru sor", "Seçenek", "🔄", "SHOW_ROOT")
                    ),
                    actionType = CoachActionType.START_5_SEC_RULE,
                    keyPunchline = "Düşünürsen yenilirsin; say ve eyleme geç!"
                )
            }

            "START_WORKOUT_NOW" -> {
                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = "Muhteşem karar $name! Seni tutan hiçbir şey yok. Bugün o salonda veya evinde sonuna kadar savaş. Seans bittiğinde hissettiğin gurur paha biçilemez olacak!",
                    persona = persona,
                    followUpOptions = listOf(
                        CoachPromptOption("fu_done_session", "İdmanı bitirdim koç, zafer bizim!", "Seçenek", "🏆", "CELEBRATE_WIN"),
                        CoachPromptOption("fu_other", "Diğer konular", "Seçenek", "🔄", "SHOW_ROOT")
                    ),
                    actionType = CoachActionType.NAVIGATE_WORKOUT,
                    keyPunchline = "Zafer ter dökenlerindir!"
                )
            }

            "CELEBRATE_WIN" -> {
                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = "Tebrikler $name! İşte gerçek bir savaşçı ruhu bu! Bugün kendine verdiğin sözü tuttun ve eski haline bir darbe daha indirdin. Dinlen, iyi beslen ve yarınki zafer için hazır ol!",
                    persona = persona,
                    followUpOptions = listOf(
                        CoachPromptOption("fu_share_win", "Bu zaferi arkadaşlarımla paylaş", "Seçenek", "📢", "SHARE_WIN"),
                        CoachPromptOption("fu_other", "Yeni bir sohbet başlat", "Seçenek", "🔄", "SHOW_ROOT")
                    ),
                    actionType = CoachActionType.SHARE_ADVICE,
                    keyPunchline = "Her tamamlanan gün, gelecekteki efsanenin bir tuğlasıdır."
                )
            }

            else -> {
                // Generic intelligent motivation reply
                CoachChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.COACH,
                    text = """
                        $name, bahsettiğin durumu çok iyi anlıyorum. Hayatta her gün harika hissetmeyeceksin. Ama disiplin zaten hissettiğinden bağımsız olarak doğru olanı yapabilme sanatıdır.
                        
                        Şimdi zihnindeki karmaşayı sustur ve sadece sıradaki en küçük doğru adımı at!
                    """.trimIndent(),
                    persona = persona,
                    followUpOptions = getRootPromptOptions().take(3),
                    keyPunchline = persona.quote
                )
            }
        }
    }

    fun generateCustomResponse(
        userText: String,
        persona: CoachPersona,
        userProfile: UserProfile?
    ): CoachChatMessage {
        val name = userProfile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"
        val lower = userText.lowercase()

        val matchingPayload = when {
            lower.contains("yorgun") || lower.contains("uyku") || lower.contains("bitkin") -> "FATIGUE"
            lower.contains("vakit") || lower.contains("zaman") || lower.contains("yoğun") -> "NO_TIME"
            lower.contains("şeker") || lower.contains("tatlı") || lower.contains("yemek") || lower.contains("diyet") || lower.contains("aç") -> "CRAVINGS"
            lower.contains("telefon") || lower.contains("sosyal") || lower.contains("video") || lower.contains("instagram") -> "PHONE_TRAP"
            lower.contains("idman") || lower.contains("spor") || lower.contains("başla") || lower.contains("enerji") -> "PREWORKOUT"
            lower.contains("stres") || lower.contains("kafa") || lower.contains("zihin") || lower.contains("bunal") -> "MIND_FULL"
            lower.contains("boz") || lower.contains("pişman") || lower.contains("kaçır") -> "CHEAT_REGRET"
            lower.contains("neden") || lower.contains("amaç") || lower.contains("hedef") -> "REMIND_WHY"
            else -> "HARD_TRUTH"
        }

        val dummyOption = CoachPromptOption(
            id = "custom_match",
            label = userText,
            category = "Özel Soru",
            icon = "💬",
            payloadKey = matchingPayload
        )

        return generateResponse(dummyOption, persona, userProfile)
    }
}
