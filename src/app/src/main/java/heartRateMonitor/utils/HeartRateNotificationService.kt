package heartRateMonitor.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import dagger.hilt.android.AndroidEntryPoint
import heartRateMonitor.data.repository.HeartRateRepository
import heartRateMonitor.viewmodel.HeartRateViewModel
import heartRateMonitor.viewmodel.HeartRateViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

// CLASS THAT CONTROLS THE HEART RATE SENSORS, SAVES THE HEART RATE DATA AND SENDS NOTIFICATIONS
@AndroidEntryPoint
class HeartRateNotificationService : Service(), SensorEventListener {

    private var heartRateSensor: Sensor? = null
    private lateinit var notificationHelper: NotificationHelper
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var currentHeartRate = 0
    override fun onBind(intent: Intent?): IBinder? = null
    private lateinit var movementTracker: MovementTracker
    private var lastStepCounterValue = 0
    private var restingHeartRate = false

    @Inject
    lateinit var heartRateRepository: HeartRateRepository
    private lateinit var heartRateViewModel: HeartRateViewModel
    private val viewModelStore = ViewModelStore()
    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        val factory = HeartRateViewModelFactory(heartRateRepository)
        heartRateViewModel =
            ViewModelProvider(viewModelStore, factory)[HeartRateViewModel::class.java]

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        movementTracker = MovementTracker(sensorManager)
        movementTracker.startCounting()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            NotificationHelper.HEART_RATE_SERVICE_NOTIFICATION_ID,
            notificationHelper.createServiceNotification()
        )

        if (intent == null) {
            Log.d("HeartRateNotification", "Service restarted after being killed")
        }
        startHeartRateCheck()
        return START_STICKY
    }

    private fun startHeartRateCheck() {
        job = coroutineScope.launch {
            var lastStepCountChangeTime = System.currentTimeMillis()

            while (isActive) {
                if (lastStepCounterValue != movementTracker.getStepCount()) {
                    val currentStepCount = movementTracker.getStepCount()
                    lastStepCounterValue = currentStepCount
                    lastStepCountChangeTime = System.currentTimeMillis()
                    restingHeartRate = false
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastStepCountChangeTime >= 5 * 60 * 1000) {
                        restingHeartRate = true
                    }
                }
                if (currentHeartRate > 0) {
                    heartRateViewModel.insertHeartRate(currentHeartRate, restingHeartRate)
                }
                if (currentHeartRate > 100) {
                    notificationHelper.checkAndSendHeartRateAlertNotification(currentHeartRate)
                }

                delay(10000)
            }
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            val heartRateValue = event.values[0]
            currentHeartRate = heartRateValue.toInt()
            Log.d("HeartRateNotification", "Heart rate changed: $currentHeartRate")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
        job?.cancel()
        viewModelStore.clear()

        val broadcastIntent = Intent()
        broadcastIntent.action = "restart_service"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("HeartRateNotification", "App removed from recent apps, service might be killed")

        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startForegroundService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }
}
