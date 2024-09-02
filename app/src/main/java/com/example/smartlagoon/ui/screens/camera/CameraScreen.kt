package com.example.smartlagoon.ui.screens.camera

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import com.example.smartlagoon.utils.NotificationWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

@Composable
fun CameraScreen(
    navController: NavController,
    photosDbVm: PhotosDbViewModel,
    challengesDbVm: ChallengesDbViewModel,
    usersDbVm: UsersDbViewModel,
) {
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(ctx) }
    val previewView = remember { PreviewView(ctx) }
    val imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    val cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val cameraControl = remember { mutableStateOf<CameraControl?>(null) }
    val isFlashEnabled = remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val currentChallenge by challengesDbVm.currentChallenge.observeAsState()

    Log.d("currentChallengeCamera", currentChallenge.toString())
    Box(modifier = Modifier.fillMaxSize()) {
        // Anteprima della fotocamera
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Configura CameraX
        LaunchedEffect(cameraProviderFuture) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCaptureConfig = ImageCapture.Builder().build()
            imageCapture.value = imageCaptureConfig

            try {
                // Disattiva tutti i use cases
                cameraProvider.unbindAll()

                // Associa i use cases alla fotocamera
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector.value,
                    preview,
                    imageCaptureConfig
                )

                // Ottieni il controllo della fotocamera per gestire zoom e flash
                cameraControl.value = camera.cameraControl

            } catch (exc: Exception) {
                Log.e("CameraX", "Impossibile associare i use cases alla fotocamera", exc)
            }
        }

        // Pulsante per scattare la foto
        Button(
            onClick = {
                isPressed = true // Cambia lo stato del bottone

                // Avvia una coroutine per gestire il delay
                coroutineScope.launch {
                    // Utilizza la funzione takePhoto modificata
                    takePhoto(ctx, imageCapture.value) { uri ->
                        if (uri != null) {
                            photosDbVm.uploadPhoto(uri)
                        } else {
                            Log.e(
                                "CameraScreen",
                                "Errore: URI della foto è nullo. Foto non caricata."
                            )
                        }
                    }
                    delay(500L) // Delay di 1 secondo (1000 millisecondi)
                    isPressed = false // Ripristina lo stato del bottone
                }
                Log.d("caricamentoFoto","aaa")
                if(currentChallenge != null) {
                    currentChallenge?.let { usersDbVm.addPoints(it.points) }
                    currentChallenge?.id?.let { challengesDbVm.challengeDone(it) }
                    photosDbVm.setShowDialog(true)
                    navController.navigate(SmartlagoonRoute.Photo.route)
                }else {
                    navController.navigateUp() // Torna indietro dopo il delay
                }
                scheduleNotification(ctx)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .size(70.dp) // Dimensione per creare una forma tonda
                .clip(CircleShape), // Clip a forma circolare
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPressed) Color.Red else Color.White, // Colore del bottone basato sullo stato
                contentColor = Color.Transparent // Nessun colore del contenuto (per l'icona)
            )
        ) {

        }

        // Pulsante per commutare la fotocamera
        IconButton(
            onClick = {
                cameraSelector.value = if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraAndroid,
                contentDescription = "Cambia Fotocamera",
                tint = Color.White
            )
        }

        // Pulsante per abilitare/disabilitare il flash
        IconButton(
            onClick = {
                isFlashEnabled.value = !isFlashEnabled.value
                cameraControl.value?.enableTorch(isFlashEnabled.value)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),

        ) {
            Icon(
                imageVector = if (isFlashEnabled.value) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = "Controllo Flash",
                tint = Color.White
            )
        }
    }
}

private fun takePhoto(context: Context, imageCapture: ImageCapture?, onPhotoTaken: (Uri?) -> Unit) {
    // Crea un file temporaneo per salvare la foto
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Errore durante lo scatto della foto: ${exception.message}", exception)
                onPhotoTaken(null) // Chiamata di callback con URI nullo in caso di errore
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("CameraX", "Foto salvata con successo: ${photoFile.absolutePath}")

                // Verifica se il file esiste prima di procedere
                if (photoFile.exists()) {
                    val uri = photoFile.toUri()
                    Toast.makeText(context, "Foto caricata!", Toast.LENGTH_SHORT).show()
                    onPhotoTaken(uri) // Chiamata di callback con URI valido
                } else {
                    Log.e("CameraX", "Il file della foto non esiste!")
                    onPhotoTaken(null) // Chiamata di callback con URI nullo
                }
            }
        }
    )
}

private fun scheduleNotification(ctx: Context) {
    val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(1, TimeUnit.MINUTES) // Ritardo di 24 ore
        .build()

    //Log.d("worker", "notifica impostata")
    WorkManager.getInstance(ctx).enqueue(notificationWorkRequest)
}
