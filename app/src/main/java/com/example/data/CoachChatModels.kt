package com.example.data

enum class CoachPersona(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val quote: String
) {
    DEMIR_KOC(
        title = "Demir Koç",
        subtitle = "Sert Gerçekler & Goggins Tarzı",
        iconEmoji = "⚔️",
        quote = "Beynin sana yalan söylüyor. Yorgun hissettiğinde kapasitenin sadece %40'ındasın!"
    ),
    STOIK_MARCUS(
        title = "Marcus",
        subtitle = "Stoik Zihniyet & Soğukkanlı İrade",
        iconEmoji = "🛡️",
        quote = "Zihninin efendisi ol. Engel yolun kendisidir, kontrol edemediğin şeylere üzülmeyi bırak."
    ),
    MAMBA_KOBE(
        title = "Mamba Koç",
        subtitle = "Şampiyon Zihniyeti & Obsesyon",
        iconEmoji = "🐍",
        quote = "Rakiplerin uyurken salonda ol. Sıradan olmak en büyük günahtır."
    ),
    BILGE_USTA(
        title = "Bilge Usta",
        subtitle = "Odak, Sade Eylem & Musashi",
        iconEmoji = "🧘",
        quote = "Gürültüyü sustur, eyleme odaklan. Yol sadece ilk adımı attığında görünür olur."
    )
}

enum class MessageSender {
    COACH,
    USER
}

enum class CoachActionType {
    START_5_SEC_RULE,
    NAVIGATE_WORKOUT,
    BREATHING_EXERCISE,
    COPY_ADVICE,
    SHARE_ADVICE
}

data class CoachPromptOption(
    val id: String,
    val label: String,
    val category: String,
    val icon: String,
    val payloadKey: String = id
)

data class CoachChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val persona: CoachPersona = CoachPersona.DEMIR_KOC,
    val followUpOptions: List<CoachPromptOption> = emptyList(),
    val actionType: CoachActionType? = null,
    val keyPunchline: String? = null
)
