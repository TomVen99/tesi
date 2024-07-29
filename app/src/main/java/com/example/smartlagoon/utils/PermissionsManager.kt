package com.example.smartlagoon.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(
    //registry: ActivityResultRegistry,
    //private val locationProvider: LocationProvider,
    private val context: Context,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestPermissionNotification(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // L'autorizzazione è già stata concessa
                onPermissionGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.POST_NOTIFICATIONS) -> {
                // Mostra una spiegazione all'utente, poi richiedi l'autorizzazione
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            else -> {
                // Richiedi direttamente l'autorizzazione
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun checkAndRequestPermissionPhoto(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // L'autorizzazione è già stata concessa
                onPermissionGranted()
            }

            /*ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED -> {
                // L'autorizzazione è già stata concessa
                onPermissionDenied()
            }*/

            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.POST_NOTIFICATIONS) -> {
                // Mostra una spiegazione all'utente, poi richiedi l'autorizzazione
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // Richiedi direttamente l'autorizzazione
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


    /*
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
    }*/

}
