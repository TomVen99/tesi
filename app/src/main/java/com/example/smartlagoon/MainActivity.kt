package com.example.smartlagoon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.ui.SmartlagoonNavGraph
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.screens.addtrack.ShowDialog
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.utils.NotificationWorker
import com.example.smartlagoon.utils.PermissionsManager
import com.example.smartlagoon.utils.sendNotifications
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // L'autorizzazione è stata concessa, puoi schedulare le notifiche
                //scheduleNotifications()
            } else {
                // L'autorizzazione è stata negata, gestisci di conseguenza
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkAndRequestPermissionNotification(
                onPermissionGranted = {
                    //scheduleNotifications()
                    //sendNotification(this)
                                      },
                onPermissionDenied = {

                }
            )
        } else {
            // Per le versioni di Android precedenti, non è necessaria alcuna autorizzazione aggiuntiva
            //scheduleNotifications()
            //sendNotification(this)
        }

        setContent {
            SmartlagoonTheme(/*darkTheme = theme == "Dark"*/) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    SmartlagoonRoute.routes.find {
                        it.route == backStackEntry?.destination?.route
                    } ?: SmartlagoonRoute.Login
                    Scaffold{ contentPadding ->
                        SmartlagoonNavGraph(
                            navController,
                            modifier = Modifier.padding(contentPadding),
                        )
                    }
                }
            }
        }
    }

    private fun sendNotification(context: Context) {
        createNotificationChannel()
        // Codice per inviare la notifica (vedi sotto)
        sendNotifications(context)
        Log.d("notifiche", "notifica inviata")
    }


    private fun createNotificationChannel() {
        val name = "Channel Name"
        val descriptionText = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
        //settingsViewModel.resetTheme()
        //locationService = get<LocationService>()

        /*createNotificationChannel(this)
        scheduleAlarm(this)*/

    private fun scheduleNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}