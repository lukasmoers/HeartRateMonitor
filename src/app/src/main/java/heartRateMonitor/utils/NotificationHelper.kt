package heartRateMonitor.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android.wearable.composeforwearos.R
import heartRateMonitor.ui.views.MainActivity

// CLASS THAT CREATES THE NOTIFICATION CHANNELS AND SENDS NOTIFICATIONS WHEN CALLED

class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)
    private val notificationInterval = 60 * 60 * 1000
    private var lastNotificationTime: Long = 0

    init {
        createNotificationChannel(CHANNEL_ID_SERVICE, "Heart Rate Service Channel")
        createNotificationChannel(CHANNEL_ID_ALERT, "Heart Rate Alert Channel")
    }

    fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID_SERVICE)
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring heart rate in background...")
            .setSmallIcon(R.drawable.ic_notifications).build()
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 500)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun checkAndSendHeartRateAlertNotification(heartRate: Int) {
        val currentTime = System.currentTimeMillis()
        val context = context.applicationContext
        if (currentTime - lastNotificationTime >= notificationInterval) {
            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_ALERT)
                .setContentTitle("High Heart Rate Alert")
                .setContentText("Your heart rate is $heartRate! Add your event!")
                .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(0, 1000, 500, 1000))
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notifications).build()
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(HEART_RATE_ALERT_NOTIFICATION_ID, notification)
            lastNotificationTime = currentTime
        }
    }

    companion object {
        const val CHANNEL_ID_SERVICE = "service_channel_id"
        const val CHANNEL_ID_ALERT = "alert_channel_id"
        const val HEART_RATE_ALERT_NOTIFICATION_ID = 2
        const val HEART_RATE_SERVICE_NOTIFICATION_ID = 1
    }
}