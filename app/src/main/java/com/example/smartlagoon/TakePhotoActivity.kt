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
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import com.example.smartlagoon.utils.NotificationWorker
import com.example.smartlagoon.utils.PermissionsManager
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.example.smartlagoon.ui.viewmodel.UserChallengeViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel

class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager
    private var imageUri: Uri? = null
    private var challengePoints = 0
    private var challengeId = 0

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")
                handlePhotoCapture()
            } else {
                Toast.makeText(this, "Permesso non concesso", Toast.LENGTH_SHORT).show()
                Log.e("PhotoPermissionTag", "Autorizzazione NON concessa")
                finish()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK ) {
                setContent {
                    val usersDbVm = koinViewModel<UsersDbViewModel>()
                    val photosDbVm = koinViewModel<PhotosDbViewModel>()

                    imageUri?.let { photosDbVm.uploadPhoto(it) }
                    usersDbVm.addPoints(challengePoints)
                    scheduleNotification()

                    LaunchedEffect(Unit) {
                        val currentTime = System.currentTimeMillis()
                        val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                        /*photosDbVm.deleteOldPhoto(cutoff)
                        if (challengeId != 0) {
                            usersChallengeVm.insertChallengeDone(
                                UserChallenge(
                                    username = user.username,
                                    challengeId = challengeId
                                )
                            )
                        } else {
                            Log.e("challengeId", "challengeId = " + challengeId)
                        }*/

                    }

                    val navController = rememberNavController()
                    PhotoScreen(
                        usersDbVm = usersDbVm,
                        photosDbVm = photosDbVm,
                        navController = navController,
                        comeFromTakePhoto = true,
                        challengePoints = challengePoints,
                    )
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
            challengePoints = intent.getIntExtra("challengePoints", 0) // 0 è il valore predefinito se l'extra non è trovato
            challengeId = intent.getIntExtra("challengeId", 0)
            Log.d("Punti ricevuti intent", challengePoints.toString())
        } else {
            Log.d("Punti ricevuti intent", "Intent NULLO")
            // Gestisci il caso in cui l'Intent è nullo
        }

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("PhotoPermissionTag", "Tirmisu")
            permissionHelper.checkAndRequestPermissionPhoto(
                onPermissionGranted = {
                    handlePhotoCapture()
                    Log.d("PhotoPermissionTag", "Autorizzazione concessa2")
                                      },
                onPermissionDenied = {
                    Log.d("PhotoPermissionTag", "Autorizzazione NON concessa2")
                }
            )
        } else {
            // Nessuna autorizzazione aggiuntiva necessaria
            handlePhotoCapture()
        }
    }

    private fun handlePhotoCapture() {
        val uri = createImageUri()
        imageUri = uri
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        cameraLauncher.launch(cameraIntent)
    }

    /*private fun createImageUri(): Uri? {
        val resolver = contentResolver
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Smartlagoon_Photo_$timeStamp.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }*/

    private fun createImageUri(): Uri {
        val resolver = contentResolver
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    private fun scheduleNotification() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES) // Ritardo di 24 ore
            .build()

        //Log.d("worker", "notifica impostata")
        WorkManager.getInstance(applicationContext).enqueue(notificationWorkRequest)
    }
}