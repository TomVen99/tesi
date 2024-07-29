package com.example.smartlagoon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartlagoon.ui.SmartlagoonNavGraph
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.utils.sendNotifications

class MainActivity : ComponentActivity() {

    //private lateinit var locationService: LocationService
    //private val settingsViewModel: SettingsViewModel by viewModel()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // L'autorizzazione è stata concessa, puoi inviare notifiche
                sendNotification(this)
            } else {
                // L'autorizzazione è stata negata, gestisci di conseguenza
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    "android.permission.POST_NOTIFICATIONS"
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // L'autorizzazione è già stata concessa
                    sendNotification(this)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.POST_NOTIFICATIONS") -> {
                    // Mostra una spiegazione all'utente, poi richiedi l'autorizzazione
                    requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
                else -> {
                    // Richiedi direttamente l'autorizzazione
                    requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
            }
        } else {
            // Per le versioni di Android precedenti, non è necessaria alcuna autorizzazione aggiuntiva
            sendNotification(this)
        }
    }

    private fun sendNotification(context: Context) {
        createNotificationChannel()
        // Codice per inviare la notifica (vedi sotto)
        sendNotifications(context)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        setContent {
            //val theme by settingsViewModel.theme.collectAsState(initial = "")
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