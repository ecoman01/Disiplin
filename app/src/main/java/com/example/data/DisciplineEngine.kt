package com.example.data

import java.util.Calendar

data class BehavioralInsight(
    val type: InsightType,
    val title: String,
    val description: String,
    val badge: String
)

data class MotivationQuote(
    val id: String,
    val quote: String,
    val author: String,
    val context: String, // "Stoik Zihniyet", "Demir İrade", "Bahaneleri Sustur", etc.
    val takeaway: String,
    val category: String // "Tümü", "Stoik", "İrade", "Bahaneleri Sustur", "Odak"
)

data class DisciplineHabit(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val category: String, // "Zihin", "İrade", "Beden", "Odak"
    val isCompleted: Boolean = false
)

enum class InsightType {
    WARNING,
    DISCOVERY,
    STRENGTH
}

object DisciplineEngine {

    val motivationQuotes = listOf(
        MotivationQuote(
            id = "q1",
            quote = "Sabah yataktan çıkmak istemediğinde şunu hatırla: Bir insanın görevini yerine getirmek için uyanıyorum.",
            author = "Marcus Aurelius",
            context = "Stoik Zihniyet • Kendime Düşünceler",
            takeaway = "Yatakta kalıp sıcakta saklanmak kolaydır; fakat sen konfor için değil, üretmek ve gelişmek için varsın.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q2",
            quote = "Zihnin pes et dediğinde, gerçek kapasitenin sadece %40'ını kullanmışsındır. Kalan %60 iradendir.",
            author = "David Goggins",
            context = "%40 Kuralı • Can't Hurt Me",
            takeaway = "Yorgunluk ve üşengeçlik bir hissten ibarettir, sınır değildir. Bedenini değil, beynini yönet.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q3",
            quote = "Zor olduğu için cesaret edemiyor değiliz; cesaret edemediğimiz için zordur.",
            author = "Seneca",
            context = "Harekete Geçme Felsefesi",
            takeaway = "Düşüncelerini fazla büyütme. Hemen ilk adımı at, zorluk adımla birlikte küçülür.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q4",
            quote = "Ne kadar süre daha kendin için en iyisini istemeyi erteleyeceksin?",
            author = "Epictetus",
            context = "Zamanın Değeri",
            takeaway = "Yarın diye bir gün yok. Dönüşmek istediğin kişi bugün aldığın kararlarla şekillenir.",
            category = "Bahaneleri Sustur"
        ),
        MotivationQuote(
            id = "q5",
            quote = "Disiplin eşittir özgürlük. Rahatlık anlık bir tatmin, disiplin ise ömürlük bir güçtür.",
            author = "Jocko Willink",
            context = "Savaşçı Disiplini",
            takeaway = "Canının istemesini bekleme. Duygularına değil, hedeflerine ve sistemine itaat et.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q6",
            quote = "Herkes zirveyi ister. Çok az insan sabah 4'te kalkıp karanlıkta ter dökmeyi arzular.",
            author = "Kobe Bryant",
            context = "Mamba Zihniyeti",
            takeaway = "Sessizlikte gösterdiğin çaba, ileride herkesin göreceği zaferleri doğurur.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q7",
            quote = "Kolay bir hayat dilemeyin; zorluklara dayanabilecek sarsılmaz bir güç dileyin.",
            author = "Bruce Lee",
            context = "Zihinsel Dayanıklılık",
            takeaway = "Engeller seni durdurmak için çıkmaz, içindeki savaşçıyı uyandırmak için vardır.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q8",
            quote = "Beni öldürmeyen şey, beni daha güçlü kılar.",
            author = "Friedrich Nietzsche",
            context = "Büyüme Zihniyeti",
            takeaway = "Zorlandığın anlarda geri çekilme. Dirençle karşılaştığın her an ruhunu dövüyorsun.",
            category = "Bahaneleri Sustur"
        ),
        MotivationQuote(
            id = "q9",
            quote = "Koşulları değiştiremiyorsak, kendimizi dönüştürmekle yükümlüyüzdür.",
            author = "Viktor Frankl",
            context = "İnsanın Anlam Arayışı",
            takeaway = "Dış şartları kontrol edemezsin fakat kendi tepkini ve azmini daima sen seçersin.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q10",
            quote = "Bugün düne karşı zafer kazan; yarın ise daha az azimli olanlara karşı kazanacaksın.",
            author = "Miyamoto Musashi",
            context = "Beş Çember Kitabı",
            takeaway = "Tek rakibin dün olduğun zayıf kişidir. Her gün ona karşı bir galibiyet al.",
            category = "Odak"
        ),
        MotivationQuote(
            id = "q11",
            quote = "İki türlü acı vardır: Disiplinin acısı ve pişmanlığın acısı. Disiplin gramlarla tartar, pişmanlık tonlarla ezer.",
            author = "Jim Rohn",
            context = "Bedel ve Ödül",
            takeaway = "Bugün zorlanmayı seçmezsen, yarın keşkelerin ağırlığı altında ezilirsin.",
            category = "Bahaneleri Sustur"
        ),
        MotivationQuote(
            id = "q12",
            quote = "Büyük işler bir anda yapılmaz; küçük şeylerin bir araya gelmesiyle inşa edilir.",
            author = "Vincent van Gogh",
            context = "Süreklilik Kanunu",
            takeaway = "Büyük sıçramalar arama. Bugün küçük bir görevi eksiksiz tamamla, momentum yarat.",
            category = "Odak"
        ),
        MotivationQuote(
            id = "q13",
            quote = "Yolun üzerindeki engel, yolun ta kendisi haline gelir. Eyleme mani olan şey, eylemi ileri taşır.",
            author = "Marcus Aurelius",
            context = "Engeli Fırsata Çevirmek",
            takeaway = "Önüne çıkan hiçbir zorluk rastlantı değildir. Her engel karakterini bilemek için bir fırsattır.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q14",
            quote = "Eğer bir insan hangi limana yelken açtığını bilmiyorsa, hiçbir rüzgar ona yardım edemez.",
            author = "Seneca",
            context = "Net Amaç & Kararlılık",
            takeaway = "Rotasız enerji tükenir. Hedefini netleştir, bahaneleri arkanda bırak ve dümeni sıkı tut.",
            category = "Odak"
        ),
        MotivationQuote(
            id = "q15",
            quote = "Motivasyon seni başlatır, disiplin seni yolda tutar, alışkanlıklar ise seni dönüştürür.",
            author = "Jim Ryun",
            context = "Sistemik Zihniyet",
            takeaway = "Geçici heveslerin heyecanına bel bağlama. Seni zirveye taşıyacak olan günlük demir disiplindir.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q16",
            quote = "Bedenine hükmetmeyen bir insan, hiçbir zaman gerçekten özgür olamaz.",
            author = "Pythagoras",
            context = "Beden & İrade Birliği",
            takeaway = "Canının çektiği her abur cubura veya tembellik isteğine boyun eğiyorsan esirsindir. Kontrolü eline al.",
            category = "Bahaneleri Sustur"
        ),
        MotivationQuote(
            id = "q17",
            quote = "Yetenek doğuştan gelebilir, ancak şampiyonluklar kimsenin izlemediği anlardaki ter damlalarıyla kazanılır.",
            author = "Michael Jordan",
            context = "Karanlıktaki Emek",
            takeaway = "Göz önünde olmayan fedakarlıkların, sahnedeki zaferlerin temel taşıdır.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q18",
            quote = "Bugün katlandığın zorluklar, yarın hissedeceğin sarsılmaz özgüvenin tohumlarıdır.",
            author = "Stoik Aforizma",
            context = "Direnç & Dönüşüm",
            takeaway = "Acı geçicidir; fakat vazgeçmenin bırakacağı tortu bir ömür sürer. İlerlemeye devam et.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q19",
            quote = "Bahanelerin senin en büyük düşmanındır. Canın istemese de kalkacaksın ve o demiri kaldıracaksın.",
            author = "David Goggins",
            context = "Acımasız Gerçekler • Can't Hurt Me",
            takeaway = "Duyguların sana yalan söyler. 'Yarın yaparım' diyen zihnini sustur ve bedeni zorla.",
            category = "Sert Gerçekler"
        ),
        MotivationQuote(
            id = "q20",
            quote = "Kimse seni kurtarmaya gelmeyecek. Şikayet etmeyi bırak, ayağa kalk ve işini bitir.",
            author = "Jocko Willink",
            context = "Aşırı Sorumluluk • Extreme Ownership",
            takeaway = "Koşulları suçlamayı bırak. Hayatındaki her sonucun tek sorumlusu aynadaki kişidir.",
            category = "Sert Gerçekler"
        ),
        MotivationQuote(
            id = "q21",
            quote = "Yorgun olduğunda durma; iş bittiğinde dur.",
            author = "David Goggins",
            context = "Sınırsız İrade",
            takeaway = "Yorgunluk sadece bir histir. Beynin sana acıdığı için durmanı ister, ona boyun eğme.",
            category = "Sert Gerçekler"
        ),
        MotivationQuote(
            id = "q22",
            quote = "Konfor alanı bir mezarlıktır. Seni zayıflatan, çürüten ve potansiyelini öldüren şey rahatlıktır.",
            author = "Demir İrade İlkesi",
            context = "Konfor Tuzağını Kırmak",
            takeaway = "Zor olanı seçmediğin her an, gelecekteki güçlü benliğinden çalıyorsun.",
            category = "Sert Gerçekler"
        ),
        MotivationQuote(
            id = "q23",
            quote = "Ben dinlenmeyi sadece işim tamamen bittiğinde düşünürüm; ortasında asla.",
            author = "Kobe Bryant",
            context = "Mamba Zihniyeti",
            takeaway = "Yarıda bırakmak alışkanlık yapar. Başladığın işi terinin son damlasına kadar tamamla.",
            category = "Mamba"
        ),
        MotivationQuote(
            id = "q24",
            quote = "Eğer pes etmezsen, başkalarının yetenek dediği şeyi çalışarak ezersin.",
            author = "Cristiano Ronaldo",
            context = "Çalışma Disiplini",
            takeaway = "Yetenek kapıyı açabilir; fakat sadece aralıksız çalışanlar o odada kalabilir.",
            category = "Mamba"
        ),
        MotivationQuote(
            id = "q25",
            quote = "Antrenmanın her dakikasından nefret ettim; ama kendime dedim ki: Şimdi acı çek ve hayatının geri kalanını bir şampiyon olarak yaşa.",
            author = "Muhammad Ali",
            context = "Şampiyon Bedeli",
            takeaway = "İdman anındaki zorluk dakikalarla ölçülür; fakat kazanacağın özsaygı ömür boyu sürer.",
            category = "Mamba"
        ),
        MotivationQuote(
            id = "q26",
            quote = "Standartlarını kimse görmezken bile en yukarıda tut. Mamba zihniyeti şans tanımaz.",
            author = "Tim Grover",
            context = "Relentless • Acımasız Ol",
            takeaway = "Başkaları izlemiyorken yaptığın tekrarlar, sahnedeki parlak zaferlerini belirler.",
            category = "Mamba"
        ),
        MotivationQuote(
            id = "q27",
            quote = "Telefonundaki anlık bildirimler ve sahte dopamin, senin gelecekteki saygınlığını çalıyor. Ekranı kapat, eyleme geç.",
            author = "Modern Stoik",
            context = "Dopamin Detoksu & Derin Odak",
            takeaway = "Sonsuz kaydırma yaparak tüketici olma; kalk ve kendi gücünü üreten savaşçı ol.",
            category = "Odak"
        ),
        MotivationQuote(
            id = "q28",
            quote = "24 saatin içinde kendine 30 dakika ayıramıyorsan kölesin demektir. Vaktim yok deme, önceliğim yok de.",
            author = "Seneca",
            context = "Zamanın Kısalığı Üzerine",
            takeaway = "Zaman eksikliği diye bir şey yoktur; yalnızca dağınık dikkat ve yanlış öncelikler vardır.",
            category = "Bahaneleri Sustur"
        ),
        MotivationQuote(
            id = "q29",
            quote = "Bir gün kaçırırsan kaza olur; iki gün üst üste kaçırırsan yeni bir kötü alışkanlık başlar. Zinciri kırma.",
            author = "James Clear",
            context = "Atomik Alışkanlıklar",
            takeaway = "Kötü bir gün geçirsen bile en azından 5 dakikalık bir idman yap. Asla 2 gün kaybetme.",
            category = "İrade"
        ),
        MotivationQuote(
            id = "q30",
            quote = "Yarın yaparım dediğin an, düne yenilmiş olursun. Güç yalnızca şu anın içindedir.",
            author = "Marcus Aurelius",
            context = "Kendime Düşünceler",
            takeaway = "Yarın diye bir zaman dilimi eylem üretmez. Güçlü insanlar sadece 'şimdi'yi kullanır.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q31",
            quote = "İradenin zayıflığı hissettiğin acıdan değil, pes etmeyi bir seçenek olarak görmenden kaynaklanır.",
            author = "Epictetus",
            context = "Zihinsel Dokunulmazlık",
            takeaway = "Geri çekilme köprülerini yak. Başka çaren kalmadığında ne kadar güçlü olduğunu göreceksin.",
            category = "Stoik"
        ),
        MotivationQuote(
            id = "q32",
            quote = "Kafanda oluşturduğun üşengeçlik bir illüzyondur. İlk seti yaptıktan 2 dakika sonra beynin sana teşekkür edecek.",
            author = "Nörobiyoloji İlkesi",
            context = "Harekete Geçme Kimyası",
            takeaway = "Eylem motivasyonu doğurur; motivasyon eylemi beklemez. İlk adımı körü körüne at.",
            category = "Bahaneleri Sustur"
        )
    )

