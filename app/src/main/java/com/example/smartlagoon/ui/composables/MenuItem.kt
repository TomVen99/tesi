package com.example.smartlagoon.ui.composables

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.smartlagoon.ui.SmartlagoonRoute

@Composable
fun MenuItem(name: String, resId: Int, route: String/*SmartlagoonRoute*/, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        //elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Imposta il colore di sfondo trasparente
        ),
        modifier = Modifier
            .size(150.dp)
            .clickable {
                navController.navigate(route)
            }
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(

            ) {
                AnimatedImage(resId)
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


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraItem(navController: NavController, size: Int) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val route = SmartlagoonRoute.Camera

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(320.dp)
            .height(size.dp)
            .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RoundedCornerShape(8.dp))
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER

                    setOnClickListener {
                        navController.navigate(route.route)

                    }
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )
                    } catch (exc: Exception) {
                        Log.e("CameraX", "Impossibile associare i use cases alla fotocamera", exc)
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SingleMenuItem(name: String, resId: Int, route: SmartlagoonRoute, navController: NavController, size: Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        //elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Imposta il colore di sfondo trasparente
        ),
        modifier = Modifier
            .width(330.dp) // Occupa tutta la larghezza
            .height(size.dp) // Altezza di due caselle
            //.clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route.route)
            }
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp),)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(

            ) {
                AnimatedImage(resId)
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
