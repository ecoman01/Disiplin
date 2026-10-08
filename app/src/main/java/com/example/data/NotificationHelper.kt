package com.example.data

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import java.util.Calendar

data class NotificationSettings(
    val workoutEnabled: Boolean = true,
    val motivationEnabled: Boolean = true,
    val workoutHour: Int = 18,
    val workoutMinute: Int = 0,
    val motivationHour: Int = 9,
    val motivationMinute: Int = 0
)

object NotificationPreferences {
    private const val PREFS_NAME = "discipline_notification_prefs"
    private const val KEY_WORKOUT_ENABLED = "key_workout_enabled"
    private const val KEY_MOTIVATION_ENABLED = "key_motivation_enabled"
    private const val KEY_WORKOUT_HOUR = "key_workout_hour"
    private const val KEY_WORKOUT_MINUTE = "key_workout_minute"
    private const val KEY_MOTIVATION_HOUR = "key_motivation_hour"
    private const val KEY_MOTIVATION_MINUTE = "key_motivation_minute"
    private const val KEY_ASKED_PERMISSION = "key_asked_notification_permission"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun hasAskedPermission(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ASKED_PERMISSION, false)
    }

    fun setAskedPermission(context: Context, asked: Boolean = true) {
        getPrefs(context).edit().putBoolean(KEY_ASKED_PERMISSION, asked).apply()
    }

    fun getSettings(context: Context): NotificationSettings {
        val prefs = getPrefs(context)
        return NotificationSettings(
            workoutEnabled = prefs.getBoolean(KEY_WORKOUT_ENABLED, true),
            motivationEnabled = prefs.getBoolean(KEY_MOTIVATION_ENABLED, true),
            workoutHour = prefs.getInt(KEY_WORKOUT_HOUR, 18),
            workoutMinute = prefs.getInt(KEY_WORKOUT_MINUTE, 0),
            motivationHour = prefs.getInt(KEY_MOTIVATION_HOUR, 9),
            motivationMinute = prefs.getInt(KEY_MOTIVATION_MINUTE, 0)
        )
    }

    fun saveSettings(context: Context, settings: NotificationSettings) {
        getPrefs(context).edit()
            .putBoolean(KEY_WORKOUT_ENABLED, settings.workoutEnabled)
            .putBoolean(KEY_MOTIVATION_ENABLED, settings.motivationEnabled)
            .putInt(KEY_WORKOUT_HOUR, settings.workoutHour)
            .putInt(KEY_WORKOUT_MINUTE, settings.workoutMinute)
            .putInt(KEY_MOTIVATION_HOUR, settings.motivationHour)
            .putInt(KEY_MOTIVATION_MINUTE, settings.motivationMinute)
            .apply()

        if (settings.workoutEnabled) {
            NotificationHelper.scheduleWorkoutAlarm(context, settings.workoutHour, settings.workoutMinute)
        } else {
            NotificationHelper.cancelWorkoutAlarm(context)
        }

        if (settings.motivationEnabled) {
            NotificationHelper.scheduleMotivationAlarm(context, settings.motivationHour, settings.motivationMinute)
        } else {
            NotificationHelper.cancelMotivationAlarm(context)
        }
    }
}

object NotificationHelper {
    const val CHANNEL_WORKOUT_ID = "discipline_workout_channel"
    const val CHANNEL_MOTIVATION_ID = "discipline_motivation_channel"

    const val NOTIFICATION_ID_WORKOUT = 1001
    const val NOTIFICATION_ID_MOTIVATION = 1002

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val workoutChannel = NotificationChannel(
                CHANNEL_WORKOUT_ID,
                "Antrenman Hatırlatıcıları",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Günün antrenman planı ve zincir koruma bildirimleri"
                enableVibration(true)
            }

            val motivationChannel = NotificationChannel(
                CHANNEL_MOTIVATION_ID,
                "Motivasyon & Stoik Sözler",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Günün motivasyon ve zihniyet disiplin sözleri"
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(workoutChannel)
            notificationManager.createNotificationChannel(motivationChannel)
        }
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    /**
     * Cihazın mevcut güncel takvim gününü döner (1: Pazartesi ... 7: Pazar).
     */
    fun getDeviceDayOfWeekIndex(): Int {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            else -> 1
        }
    }

    /**
     * Gün indeksine göre Türkçe gün adı döner.
     */
    fun getDeviceDayName(dayIndex: Int = getDeviceDayOfWeekIndex()): String {
        return when (dayIndex) {
            1 -> "Pazartesi"
            2 -> "Salı"
            3 -> "Çarşamba"
            4 -> "Perşembe"
            5 -> "Cuma"
            6 -> "Cumartesi"
            7 -> "Pazar"
            else -> "Bugün"
        }
    }

    /**
     * Bildirimin bir sonraki çalma zamanını okunabilir metin olarak döner.
     */
    fun getNextTriggerDescription(hour: Int, minute: Int): String {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val isToday = target.timeInMillis > now.timeInMillis
        val formattedTime = "%02d:%02d".format(hour, minute)
        return if (isToday) "Bugün $formattedTime" else "Yarın $formattedTime"
    }

    fun sendWorkoutNotification(
        context: Context,
        planTitle: String = "Günün Antrenmanı",
        userName: String = "Savaşçı",
        dayName: String? = null,
        targetMuscles: String? = null
    ): Boolean {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "workouts")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            10,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val resolvedDay = dayName ?: getDeviceDayName()
        val muscleInfo = if (!targetMuscles.isNullOrBlank()) "\n🎯 Hedef Bölge: $targetMuscles" else ""

        val builder = NotificationCompat.Builder(context, CHANNEL_WORKOUT_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🏋️ $resolvedDay İdmanı Vakti Geldi, $userName!")
            .setContentText("Bugün: $planTitle seni bekliyor. Zincirini kırma!")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "📅 Cihaz Günü: $resolvedDay\n💪 İdman: $planTitle$muscleInfo\n\nDemir iradeni korumak için bugünkü antrenmanını tamamla. Ertelemek yok, disiplin seni özgürleştirir!"
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        return try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(NOTIFICATION_ID_WORKOUT, builder.build())
            true
        } catch (e: SecurityException) {
            false
        }
    }

    fun sendMotivationNotification(
        context: Context,
        quote: String,
        author: String,
        userName: String = "Savaşçı"
    ): Boolean {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "motivation")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            11,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_MOTIVATION_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🔥 Günün Zihniyet Dozu ($userName)")
            .setContentText("“$quote” — $author")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "“$quote”\n\n— $author\n\nZihnini topla, bahaneleri sustur ve bugün hedefine doğru bir adım daha at."
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        return try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(NOTIFICATION_ID_MOTIVATION, builder.build())
            true
        } catch (e: SecurityException) {
            false
        }
    }

    fun scheduleWorkoutAlarm(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_WORKOUT_NOTIFICATION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // Eğer seçilen saat bugün için geçtiyse bir sonraki güne (yarına) planla
            if (timeInMillis <= System.currentTimeMillis() + 5000) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } catch (e2: Exception) {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scheduleMotivationAlarm(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_MOTIVATION_NOTIFICATION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            102,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // Eğer seçilen saat bugün için geçtiyse bir sonraki güne (yarına) planla
            if (timeInMillis <= System.currentTimeMillis() + 5000) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } catch (e2: Exception) {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelWorkoutAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_WORKOUT_NOTIFICATION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun cancelMotivationAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_MOTIVATION_NOTIFICATION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            102,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
