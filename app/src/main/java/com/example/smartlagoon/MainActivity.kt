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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartlagoon.ui.SmartlagoonNavGraph
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.utils.PermissionsManager
import org.koin.androidx.compose.koinViewModel

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

    private var startRoute = ""
    private var generateTest = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent? = intent
        if (intent != null) {
            // Recupera l'intero passato tramite l'Intent
            startRoute = intent.getStringExtra("route").toString()  // 0 è il valore predefinito se l'extra non è trovato
            Log.d("route ricevuta", startRoute)
        } else {
            Log.d("route ricevuta", "Intent NULLO")
            // Gestisci il caso in cui l'Intent è nullo
        }

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkAndRequestPermissionNotification(
                onPermissionGranted = {
                    //scheduleNotifications()
                    //sendNotification(this)
                                      },
                onPermissionDenied = {
                    Toast.makeText(this, "Permesso non concesso", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        } else {
            // Per le versioni di Android precedenti, non è necessaria alcuna autorizzazione aggiuntiva
            //scheduleNotifications()
            //sendNotification(this)
        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        generateTest = sharedPreferences.getBoolean("generateTest", true)
        setContent {
            SmartlagoonTheme(/*darkTheme = theme == "Dark"*/) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()

                    /*// Se route è null o vuota, naviga verso la schermata di login
                    LaunchedEffect(route) {
                        val startRoute = route.ifEmpty {
                            SmartlagoonRoute.Login.route
                        }

                        navController.navigate(startRoute) {
                            // Pulisci il back stack e naviga alla schermata specificata
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }*/

                    /*SmartlagoonRoute.routes.find {
                        it.route == backStackEntry?.destination?.route
                    } ?: SmartlagoonRoute.Login*/
                    /*var startRoute = ""
                    if(route != "") {
                        startRoute = SmartlagoonRoute.Challenge
                    } else {
                        startRoute = SmartlagoonRoute.Login
                    }*/
                    if(generateTest) {
                        val challengeDbVm = koinViewModel<ChallengesDbViewModel>()
                        challengeDbVm.createChallangeTest()
                        Log.e("generateTest", "generateTest Main")
                        generateTest = false
                        sharedPreferences.edit().putBoolean("generateTest", false).apply()
                    }
                    Log.d("start route", startRoute)
                    /*SmartlagoonRoute.routes.find {
                        it.route == startRoute//backStackEntry?.destination?.route
                    } ?: SmartlagoonRoute.Login*/
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