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

    val ctx = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var classificationResult by remember { mutableStateOf<Int?>(null) }
    val classifier = remember { MarineClassifier(ctx) }
    usersDbVm.currentUser.value?.email?.let { Log.d("Camera", it) }

    val imageCaptureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("test", result.resultCode.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: imageUri
            Log.e("test", uri.toString())
            uri?.let {
                it1 -> photosDbVm.uploadPhoto(it1)
                scheduleNotification(ctx)
            }
            if(challengesDbVm.currentChallenge.value != null) {
                usersDbVm.addPoints(challengesDbVm.currentChallenge.value!!.points)
                Log.d("A","CurrentRoute not nul")
                challengesDbVm.currentChallenge.value!!.id?.let { challengesDbVm.challengeDone(it) }
            }
            photosDbVm.setShowDialog(true)
            val bitmap = uri?.let { it1 -> getBitmapFromUri(ctx, it1) }
            classificationResult = bitmap?.let { classifier.classifyImage(it) }
            photosDbVm.setCategoryClassification(MarineClassifier.Categories.entries[classificationResult!!])
            navController.navigate(SmartlagoonRoute.Photo.route) {
                popUpTo(SmartlagoonRoute.Camera.route) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.navigateUp()
        }
    }

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
            Toast.makeText(ctx, "Permesso camera non concesso", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }
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
        .setInitialDelay(24, TimeUnit.HOURS) // Ritardo di 24 ore
        .build()
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
