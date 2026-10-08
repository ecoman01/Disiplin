package com.example.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

object ShareHelper {

    /**
     * General purpose text sharing using Android system chooser.
     */
    fun shareText(context: Context, text: String, title: String = "Paylaş") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, title))
        } catch (e: Exception) {
            Toast.makeText(context, "Paylaşım açılamadı", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Attempts to share text directly to WhatsApp.
     * If WhatsApp is not installed, seamlessly falls back to standard Android app chooser.
     */
    fun shareToWhatsApp(context: Context, message: String) {
        val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            setPackage("com.whatsapp")
        }

        try {
            context.startActivity(whatsappIntent)
        } catch (e: Exception) {
            try {
                val chooserIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                }
                context.startActivity(Intent.createChooser(chooserIntent, "WhatsApp veya Başka Uygulamayla Paylaş"))
            } catch (err: Exception) {
                Toast.makeText(context, "Paylaşım açılamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Builds an epic warrior status card text for sharing on WhatsApp.
     */
    fun buildWarriorJourneyShareText(
        userName: String,
        levelTitle: String,
        levelNumber: Int,
        totalXp: Int,
        streakDays: Int,
        completedWorkouts: Int
    ): String {
        return """
            ⚔️ DİSİPLİN GÜNLÜĞÜ | SAVAŞÇININ YOLCULUĞU ⚔️
            
            👤 Savaşçı: $userName
            🛡️ Seviye: $levelNumber. Seviye — $levelTitle
            ⚡ Deneyim: $totalXp XP
            🔥 Kesintisiz Seri: $streakDays Gün
            🏆 Tamamlanan İdman: $completedWorkouts Seans
            
            "Motivasyon biter, sistem ve disiplin kalır. Ertelemeyi yıktım, tavizsiz yola devam ediyorum!"
            
            📲 #Disiplin #StoikIrade #Antrenman #NoExcuses
        """.trimIndent()
    }

    /**
     * Builds workout victory text for WhatsApp.
     */
    fun buildWorkoutShareText(
        planTitle: String,
        targetMuscles: String,
        estimatedMinutes: Int = 45,
        durationMinutes: Int = estimatedMinutes,
        streakDays: Int = 0,
        userName: String = "Savaşçı"
    ): String {
        val minutes = if (durationMinutes > 0) durationMinutes else estimatedMinutes
        return """
            🔥 DİSİPLİN ZAFERİ: BUGÜNKÜ İDMAN PLANI 💪
            
            👤 Savaşçı: $userName
            🏋️ Program: $planTitle
            🎯 Hedef Kaslar: $targetMuscles
            ⏱️ Süre: $minutes Dakika
            🔥 Seri: $streakDays Gün
            
            "Bugün de bahane üretmedim, ter döktüm ve sözümü tuttum. Asla vazgeçme!"
            
            📲 #Disiplin #GününAntrenmanı #StoikIrade
        """.trimIndent()
    }

    /**
     * Builds stoic mindset quote text for WhatsApp.
     */
    fun buildQuoteShareText(
        quote: String,
        author: String,
        category: String = "Disiplin & İrade",
        takeaway: String = ""
    ): String {
        val takeawayText = if (takeaway.isNotBlank()) "\n💡 İlke: $takeaway\n" else ""
        return """
            📜 DİSİPLİN & STOİK İRADE DÜŞÜNCESİ
            
            “$quote”
            — $author
            $takeawayText
            🏷️ Kategori: $category
            ⚡ "Disiplin kaderindir. Erteleme, eyleme geç!"
            
            📲 #Disiplin #StoikFelsefe #Zihniyet
        """.trimIndent()
    }
}
