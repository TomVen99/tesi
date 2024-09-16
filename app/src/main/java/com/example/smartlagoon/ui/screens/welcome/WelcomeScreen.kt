package com.example.smartlagoon.ui.screens.welcome

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.AnimatedImage
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    usersDbVm: UsersDbViewModel,
) {
    val ctx = LocalContext.current

    // Launcher per richiedere il permesso alla fotocamera
    val cameraPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            navController.navigate(SmartlagoonRoute.Home.route)
            Toast.makeText(ctx, "Il permesso alla fotocamera è necessario per scattare le foto", Toast.LENGTH_SHORT)
                .show()
        } else {
            navController.navigate(SmartlagoonRoute.Home.route)
            //entrambi i permessi sono concessi
        }
    }

    val notificationPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Se il permesso per le notifiche è concesso, richiedi quello per la fotocamera
            cameraPermissionState.launch(Manifest.permission.CAMERA)
        } else {
            Toast.makeText(ctx,"Il permesso per le notifiche è necessario per rimanere aggiornati",Toast.LENGTH_SHORT)
                .show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedImage(R.raw.sea_background)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lagoonguard_logo_nosfondo),
                contentDescription = "Smart Lagoon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cos'è LagoonGuard?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "LagoonGuard è un app che ti permette di interagire con la laguna del Mar Menor creando contenuti digitali.",
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Gioca con noi!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Con LagoonGuard potrai affrontare quiz che ti aiuteranno a salvaguardare il Mar Menor!\n Tramite le notifiche potremo rimanere in contatto e affrontare tante nuove sfide!\n Per viviere un'esperienza completa, accetta i permessi per le notifiche e la fotocamera.",
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                usersDbVm.setShowWelcome(false)
                notificationPermissionState.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
                colors = myButtonColors()
            ) {
                Text("Inizia")
            }
        }
    }
}