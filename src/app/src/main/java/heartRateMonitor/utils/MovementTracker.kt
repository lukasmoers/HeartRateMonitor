package heartRateMonitor.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

// CLASS THAT COUNTS THE NUMBER OF STEPS TAKEN BY THE USER

class MovementTracker(private val sensorManager: SensorManager) : SensorEventListener {
    private var stepSensor: Sensor? = null
    private var stepCount = 0

    init {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun startCounting() {
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopCounting() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            stepCount = event.values[0].toInt()
            Log.d("StepCounter", "Current step count: $stepCount")
        }
    }

    fun getStepCount(): Int {
        return stepCount
    }
}