    fun getDefaultHabits(): List<DisciplineHabit> {

        return listOf(
            DisciplineHabit(
                id = "h_cold",
                title = "🧊 Konfor Alanından Çık (Soğuk Su / Duş)",
                description = "Sabah güne iradeyle başla, bahanelere karşı zihnini uyar.",
                emoji = "🧊",
                category = "İrade"
            ),
            DisciplineHabit(
                id = "h_read",
                title = "📖 15 Dk Zihinsel Gelişim / Kitap",
                description = "Her gün zihnini derin bir fikirle besle, dikkatini geliştir.",
                emoji = "📖",
                category = "Zihin"
            ),
            DisciplineHabit(
                id = "h_focus",
                title = "📵 45 Dk Dijital & Dopamin Detoksu",
                description = "Bildirimleri kapat, tek bir göreve tüm dikkatinle odaklan.",
                emoji = "📵",
                category = "Odak"
            ),
            DisciplineHabit(
                id = "h_water",
                title = "💧 2.5 Litre Su & Bilinçli Beslenme",
                description = "Bedenine saygı göster; fiziksel berraklık zihinsel direnç sağlar.",
                emoji = "💧",
                category = "Beden"
            ),
            DisciplineHabit(
                id = "h_stoic",
                title = "✍️ 5 Dk Akşam Stoik Muhasebesi",
                description = "Günün değerlendirmesi: Nerelerde iradeliydin, nerelerde zayıf kaldın?",
                emoji = "✍️",
                category = "Zihin"
            )
        )
    }

