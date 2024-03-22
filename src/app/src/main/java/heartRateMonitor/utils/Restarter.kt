package heartRateMonitor.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

// CLASS THAT RESTARTS THE SERVICE WHEN THE APP IS CLOSED

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                Intent(
                    context, HeartRateNotificationService::class.java
                )
            )
        } else {
            context.startService(Intent(context, HeartRateNotificationService::class.java))
        }
    }
}