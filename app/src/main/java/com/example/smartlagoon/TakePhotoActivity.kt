package com.example.smartlagoon

import android.annotation.SuppressLint
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
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import com.example.smartlagoon.data.database.UserChallenge
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.UserChallengeViewModel

class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager
    private var imageUri: Uri? = null
    private var challengePoints = 0
    private var challengeId = 0
    private val snackbarHostState = SnackbarHostState()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")
                handlePhotoCapture()
            } else {
                Toast.makeText(this, "Permesso non concesso", Toast.LENGTH_SHORT)
                    .show()
                Log.e("PhotoPermissionTag", "Autorizzazione NON concessa")
                //ShowPermissionDeniedSnackbar()
                //startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    /*private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")
                handlePhotoCapture()
            } else {
                Log.e("PhotoPermissionTag", "Autorizzazione NON concessa")
                showPermissionDeniedSnackbar()
            }
        }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setContent {
                    /*MyContent(
                        imageUri = imageUri,
                        challengePoints = challengePoints,
                        snackbarHostState = snackbarHostState,
                    )*/
                }
            } else {
                Log.e("TakePhotoActivity", "Cattura dell'immagine fallita o annullata")
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera i dati dall'intent
        val intent: Intent? = intent
        challengePoints = intent?.getIntExtra("challengePoints", 0) ?: 0
        Log.d("Punti ricevuti intent", challengePoints.toString())

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkAndRequestPermissionPhoto(
                onPermissionGranted = {
                    handlePhotoCapture()
                    Log.d("PhotoPermissionTag", "Autorizzazione concessa2")
                },
                onPermissionDenied = {
                    Log.d("PhotoPermissionTag", "Autorizzazione NON concessa2")
                    showPermissionDeniedSnackbar()
                }
            )
        } else {
            handlePhotoCapture()
        }

        setContent {
            MyContent(
                imageUri = imageUri,
                challengePoints = challengePoints,
                snackbarHostState = snackbarHostState,
            )
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MyContent(
        imageUri: Uri?,
        challengePoints: Int,
        snackbarHostState: SnackbarHostState,
    ) {
        val usersVm = koinViewModel<UsersViewModel>()
        val usersState by usersVm.state.collectAsStateWithLifecycle()
        val photosDbVm = koinViewModel<PhotosDbViewModel>()
        val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
        val navController = rememberNavController()

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            if (usersState.users.isNotEmpty()) {
                val user = requireNotNull(usersState.users.find {
                    it.username == sharedPreferences.getString("username", "")
                })

                // Gestione dell'inserimento foto e aggiornamento punteggi
                LaunchedEffect(Unit) {
                    launch {
                        photosDbVm.addPhoto(
                            Photo(
                                imageUri = imageUri.toString(),
                                username = user.username,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        usersVm.addPoints(user.username, challengePoints)
                    }

                    // Eliminazione delle foto vecchie
                    val currentTime = System.currentTimeMillis()
                    val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                    photosDbVm.deleteOldPhoto(cutoff)
                }

                // Visualizzazione della schermata della foto
                PhotoScreen(
                    user = user,
                    photosDbVm = photosDbVm,
                    photosDbState = photosDbState,
                    navController = navController,
                    comeFromTakePhoto = true,
                    challengePoints = challengePoints,
                )
            } else {
                // Qui puoi gestire cosa mostrare se `usersState.users` è vuoto
                Log.e("userState", "usersState vuoto")
            }
        }
    }

    */
/*    @Composable
    private fun ShowPermissionDeniedSnackbar() {

        // Lancia uno Snackbar usando un Composable
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                message = "Permesso non concesso",
                actionLabel = "Impostazioni"
            ).also { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    // Apri le impostazioni dell'applicazione
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            }
        }
    }*/

     private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK ) {
                setContent {
                    val usersVm = koinViewModel<UsersViewModel>()
                    val usersState by usersVm.state.collectAsStateWithLifecycle()
                    val photosDbVm = koinViewModel<PhotosDbViewModel>()
                    val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
                    val usersChallengeVm = koinViewModel<UserChallengeViewModel>()
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
                            if (challengeId != 0) {
                                usersChallengeVm.insertChallengeDone(
                                    UserChallenge(
                                        username = user.username,
                                        challengeId = challengeId
                                    )
                                )
                            } else {
                                Log.e("challengeId", "challengeId = " + challengeId)
                            }

                        }

                        val navController = rememberNavController()
                        PhotoScreen(
                            //user = user,
                            usersViewModel = usersVm,
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

    private fun scheduleNotification() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES) // Ritardo di 24 ore
            .build()

        //Log.d("worker", "notifica impostata")
        WorkManager.getInstance(applicationContext).enqueue(notificationWorkRequest)
    }
}