    fun getDailyDisciplineMessage(
        streakDays: Int,
        crisisCount: Int,
        recentCrisisJustCompleted: Boolean = false
    ): String {
        return getDailyDisciplineMessage(null, streakDays, crisisCount, recentCrisisJustCompleted)
    }

    fun getDailyDisciplineMessage(
        profile: UserProfile?,
        streakDays: Int,
        crisisCount: Int,
        recentCrisisJustCompleted: Boolean = false
    ): String {
        if (recentCrisisJustCompleted) {
            return "Bugün motivasyon kazanmadın. Kontrolü ve iradeni geri aldın."
        }

        val style = profile?.motivationStyle ?: "Stoik Felsefe"
        val problem = profile?.biggestProblem ?: ""

        return when {
            problem.contains("Vakit", ignoreCase = true) || problem.contains("Zaman", ignoreCase = true) ->
                "24 saatin içinde 30 dakikanı kendine ayıramıyorsan kendi hayatının efendisi değilsin. Bahaneleri sustur."
            problem.contains("Telefon", ignoreCase = true) || problem.contains("Dikkat", ignoreCase = true) ->
                "Telefonundaki o parıltılı ekran senin geleceğini çalıyor. Ekranı kapat, demiri kaldır."
            problem.contains("1 gün", ignoreCase = true) || problem.contains("Aksat", ignoreCase = true) ->
                "Asla 2 gün üst üste kaybetme kuralı! Dün aksamış olsa bile bugün ayağa kalkmak zorundasın."
            problem.contains("Motivasyon", ignoreCase = true) ->
                "Motivasyon geçici bir hevestir, disiplin ise ömürlük bir güçtür. Canının istemesini bekleme."
            style.contains("Sert", ignoreCase = true) || style.contains("Goggins", ignoreCase = true) ->
                if (streakDays >= 7) "Sert zihniyet zafer getiriyor. Rahatlığa izin verme; sınırlarını her sette zorla."
                else "Bahanelerinin hiçbiri gerçek değil. Yorgun olduğunda değil, iş bittiğinde dur."
            style.contains("Mamba", ignoreCase = true) || style.contains("Şampiyon", ignoreCase = true) ->
                if (streakDays >= 7) "$streakDays gündür şampiyon gibi ter döküyorsun. Standartlarını asla düşürme."
                else "Karanlıkta dökülen ter, ışıklar altında zafere dönüşür. Bugün bir tekrar daha."
            style.contains("Stoik", ignoreCase = true) ->
                if (streakDays >= 7) "Amor Fati. Zorlukları kucakladın, 7 günü aştın. Kararlılıkla devam et."
                else "Dış dünyayı kontrol edemezsin fakat kendi iradeni daima yönetebilirsin. Görevini yap."
            streakDays >= 14 ->
                "İstemediğin gün yaptığın antrenman, gerçekten seni değiştiren antrenmandır. $streakDays günlük seri zırhındır."
            streakDays >= 7 ->
                "7 gün önce karar verdin. Şimdi bunu sarsılmaz bir alışkanlığa dönüştürüyorsun."
            crisisCount > 0 && streakDays in 1..3 ->
                "Önemli olan hiç düşmemek değil. Düştükten sonra ne kadar çabuk kalktığındır."
            streakDays == 0 ->
                "Motivasyonunu değil, sistemini kaybettin. Sistemi ve antrenmanını hemen şimdi yeniden başlat."
            else ->
                "Kendine verdiğin sözleri tuttuğun her gün, yeni kimliğin inşa ediliyor."
        }
    }

