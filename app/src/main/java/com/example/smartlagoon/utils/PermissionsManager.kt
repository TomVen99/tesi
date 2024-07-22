package com.example.outdoorromagna.utils

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.MutableLiveData

class PermissionsManager(
    registry: ActivityResultRegistry,
    private val locationProvider: LocationProvider,
    private val stepCounter: StepCounter
) {

    val locationPermissionDenied = MutableLiveData<Boolean>()
    val activityRecognitionPermissionDenied = MutableLiveData<Boolean>()

    private val locationPermissionProvider =
        registry.register("locationPermission", ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                locationProvider.getUserLocation()
            } else {
                locationPermissionDenied.value = true
            }
        }

    private val activityRecognitionPermissionProvider =
        registry.register("activityRecognitionPermission", ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                stepCounter.setupStepCounter()
            } else {
                activityRecognitionPermissionDenied.value = true
            }
        }

    fun requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun requestActivityRecognition() {
        Log.d("TAG", "Log: " + Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityRecognitionPermissionProvider.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            Log.d("TAG", "Log: setupStepCounter")
            stepCounter.setupStepCounter()
        }
    }
}
