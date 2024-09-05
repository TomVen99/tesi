package com.example.smartlagoon.ui.screens.camera

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import com.example.smartlagoon.utils.MarineClassifier
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.smartlagoon.utils.NotificationWorker
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraScreen(
    navController: NavController,
    photosDbVm: PhotosDbViewModel,
    challengesDbVm: ChallengesDbViewModel,
    usersDbVm: UsersDbViewModel,
) {
    Log.d("NOONOOSONDNIVI", "aaaaaaaaaaaaaaa")
    val ctx = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var classificationResult by remember { mutableStateOf<Int?>(null) }
    val classifier = remember { MarineClassifier(ctx) }
    usersDbVm.currentUser.value?.email?.let { Log.d("Camera", it) }

    // Launcher per gestire il risultato dell'attività della fotocamera
    val imageCaptureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("test", result.resultCode.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: imageUri
            Log.e("test", uri.toString())
            uri?.let {
                it1 -> photosDbVm.uploadPhoto(it1)
            }
            if(challengesDbVm.currentChallenge.value != null) {
                usersDbVm.addPoints(challengesDbVm.currentChallenge.value!!.points)
                Log.d("A","CurrentRoute not nul")
                challengesDbVm.currentChallenge.value!!.id?.let { challengesDbVm.challengeDone(it) }
                scheduleNotification(ctx)
            }
            photosDbVm.setShowDialog(true)
            val bitmap = uri?.let { it1 -> getBitmapFromUri(ctx, it1) }
            classificationResult = bitmap?.let { classifier.classifyImage(it) }
            photosDbVm.setCategoryClassification(MarineClassifier.Categories.entries[classificationResult!!])
            navController.navigate(SmartlagoonRoute.Photo.route) {
                // Rimuove ScreenA dalla pila
                popUpTo(SmartlagoonRoute.Camera.route) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.navigateUp()
        }
    }

    // Launcher per la richiesta del permesso della fotocamera
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imageUri = createImageUri(ctx)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }

            imageCaptureLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(ctx, "Permesso non concesso", Toast.LENGTH_SHORT).show()
        }
    }

    // Avvia automaticamente l'intent della fotocamera se i permessi sono concessi
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}
private fun createImageUri(context: Context): Uri {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }
    return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}

private fun scheduleNotification(ctx: Context) {
    val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(1, TimeUnit.MINUTES) // Ritardo di 24 ore
        .build()

    //Log.d("worker", "notifica impostata")
    WorkManager.getInstance(ctx).enqueue(notificationWorkRequest)
}

@RequiresApi(Build.VERSION_CODES.P)
private fun getBitmapFromUri(ctx: Context, uri: Uri): Bitmap? {
    return try {
        val source = ImageDecoder.createSource(ctx.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
    /*val ctx = LocalContext.current
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
    var classificationResult by remember { mutableStateOf<Int?>(null) }
    val classifier = remember { MarineClassifier(ctx) }

    // Retrieve camera IDs
    LaunchedEffect(Unit) {
        getCameraIds(ctx)
    }

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
                    CameraSelector.DEFAULT_BACK_CAMERA,//cameraSelector.value,
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
                    takePhoto(ctx, imageCapture.value)  { uri ->
                        if (uri != null) {
                            photosDbVm.uploadPhoto(uri)
                            val bitmap = getBitmapFromUri(ctx, uri)
                            classificationResult = bitmap?.let { classifier.classifyImage(it) }
                        } else {
                            Log.e("CameraScreen","Errore: URI della foto è nullo. Foto non caricata.")
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
                    photosDbVm.setCategoryClassification(MarineClassifier.Categories.entries[classificationResult!!])
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

// Decode bitmap using ImageDecoder


private fun getCameraIds(ctx: Context) {
    val cameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraIds = cameraManager.cameraIdList
    cameraIds.forEach {
        println("Camera ID: $it")
    }
}

private fun takePhoto(context: Context, imageCapture: ImageCapture?, onPhotoTaken: (Uri?) -> Unit) {
    if (imageCapture == null) {
        Log.e("CameraX", "Errore: ImageCapture non è pronto.")
        onPhotoTaken(null)
        return
    }else {
        Log.e("CameraX", "NON NULLO")
    }

    // Crea un file temporaneo per salvare la foto
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
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
}*/