    fun getPersonalizedQuotes(profile: UserProfile?): List<MotivationQuote> {
        val style = profile?.motivationStyle ?: "Stoik Felsefe"
        val problem = profile?.biggestProblem ?: ""

        val priorityCategory = when {
            style.contains("Sert", ignoreCase = true) || style.contains("Goggins", ignoreCase = true) -> "Sert Gerçekler"
            style.contains("Mamba", ignoreCase = true) || style.contains("Şampiyon", ignoreCase = true) -> "Mamba"
            style.contains("Stoik", ignoreCase = true) -> "Stoik"
            style.contains("Odak", ignoreCase = true) || style.contains("Musashi", ignoreCase = true) -> "Odak"
            else -> "İrade"
        }

        val problemCategory = when {
            problem.contains("Zaman", ignoreCase = true) || problem.contains("Vakit", ignoreCase = true) -> "Bahaneleri Sustur"
            problem.contains("Telefon", ignoreCase = true) || problem.contains("Dikkat", ignoreCase = true) -> "Odak"
            problem.contains("Üşen", ignoreCase = true) || problem.contains("Yorgun", ignoreCase = true) -> "Sert Gerçekler"
            problem.contains("Aksat", ignoreCase = true) || problem.contains("Bırak", ignoreCase = true) -> "İrade"
            else -> "Bahaneleri Sustur"
        }

        return motivationQuotes.sortedByDescending { q ->
            var weight = 0
            if (q.category == priorityCategory) weight += 15
            if (q.category == problemCategory) weight += 8
            if (problem.isNotEmpty() && q.quote.contains("zaman", ignoreCase = true) && problem.contains("zaman", ignoreCase = true)) weight += 10
            if (problem.isNotEmpty() && q.quote.contains("yorgun", ignoreCase = true) && problem.contains("yorgun", ignoreCase = true)) weight += 10
            if (problem.isNotEmpty() && q.quote.contains("telefon", ignoreCase = true) && problem.contains("telefon", ignoreCase = true)) weight += 10
            weight
        }
    }


