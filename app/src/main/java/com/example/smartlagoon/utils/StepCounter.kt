package com.example.outdoorromagna.utils

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_FASTEST
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

class StepCounter(private val context: Context) : SensorEventListener {

    val liveSteps = MutableLiveData<Int>()

    private val sensorManager by lazy {
        context.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private val stepCounterSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private var initialSteps = -1

    override fun onSensorChanged(event: SensorEvent) {
        event.values.firstOrNull()?.toInt()?.let { newSteps ->
            if (initialSteps == -1) {
                initialSteps = newSteps
            }

            val currentSteps = newSteps - initialSteps

            liveSteps.value = currentSteps
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit

    fun setupStepCounter() {
        Log.d("TAG", "start step counter")
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SENSOR_DELAY_FASTEST)
        }
    }

    fun unloadStepCounter() {
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this)
        }
    }

}