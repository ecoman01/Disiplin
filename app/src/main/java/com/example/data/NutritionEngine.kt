package com.example.data

data class NutritionRecipe(
    val id: String,
    val title: String,
    val category: String, // "Sağlıklı Tatlılar", "Yüksek Protein", "Pratik & Hızlı", "Detoks & İçecek"
    val prepMinutes: Int,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val emoji: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val disciplineTip: String
)

data class DailyNutritionPlan(
    val title: String,
    val targetGoal: String,
    val dailyCalorieTarget: String,
    val dailyProteinTarget: String,
    val mealSuggestions: List<String>,
    val hydrationTip: String
)

object NutritionEngine {

    val categories = listOf(
        "Tümü",
        "Sağlıklı Tatlılar",
        "Yüksek Protein",
        "Pratik & Hızlı",
        "Detoks & İçecek"
    )

    val recipes: List<NutritionRecipe> = listOf(
        // --- SAĞLIKLI TATLILAR ---
        NutritionRecipe(
            id = "dessert_1",
            title = "Şekersiz Fit Yulaf Sufle",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 12,
            calories = 275,
            proteinG = 24,
            carbsG = 28,
            fatG = 6,
            emoji = "🍫",
            description = "Akışkan bitter çikolata kalbiyle tatlı krizini hedeflerinden sapmadan yok eden yüksek proteinli sufle.",
            ingredients = listOf(
                "35g ince öğütülmüş yulaf ezmesi / unu",
                "1 adet olgun muz (ezilmiş)",
                "1 ölçek çikolatalı protein tozu (veya 15g ham kakao)",
                "1 adet yumurta akı",
                "40ml badem sütü veya yağsız süt",
                "1 çay kaşığı kabartma tozu",
                "1 kare (%80+) bitter çikolata (iç harcı için)"
            ),
            instructions = listOf(
                "Yulaf, muz, protein tozu, yumurta akı, süt ve kabartma tozunu pürüzsüz kıvama gelene kadar çatalla veya blenderla karıştır.",
                "Karışımı fırına veya mikrodalgaya uygun küçük bir sufle kabına dök.",
                "Orta kısmına bitter çikolata karesini hafifçe batır.",
                "Mikrodalgada 90 saniye veya önceden ısıtılmış 180°C fırında 10-12 dakika pişir. İçi akışkan kalmalı!",
                "Sıcakken tüket, tatlı krizini disiplinle aş!"
            ),
            disciplineTip = "Rafine şeker insülin dalgalanması yaratarak iradeni zayıflatır. Doğal muz ve ham kakaolu bu tarif tatlı isteğini keserken kaslarına 24g protein taşır."
        ),
        NutritionRecipe(
            id = "dessert_2",
            title = "Fıstık Ezmeli & Muzlu Fit Bar",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 10,
            calories = 210,
            proteinG = 15,
            carbsG = 22,
            fatG = 7,
            emoji = "🥜",
            description = "Fırın gerektirmeyen, çantada taşınabilir, antrenman öncesi veya sonrası için mükemmel ev yapımı enerji barı.",
            ingredients = listOf(
                "100g yulaf ezmesi",
                "2 yemek kaşığı %100 şekersiz yer fıstığı ezmesi",
                "1 yemek kaşığı chia veya keten tohumu",
                "1.5 yemek kaşığı bal veya agave şurubu",
                "1 tatlı kaşığı tarçın",
                "1 ölçek vanilyalı veya çikolatalı protein tozu"
            ),
            instructions = listOf(
                "Geniş bir kapta fıstık ezmesi ve balı hafif ısıtıp akışkanlaştır.",
                "Yulaf, protein tozu, chia ve tarçını ekleyerek yoğun bir hamur kıvamına gelene kadar yoğur.",
                "Karışımı pişirme kağıdı serili küçük bir kaba dikdörtgen şeklinde bastırarak yay.",
                "Buzdolabında 30 dakika beklettikten sonra 6 eşit bar halinde dilimle.",
                "Haftalık atıştırmalığın hazır!"
            ),
            disciplineTip = "Dışarıdaki katkı maddeli paketli barlara para ve sağlık harcamak yerine, 10 dakikada hazırladığın bu barlarla disiplinini koru."
        ),
        NutritionRecipe(
            id = "dessert_3",
            title = "Avokadolu Kakaolu Kremalı Puding",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 5,
            calories = 195,
            proteinG = 8,
            carbsG = 18,
            fatG = 11,
            emoji = "🥑",
            description = "İpeksi dokusuyla sıfır pişirmeyle hazırlanan, sağlıklı yağlar ve antioksidan deposu çikolatalı puding.",
            ingredients = listOf(
                "1 adet tam olgunlaşmış yumuşak avokado",
                "2 yemek kaşığı kaliteli ham kakao",
                "1 tatlı kaşığı bal veya hurma ezmesi",
                "50ml soğuk badem sütü",
                "1 çay kaşığı vanilya özütü",
                "Üzeri için 4-5 adet kırık çiğ fındık"
            ),
            instructions = listOf(
                "Avokadoyu ikiye bölüp çekirdeğini çıkar ve kaşıkla blender haznesine al.",
                "Ham kakao, bal, badem sütü ve vanilyayı ekle.",
                "Tamamen parlak ve ipeksi kıvam alana kadar yüksek devirde 45 saniye pürele.",
                "Kadehe alıp üzerine fındık serperek soğuk servis yap."
            ),
            disciplineTip = "Avokadodaki tekli doymamış yağlar tokluk hissi verir ve odaklanma yeteneğini artıran beyin fonksiyonlarını destekler."
        ),
        NutritionRecipe(
            id = "dessert_4",
            title = "Yüksek Proteinli Lor & Yulaf Pankeki",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 15,
            calories = 310,
            proteinG = 28,
            carbsG = 32,
            fatG = 6,
            emoji = "🥞",
            description = "Hafta sonu ödülünü bozmadan yapabileceğin, yüksek biyoyararlanıma sahip protein pankeki.",
            ingredients = listOf(
                "100g tatlı lor peyniri (veya süzme peynir)",
                "40g öğütülmüş yulaf",
                "2 adet yumurta (1 tam, 1 beyaz)",
                "Yarım çay kaşığı tarçın",
                "Üzeri için bir avuç yaban mersini / çilek"
            ),
            instructions = listOf(
                "Lor peyniri, yulaf, yumurtalar ve tarçını mikserde pürüzsüzleştir.",
                "Yapışmaz tavayı hafifçe zeytinyağı peçetesiyle sil ve orta ateşte ısıt.",
                "Harcı küçük yuvarlaklar halinde tavaya dök, üzeri göz göz olunca ters çevirip 1 dakika daha pişir.",
                "Meyvelerle süsleyip sıcak tüket."
            ),
            disciplineTip = "Lor peyniri en ekonomik ve en yüksek kazein/whey oranına sahip protein kaynaklarından biridir."
        ),
        NutritionRecipe(
            id = "dessert_5",
            title = "Unsuz & Şekersiz Fit Brownie",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 18,
            calories = 180,
            proteinG = 12,
            carbsG = 16,
            fatG = 6,
            emoji = "🍪",
            description = "Unsuz, rafine şekersiz, yoğun çikolata lezzetiyle tatlı krizini disiplinli bir ziyafete çeviren fit brownie.",
            ingredients = listOf(
                "2 adet olgun muz",
                "2 adet tam yumurta",
                "3 yemek kaşığı kaliteli ham kakao",
                "2 yemek kaşığı fıstık ezmesi (%100 fıstık)",
                "1 çay kaşığı kabartma tozu",
                "Üzeri için parça bitter çikolata (%85+)"
            ),
            instructions = listOf(
                "Muzları çatalla tamamen püre haline getir.",
                "Yumurtaları, kakaoyu, fıstık ezmesini ve kabartma tozunu ekleyip çırp.",
                "Karışımı yağlı kağıt serili küçük fırın kabına yay.",
                "180 derece önceden ısıtılmış fırında 16-18 dakika pişir.",
                "Ilındıktan sonra kare dilimleyerek servis yap."
            ),
            disciplineTip = "Beynin şeker krizini bir zaferle atlat: Ham kakao serotonin ve dopamin salgısını tetiklerken rafine şekerin yarattığı halsizliği önler."
        ),
        NutritionRecipe(
            id = "dessert_6",
            title = "Yüksek Proteinli Fit Tiramisu",
            category = "Sağlıklı Tatlılar",
            prepMinutes = 10,
            calories = 220,
            proteinG = 26,
            carbsG = 20,
            fatG = 3,
            emoji = "☕",
            description = "Espresso aroması ve yüksek proteinli labne-yoğurt kremasıyla İtalyan klasiğini sporcu formuna sokan efsane.",
            ingredients = listOf(
                "40g yulaf ezmesi (veya 2 adet tam buğday etimek)",
                "1 fincan taze demlenmiş sıcak espresso / filtre kahve",
                "150g yağsız süzme yoğurt + 50g light labne",
                "1 ölçek vanilyalı protein tozu (veya 1 tatlı kaşığı bal)",
                "Üzeri için elenmiş ham kakao"
            ),
            instructions = listOf(
                "Taban için yulafları sıcak kahveyle ıslatıp kupta 2 dakika dinlendir.",
                "Ayrı bir kapta yoğurt, labne ve vanilyalı protein tozunu pürüzsüz krema kıvamına gelene kadar çırp.",
                "Kahveli tabanın üzerine bolca kremayı dök.",
                "Üzerine çay süzgeciyle bol ham kakao serp.",
                "Buzdolabında 20 dakika soğutup tüket."
            ),
            disciplineTip = "Kahvedeki kafein antrenman motivasyonunu artırırken, 26 gramlık kazein-whey proteini tokluk süreni saatlerce uzatır."
        ),

        // --- YÜKSEK PROTEİN ---
        NutritionRecipe(
            id = "protein_1",
            title = "Fırın Baharatlı Tavuk & Tatlı Patates Bowl",
            category = "Yüksek Protein",
            prepMinutes = 25,
            calories = 450,
            proteinG = 46,
            carbsG = 42,
            fatG = 9,
            emoji = "🍗",
            description = "İdman sonrası kas onarımı için altın standart: Kompleks karbonhidrat ve yağsız tavuk göğsü.",
            ingredients = listOf(
                "180g derisiz tavuk göğsü (küp doğranmış)",
                "150g tatlı patates (küp doğranmış)",
                "100g buharda brokoli",
                "1 tatlı kaşığı zeytinyağı",
                "Kırmızı toz biber, kimyon, kekik, karabiber, az kaya tuzu"
            ),
            instructions = listOf(
                "Fırını 200°C'ye ısıt.",
                "Tavuk ve tatlı patates küplerini baharatlar ve 1 tatlı kaşığı zeytinyağıyla harmanla.",
                "Fırın tepsisine yayıp 20-22 dakika pişir.",
                "Yanına buharda hafif haşlanmış brokolileri ekleyerek derin bir kasede sun."
            ),
            disciplineTip = "Meal prep (önceden yemek hazırlığı) disiplinin gizli silahıdır. Pazar günü 3 porsiyon hazırla, hafta içi dışarıdan fast food tuzağına düşme."
        ),
        NutritionRecipe(
            id = "protein_2",
            title = "Ton Balıklı & Kinoalı Akdeniz Güç Salatası",
            category = "Yüksek Protein",
            prepMinutes = 10,
            calories = 370,
            proteinG = 38,
            carbsG = 30,
            fatG = 8,
            emoji = "🥗",
            description = "Pişirme zahmeti olmayan, omega-3 ve tam aminoasit profili sağlayan serinletici öğün.",
            ingredients = listOf(
                "1 kutu (160g) süzülmüş zeytinyağlı/doğal ton balığı",
                "70g haşlanmış kinoa",
                "1 kase taze tırnak roka ve marul",
                "6-7 adet çeri domates",
                "Yarım limon suyu, 1 tatlı kaşığı nar ekşisi, 3 yarım ceviz"
            ),
            instructions = listOf(
                "Geniş bir kasede roka ve marulları elle parçala.",
                "Haşlanmış soğuk kinoayı ve ikiye bölünmüş çeri domatesleri ekle.",
                "Ton balığını çatalla hafif didikleyip salatanın üzerine yerleştir.",
                "Limon suyu ve cevizleri ekleyip karıştır."
            ),
            disciplineTip = "Omega-3 yağ asitleri kas içi iltihabı (DOMS) azaltır ve eklem sağlığını ağır antrenmanlara karşı korur."
        ),

        // --- PRATİK & HIZLI ---
        NutritionRecipe(
            id = "quick_1",
            title = "Gece Yulafı (Overnight Oats) Orman Meyveli",
            category = "Pratik & Hızlı",
            prepMinutes = 5,
            calories = 310,
            proteinG = 22,
            carbsG = 38,
            fatG = 6,
            emoji = "🥣",
            description = "Akşamdan 3 dakikada hazırla, sabah uyanır uyanmaz 0 saniye vakit harcayarak enerjiyi yakala.",
            ingredients = listOf(
                "45g yulaf ezmesi",
                "150g probiyotik yoğurt veya süzme yoğurt",
                "50ml badem sütü",
                "1 tatlı kaşığı chia tohumu",
                "1 avuç dondurulmuş vişne veya böğürtlen"
            ),
            instructions = listOf(
                "Bir kavanoza yulaf, chia, yoğurt ve sütü koyup kaşıkla iyice homojenleştir.",
                "Üzerine meyveleri yerleştir ve kavanozun kapağını kapat.",
                "Buzdolabında en az 4 saat (tercihen gece boyu) beklet.",
                "Sabah kapağını aç ve hemen tüket!"
            ),
            disciplineTip = "Sabahları 'ne yesem' kararsızlığı irade enerjini tüketir. Kararı akşamdan vererek sabah iradeni koru."
        ),
        NutritionRecipe(
            id = "quick_2",
            title = "Demir Yumruk Fit Dürüm",
            category = "Pratik & Hızlı",
            prepMinutes = 8,
            calories = 340,
            proteinG = 32,
            carbsG = 26,
            fatG = 8,
            emoji = "🌯",
            description = "Masa başında veya yolda hızlıca yiyebileceğin yüksek proteinli tam buğdaylı dürüm.",
            ingredients = listOf(
                "1 adet tam buğday lavaş",
                "2 adet haşlanmış yumurta (dilimlenmiş)",
                "50g yağsız beyaz peynir veya süzme peynir",
                "Bol taze nane, maydanoz, salatalık şeritleri",
                "Pul biber ve kekik"
            ),
            instructions = listOf(
                "Lavaşın ortasına peyniri çatalla yay.",
                "Haşlanmış yumurta dilimlerini diz, baharatları ekle.",
                "Yeşillikleri ve salatalığı koyup sıkıca rulo yap.",
                "İster tavada 1 dakika hafif çıtırdat, ister soğuk tüket."
            ),
            disciplineTip = "Hızlı yemek yemek sağlıksız beslenmek zorunda olduğun anlamına gelmez. Doğru malzemeler 5 dakikada mükemmel besler."
        ),

        // --- DETOKS & İÇECEK ---
        NutritionRecipe(
            id = "drink_1",
            title = "Zihinsel Netlik Yeşil Detoks Smoothie",
            category = "Detoks & İçecek",
            prepMinutes = 4,
            calories = 145,
            proteinG = 6,
            carbsG = 26,
            fatG = 1,
            emoji = "🥬",
            description = "Beyin sisi ve halsizliği anında temizleyen klorofil, zencefil ve elektrolit bombası.",
            ingredients = listOf(
                "1 avuç dolusu bebek ıspanak",
                "Yarım yeşil elma",
                "1 adet küçük salatalık",
                "1 fındık büyüklüğünde taze zencefil",
                "Yarım limonun suyu",
                "200ml soğuk maden suyu / su",
                "3-4 yaprak taze nane"
            ),
            instructions = listOf(
                "Tüm malzemeleri yıkayıp kabaca doğra.",
                "Blender haznesine soğuk suyla birlikte ekle.",
                "Pürüzsüz yemyeşil bir kıvam alana kadar 40 saniye çek.",
                "Bardağa döküp bekletmeden taze iç."
            ),
            disciplineTip = "Öğleden sonra gelen rehavet genellikle susuzluk ve mikro-besin eksikliğindendir. Kahveye sarılmak yerine hücrelerini canlandır."
        ),
        NutritionRecipe(
            id = "drink_2",
            title = "Altın Süt (Zerdeçallı Toparlanma İksiri)",
            category = "Detoks & İçecek",
            prepMinutes = 6,
            calories = 120,
            proteinG = 7,
            carbsG = 12,
            fatG = 3,
            emoji = "🥛",
            description = "Ağır idman sonrası kas yangısını söndüren ve derin uyku kalitesini artıran stoik gece içeceği.",
            ingredients = listOf(
                "200ml süt (veya bitkisel süt)",
                "1 çay kaşığı toz zerdeçal",
                "Yarım çay kaşığı toz tarçın",
                "Çok az karabiber (kurkumin emilimini %2000 artırır)",
                "1 çay kaşığı bal (ılıkken ekle)"
            ),
            instructions = listOf(
                "Küçük bir cezvede sütü, zerdeçalı, tarçını ve karabiberi kısık ateşte karıştırarak ısıt.",
                "Kaynama noktasına gelmeden ocaktan al.",
                "Bardağa döküp hafif ılıyınca balı ekle.",
                "Yatmadan 45 dakika önce sıcak tüket."
            ),
            disciplineTip = "Toparlanma antrenmanın yarısıdır. Gece kasların tamir olurken derin uyku hormonlarını bu içecekle destekle."
        )
    )

    val disciplineTips = listOf(
        "Su tüketimini ihmal etme: Günde en az 2.5 - 3 litre su iç. Susuzluk sıklıkla açlık veya tatlı krizleriyle karıştırılır.",
        "Şekersiz tatlılar hazırla: Canın tatlı istediğinde market çikolatasına değil, tariflerimizdeki gibi proteinli ve lifli fit tatlılara yönel.",
        "Öğünlerini planla: Açken markete veya buzdolabına gitmek iradeni sınar. Ne yiyeceğini bir gün önceden belirle.",
        "Protein hedefini tuttur: Her öğünde en az 20-30g kaliteli protein tüketmek tokluk hormonlarını zirvede tutar.",
        "Yemek yerken ekrana bakma: Dikkatin dağıldığında doyma sinyallerini kaçırırsın. Bilinçli ve yavaş çiğne."
    )

    fun getRecipesByCategory(category: String): List<NutritionRecipe> {
        if (category == "Tümü") return recipes
        return recipes.filter { it.category.equals(category, ignoreCase = true) }
    }
}
