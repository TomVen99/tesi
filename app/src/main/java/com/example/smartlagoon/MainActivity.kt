package com.example.smartlagoon

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.smartlagoon.ui.SmartlagoonNavGraph
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.utils.PermissionsManager

class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
            } else {
            }
        }

    private var startRoute = ""
    private var generateTest = true

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent? = intent
        if (intent != null) {
            startRoute = intent.getStringExtra("route").toString()
            Log.d("route ricevuta", startRoute)
        } else {
            Log.d("route ricevuta", "Intent NULLO")
        }

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkAndRequestPermissionNotification(
                onPermissionGranted = {
                                      },
                onPermissionDenied = {
                    Toast.makeText(this, "Permesso notifiche non concesso", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        } else {
        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        generateTest = sharedPreferences.getBoolean("generateTest", true)
        setContent {
            SmartlagoonTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    if(startRoute == SmartlagoonRoute.Challenge.route) {
                        Scaffold { contentPadding ->
                            SmartlagoonNavGraph(
                                navController,
                                modifier = Modifier.padding(contentPadding),
                                startDestination = SmartlagoonRoute.Challenge
                            )
                        }
                    } else {
                        Scaffold { contentPadding ->
                            SmartlagoonNavGraph(
                                navController,
                                modifier = Modifier.padding(contentPadding),
                            )
                        }
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