    fun getTimeGreeting(name: String): Pair<String, String> {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..11 -> "Günaydın, $name."
            in 12..17 -> "İyi günler, $name."
            in 18..22 -> "İyi akşamlar, $name."
            else -> "İyi geceler, $name."
        }
        val sub = "Bugün kendin için ne yapacaksın?"
        return Pair(greeting, sub)
    }

    fun getBehavioralInsights(
        longestStreak: Int,
        totalWorkouts: Int
    ): List<BehavioralInsight> {
        return listOf(
            BehavioralInsight(
                type = InsightType.WARNING,
                title = "⚠️ DİKKAT",
                description = "Pazartesi günleri antrenman atlama oranının yüksek. Son 4 Pazartesinin 3'ünde spor yapmadın.",
                badge = "Hafta Başı Direnci"
            ),
            BehavioralInsight(
                type = InsightType.DISCOVERY,
                title = "💡 TESPİT",
                description = "Akşam saatlerinde daha düzenlisin. Antrenmanlarının %72'si 18.00'dan sonra gerçekleşti.",
                badge = "Biyolojik Ritim"
            ),
            BehavioralInsight(
                type = InsightType.STRENGTH,
                title = "🔥 GÜÇLÜ YÖN",
                description = "Başladığında uzun süre devam edebiliyorsun. En uzun serin: $longestStreak gün.",
                badge = "Momentum Gücü"
            )
        )
    }
}
