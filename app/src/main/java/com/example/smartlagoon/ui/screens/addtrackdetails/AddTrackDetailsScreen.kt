package com.example.smartlagoon.ui.screens.addtrackdetails

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.data.database.Track
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
import com.example.smartlagoon.ui.screens.addtrack.AddTrackState

@Composable
fun AddTrackDetailsScreen(
    navController: NavController,
    addTrackDetailsState: AddTrackDetailsState,
    addTrackDetailsActions: AddTrackDetailsActions,
    user: User,
    addTrackState: AddTrackState,
    tracksDbVm: TracksDbViewModel
){
    val ctx = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var userImage = user.urlProfilePicture

    fun createImageUri(): Uri {
        val resolver = ctx.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "profile_picture.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    //tracksDbVm.updateProfileImg(user.username, uri.toString())
                }
            }
        }

    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val uri = createImageUri()
                imageUri = uri
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, uri)
                }
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

                imagePickerLauncher.launch(chooserIntent)
            } else {
                Toast.makeText(ctx, "Permesso non concesso", Toast.LENGTH_SHORT).show()
            }
        }

    Scaffold { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = addTrackDetailsState.title,
                onValueChange = addTrackDetailsActions::setTitle,
                label = { Text("Titolo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = addTrackDetailsState.description,
                onValueChange = addTrackDetailsActions::setDescription,
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                    //takePicture()
                },
            ) {
                Icon(
                    Icons.Filled.PhotoCamera,
                    contentDescription = "Camera icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Aggiungi foto")
            }
            if(imageUri != null) {
                val painter = rememberAsyncImagePainter(model = imageUri)
                Image(
                    painter = painter,
                    contentDescription = "Track Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                )
            }
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = {
                    if (!addTrackDetailsState.canSubmit) {
                        Toast.makeText(ctx, "Inserisci i valori", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(SmartlagoonRoute.AddTrack.currentRoute)
                        tracksDbVm.addTrack(
                            Track(
                                city = addTrackState.city,
                                duration = addTrackState.duration,
                                trackPositions = addTrackState.trackPositions,
                                startLat = addTrackState.startLat,
                                startLng = addTrackState.startLng,
                                description = addTrackDetailsState.description,
                                name = addTrackDetailsState.title,
                                imageUri = imageUri?.toString(),
                                userId = user.id,
                            )
                        )
                    }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    Icons.Outlined.DoneOutline,
                    contentDescription = "save icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Salva")
            }
        }
    }
}