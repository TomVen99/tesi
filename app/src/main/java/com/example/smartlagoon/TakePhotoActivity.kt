package com.example.smartlagoon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.data.database.Photo
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import com.example.smartlagoon.utils.NotificationWorker
import com.example.smartlagoon.utils.PermissionsManager
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager
    private var imageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")
                handlePhotoCapture()
            } else {
                Log.d("PhotoPermissionTag", "Autorizzazione NON concessa")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkAndRequestPermissionPhoto(
                onPermissionGranted = { handlePhotoCapture() },
                onPermissionDenied = {
                    Log.d("PhotoPermissionTag", "Autorizzazione NON concessa2")
                }
            )
        } else {
            // Nessuna autorizzazione aggiuntiva necessaria
        }
    }

    private fun handlePhotoCapture() {
        val uri = createImageUri()
        if (uri != null) {
            imageUri = uri
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            setContent {
                val usersVm = koinViewModel<UsersViewModel>()
                val usersState by usersVm.state.collectAsStateWithLifecycle()
                val photosDbVm = koinViewModel<PhotosDbViewModel>()
                val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
                val context = LocalContext.current
                val sharedPreferences =
                    context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
                sharedPreferences.getString("username", "")?.let { Log.d("share", it) }
                Log.d("userState", usersState.users.toString())
                if (usersState.users.isNotEmpty()){
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", "")
                    })

                    val imagePickerLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                            if (result.resultCode == RESULT_OK) {
                                result.data?.data?.let { uri ->
                                    imageUri = uri
                                    photosDbVm.addPhoto(
                                        Photo(
                                            imageUri = imageUri.toString(),
                                            username = user.username,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                } ?: run {
                                    imageUri?.let { uri ->
                                        photosDbVm.addPhoto(
                                            Photo(
                                                imageUri = uri.toString(),
                                                username = user.username,//userId = sharedPreferences.getInt("userId", 0),
                                                timestamp = System.currentTimeMillis()
                                            )
                                        )
                                    }
                                }
                                scheduleNotification()
                            }
                        }
                    LaunchedEffect(Unit) {
                        imagePickerLauncher.launch(cameraIntent)
                    }
                    LaunchedEffect(Unit) {
                        val currentTime = System.currentTimeMillis()
                        val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                        photosDbVm.deleteOldPhoto(cutoff)
                    }
                    scheduleNotification()
                    val navController = rememberNavController()
                    PhotoScreen(
                        user = user,
                        photosDbVm = photosDbVm,
                        photosDbState = photosDbState,
                        navController = navController
                    )
                } else {
                    Log.d("if", "son qui")
                }
            }
        } else {
            Log.e("TakePhotoActivity", "Impossibile creare l'URI per l'immagine")
        }
    }

    /*private fun scheduleNotification() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }*/

    private fun createImageUri(): Uri? {
        val resolver = contentResolver
        val contentValues = ContentValues().apply {
            /*put(MediaStore.MediaColumns.DISPLAY_NAME, "profile_picture.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")*/
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
    private fun scheduleNotification() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES) // Ritardo di 24 ore
            .build()

        //Log.d("worker", "notifica impostata")
        WorkManager.getInstance(applicationContext).enqueue(notificationWorkRequest)
    }
}