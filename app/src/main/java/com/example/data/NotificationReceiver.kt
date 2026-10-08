package com.example.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_WORKOUT_NOTIFICATION = "com.example.ACTION_WORKOUT_NOTIFICATION"
        const val ACTION_MOTIVATION_NOTIFICATION = "com.example.ACTION_MOTIVATION_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = DisciplineDatabase.getDatabase(context)
                val profile = db.userDao().getUserProfileSync()
                val userName = profile?.name?.ifBlank { "Savaşçı" } ?: "Savaşçı"

                when (action) {
                    ACTION_WORKOUT_NOTIFICATION -> {
                        val plans = db.workoutDao().getAllPlansSync()
                        val deviceDayOfWeek = NotificationHelper.getDeviceDayOfWeekIndex()
                        val deviceDayName = NotificationHelper.getDeviceDayName(deviceDayOfWeek)

                        // Cihazın bugünkü takvim gününe (Pazartesi-Pazar) ait planı seç
                        val todayPlan = plans.firstOrNull { it.dayOfWeek == deviceDayOfWeek && !it.isCustom }
                            ?: plans.firstOrNull { it.dayOfWeek == deviceDayOfWeek }
                            ?: plans.firstOrNull { !it.isCustom }

                        val planTitle = todayPlan?.title ?: "$deviceDayName İdmanı"
                        NotificationHelper.sendWorkoutNotification(
                            context = context,
                            planTitle = planTitle,
                            userName = userName,
                            dayName = todayPlan?.dayName ?: deviceDayName,
                            targetMuscles = todayPlan?.targetMuscles
                        )

                        // Bir sonraki günün bildirimini tam seçilen saatte çalması için yeniden kur
                        val settings = NotificationPreferences.getSettings(context)
                        if (settings.workoutEnabled) {
                            NotificationHelper.scheduleWorkoutAlarm(
                                context,
                                settings.workoutHour,
                                settings.workoutMinute
                            )
                        }
                    }
                    ACTION_MOTIVATION_NOTIFICATION -> {
                        val quotes = DisciplineEngine.motivationQuotes
                        val randomQuote = quotes.random()
                        NotificationHelper.sendMotivationNotification(
                            context = context,
                            quote = randomQuote.quote,
                            author = randomQuote.author,
                            userName = userName
                        )

                        // Bir sonraki günün motivasyon alarmını yeniden kur
                        val settings = NotificationPreferences.getSettings(context)
                        if (settings.motivationEnabled) {
                            NotificationHelper.scheduleMotivationAlarm(
                                context,
                                settings.motivationHour,
                                settings.motivationMinute
                            )
                        }
                    }
                    Intent.ACTION_BOOT_COMPLETED -> {
                        // Cihaz yeniden başladığında alarmları kayıtlı saatlere göre geri kur
                        val settings = NotificationPreferences.getSettings(context)
                        if (settings.workoutEnabled) {
                            NotificationHelper.scheduleWorkoutAlarm(
                                context,
                                settings.workoutHour,
                                settings.workoutMinute
                            )
                        }
                        if (settings.motivationEnabled) {
                            NotificationHelper.scheduleMotivationAlarm(
                                context,
                                settings.motivationHour,
                                settings.motivationMinute
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
}
