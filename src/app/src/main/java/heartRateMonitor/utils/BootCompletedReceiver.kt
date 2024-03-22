package heartRateMonitor.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// CLASS TO START THE FOREGROUND SERVICE WHEN THE DEVICE BOOTS
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val serviceIntent = Intent(context, HeartRateNotificationService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}