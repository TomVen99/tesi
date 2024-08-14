package com.example.smartlagoon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager
    private var imageUri: Uri? = null
    private var challengePoints = 0
    //private var isProcessing = false

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

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK ) {
                setContent {
                    val usersVm = koinViewModel<UsersViewModel>()
                    val usersState by usersVm.state.collectAsStateWithLifecycle()
                    val photosDbVm = koinViewModel<PhotosDbViewModel>()
                    val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    val sharedPreferences = context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
                    sharedPreferences.getString("username", "")?.let { Log.d("share", it) }
                    Log.d("userState", usersState.users.toString())

                    if (usersState.users.isNotEmpty()) {
                        Log.d("isProcessing", "isProcessing = true")
                        val user = requireNotNull(usersState.users.find {
                            it.username == sharedPreferences.getString("username", "")
                        })

                        LaunchedEffect(Unit) {
                            val job = launch {
                                photosDbVm.addPhoto(
                                    Photo(
                                        imageUri = imageUri.toString(),
                                        username = user.username,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                usersVm.addPoints(user.username, challengePoints)
                            }
                            Log.d("JOB", "JOB")
                            //job.cancel() // Cancel the coroutine job when the effect is no longer needed
                        }

                        scheduleNotification()

                        LaunchedEffect(Unit) {
                            val currentTime = System.currentTimeMillis()
                            val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                            photosDbVm.deleteOldPhoto(cutoff)
                        }

                        val navController = rememberNavController()
                        PhotoScreen(
                            user = user,
                            photosDbVm = photosDbVm,
                            photosDbState = photosDbState,
                            navController = navController,
                            comeFromTakePhoto = true,
                            challengePoints = challengePoints,
                        )
                    } else {
                        Log.d("if", "son qui")
                    }
                }
            } else {
                Log.e("TakePhotoActivity", "Cattura dell'immagine fallita o annullata")
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verifica se l'Intent non è nullo
        val intent: Intent? = intent
        if (intent != null) {
            // Recupera l'intero passato tramite l'Intent
            challengePoints = intent.getIntExtra("challengePoints", 0)  // 0 è il valore predefinito se l'extra non è trovato
            Log.d("Punti ricevuti intent", challengePoints.toString())
        } else {
            Log.d("Punti ricevuti intent", "Intent NULLO")
            // Gestisci il caso in cui l'Intent è nullo
        }

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
            //handlePhotoCapture()
        }
    }

    /*private fun handlePhotoCapture() {
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
                                usersVm.addPoints(user.username, challengePoints)
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
                    //scheduleNotification()
                    val navController = rememberNavController()
                    PhotoScreen(
                        user = user,
                        photosDbVm = photosDbVm,
                        photosDbState = photosDbState,
                        navController = navController,
                        comeFromTakePhoto = true,
                        challengePoints = challengePoints,
                    )
                } else {
                    Log.d("if", "son qui")
                }
            }
        } else {
            Log.e("TakePhotoActivity", "Impossibile creare l'URI per l'immagine")
        }
    }*/

    private fun handlePhotoCapture() {
        val uri = createImageUri()
        if (uri != null) {
            imageUri = uri
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cameraLauncher.launch(cameraIntent)
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
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Smartlagoon_Photo_$timeStamp.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    /*private fun createImageUri(): Uri? {
        val resolver = contentResolver
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        var fileName = "Smartlagoon_Photo_$timeStamp.jpg"
        var uri: Uri? = null
        var counter = 1

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        while (uri == null) {
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

            try {
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            } catch (e: IllegalStateException) {
                // Se fallisce la creazione del file, proviamo con un altro nome
                fileName = "Smartlagoon_Photo_${timeStamp}_$counter.jpg"
                counter++
                Log.e("CreateImageUri", "Failed to create URI, trying with a new name: $fileName")
            }
        }

        return uri
    }*/



    private fun scheduleNotification() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES) // Ritardo di 24 ore
            .build()

        //Log.d("worker", "notifica impostata")
        WorkManager.getInstance(applicationContext).enqueue(notificationWorkRequest)
    }
}