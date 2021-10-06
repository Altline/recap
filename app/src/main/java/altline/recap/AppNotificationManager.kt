package altline.recap

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNotificationManager @Inject constructor() {

    lateinit var context: Context

    companion object {
        const val CHANNEL_ID_RECAP_NUDGE = "recapNudge"
    }

    fun createRecapNudgeChannel() {
        val name = context.getString(R.string.channel_name_recapNudge)
        val descriptionText = context.getString(R.string.channel_desc_recapNudge)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID_RECAP_NUDGE, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleRecapNudge() {
        val intent = Intent(context, RecapNudgeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, 0
        )

        val triggerTime = Calendar.getInstance().run {
            timeInMillis = System.currentTimeMillis()
            if (get(Calendar.HOUR_OF_DAY) >= 21) add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            timeInMillis
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
            AlarmManager.RTC,
            triggerTime,
            pendingIntent
        )
    }

    fun showRecapNudge(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = Notification.Builder(context, CHANNEL_ID_RECAP_NUDGE)
            .setSmallIcon(R.drawable.ic_recap_logo)
            .setContentTitle(context.getString(R.string.notif_title_recapNudge))
            .setContentText(context.getString(R.string.notif_text_recapNudge))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(0, notification)
        }
    }
}

@AndroidEntryPoint
class RecapNudgeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager.showRecapNudge(context)
        notificationManager.scheduleRecapNudge()
    }
}

@AndroidEntryPoint
class DeviceBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            notificationManager.scheduleRecapNudge()
        }
    }

}