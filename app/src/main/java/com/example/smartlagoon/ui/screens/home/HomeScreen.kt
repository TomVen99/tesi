package com.example.smartlagoon.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    val ctx = LocalContext.current
    val cameraPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(ctx, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) -> {
                // Permission already granted, proceed normally
            }
            else -> {
                // Request camera permission
                cameraPermissionState.launch(Manifest.permission.CAMERA)
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lagoonguard_logo),
                contentDescription = "Smart Lagoon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGrid(navController)
        }
    }
}

@Composable
fun MenuGrid(navController: NavController){
    LazyColumn {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CameraItem(
                    SmartlagoonRoute.Camera,
                    navController,
                    450
                )
                /*
            MenuItem("Classifica", R.drawable.ic_classifica, SmartlagoonRoute.Ranking, navController)*/
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Foto", R.drawable.ic_badge, SmartlagoonRoute.Photo, navController)
                MenuItem("Gioca", R.drawable.ic_info, SmartlagoonRoute.Play, navController)
            }

            Spacer(modifier = Modifier.height(16.dp))
            /*Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Quiz", R.drawable.ic_ricicla, SmartlagoonRoute.Quiz, navController)
                MenuItem("Sfide", R.drawable.ic_sfide, SmartlagoonRoute.Challenge, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Profilo", R.drawable.ic_profilo, SmartlagoonRoute.Profile, navController)
                MenuItem(
                    "Classifica",
                    R.drawable.ic_classifica,
                    SmartlagoonRoute.Ranking,
                    navController
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SingleMenuItem(
                    "About",
                    R.drawable.ic_info,
                    SmartlagoonRoute.About,
                    navController,
                    80
                )
            }
            Spacer(modifier = Modifier.height(16.dp))*/
        }
    }
}

@Composable
fun MenuItem(name: String, iconId: Int, route: SmartlagoonRoute, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(150.dp)
            //.clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route.route)
            }
            .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RoundedCornerShape(8.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(

            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = name,
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        //.clip(RoundedCornerShape(8.dp))
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun CameraItem(route: SmartlagoonRoute, navController: NavController, size: Int) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(320.dp) // Occupa tutta la larghezza
            .height(size.dp) // Altezza di due caselle
            //.clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route.route)
            }
            .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RoundedCornerShape(8.dp),)
    ) {
        // Usa AndroidView per incorporare PreviewView
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                // Configura CameraX
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Imposta l'anteprima
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        // Disattiva tutti i use cases
                        cameraProvider.unbindAll()

                        // Associa i use cases alla fotocamera
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )

                    } catch (exc: Exception) {
                        Log.e("CameraX", "Impossibile associare i use cases alla fotocamera", exc)
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView // Ritorna PreviewView
            },
            modifier = Modifier.fillMaxSize() // Usa tutto lo spazio disponibile
        )
    }
}
@Composable
fun SingleMenuItem(name: String, iconId: Int, route: SmartlagoonRoute, navController: NavController, size: Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(320.dp) // Occupa tutta la larghezza
            .height(size.dp) // Altezza di due caselle
            //.clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route.route)
            }
            .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RoundedCornerShape(8.dp),)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(

            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = name,
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                    /*    .clip(RoundedCornerShape(8.dp))*/
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}