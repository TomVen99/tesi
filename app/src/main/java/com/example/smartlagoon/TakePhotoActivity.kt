package com.example.smartlagoon

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.smartlagoon.utils.PermissionsManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/*class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // L'autorizzazione è stata concessa, puoi aprire la fotocamera
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")
                openCamera()
            } else {
                // L'autorizzazione è stata negata, gestisci di conseguenza
                Log.d("PhotoPermissionTag", "Autorizzazione NON concessa")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                // La foto è stata catturata correttamente, gestisci la foto qui
                Log.d("PhotoPermissionTag", "Foto catturata con successo: $photoUri")
            } else {
                // La foto non è stata catturata, gestisci di conseguenza
                Log.d("PhotoPermissionTag", "Foto NON catturata")
            }
        }

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("PhotoPermissionTag", "son qua")
            permissionHelper.checkAndRequestPermissionPhoto(
                onPermissionGranted = {
                    Log.d("PhotoPermissionTag", "Autorizzazione concessa2")
                    openCamera()
                },
                onPermissionDenied = {
                    Log.d("PhotoPermissionTag", "Autorizzazione NON concessa2")
                }
            )
        } else {
            // Per le versioni di Android precedenti, non è necessaria alcuna autorizzazione aggiuntiva
        }
    }

    private fun openCamera() {
        try {
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", photoFile)
            takePictureLauncher.launch(photoUri!!)
        } catch (ex: IOException) {
            Log.e("PhotoPermissionTag", "Errore durante la creazione del file immagine", ex)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Crea un file immagine con un nome univoco
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Salva un percorso per essere usato con l'intent ACTION_VIEW
            photoUri = Uri.fromFile(this)
        }
    }
}*/

class TakePhotoActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionsManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // L'autorizzazione è stata concessa, puoi schedulare le notifiche
                Log.d("PhotoPermissionTag", "Autorizzazione concessa")

            } else {
                // L'autorizzazione è stata negata, gestisci di conseguenza
                Log.d("PhotoPermissionTag", "Autorizzazione NON concessa")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionsManager(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("PhotoPermissionTag", "son qua")
            permissionHelper.checkAndRequestPermissionPhoto(
                onPermissionGranted = {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(cameraIntent)
                    finish()

                },
                onPermissionDenied = {
                    Log.d("PhotoPermissionTag", "Autorizzazione NON concessa2")
                }
            )
        } else {
            // Per le versioni di Android precedenti, non è necessaria alcuna autorizzazione aggiuntiva
        }
    }
}