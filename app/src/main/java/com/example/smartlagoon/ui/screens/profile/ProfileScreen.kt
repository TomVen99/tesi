package com.example.smartlagoon.ui.screens.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    usersDbVm: UsersDbViewModel,
) {
    val ctx = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val user by usersDbVm.userLiveData.observeAsState()
    var name by remember { mutableStateOf(user?.name ?: "") }
    var surname by remember { mutableStateOf(user?.surname ?: "") }
    var username by remember { mutableStateOf(user?.username ?: "") }

    // Funzione per creare URI temporaneo per l'immagine
    fun createImageUri(): Uri {
        val resolver = ctx.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    // Funzione per lanciare la selezione dell'immagine
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: imageUri
                uri?.let {
                    imageUri = it
                    usersDbVm.auth.currentUser?.let { fbUser ->
                        usersDbVm.uploadProfileImage(fbUser.uid, imageUri!!) {
                            // Questo verrà eseguito solo dopo che l'upload e l'update sono completati
                            usersDbVm.fetchUserProfile() // Assicurati che questo aggiorni i dati correttamente
                            user?.let { it1 -> Log.d("uploadProf", it1.profileImageUrl) }
                        }
                    }
                }
            }
        }

    // Funzione per richiedere permesso della fotocamera e scegliere un'immagine
    val requestCameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val uri = createImageUri()
                imageUri = uri

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, uri)
                }
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                val chooserIntent = Intent.createChooser(galleryIntent, "Select Image").apply {
                    putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                }

                imagePickerLauncher.launch(chooserIntent)
            } else {
                Toast.makeText(ctx, "Permesso non concesso", Toast.LENGTH_SHORT).show()
            }
        }

    // Funzione per gestire l'immagine del profilo
    @Composable
    fun setProfileImage() {
        val imageModifier = Modifier
            .size(200.dp)
            .border(BorderStroke(2.dp, Color.Black), CircleShape)
            .clip(CircleShape)

        when {
            user?.profileImageUrl?.isNotEmpty() == true -> {
                Log.d("non empty", "aaa")
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(user?.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Image",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            imageUri != null -> {
                Log.d("non empty", "111")
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected Image",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                Log.d("non empty", "333")
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Placeholder Image",
                    modifier = imageModifier.background(MaterialTheme.colorScheme.background),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    // Per lanciare coroutine per l'apertura/chiusura del Bottom Sheet
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Modifica dati profilo",
                    modifier = Modifier.fillMaxWidth()
                        .background(MyColors().myBlu)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                    )
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                    )
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        usersDbVm.updateUserProfile(name, surname, username)
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                    colors = myButtonColors(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Salva")
                }
            }
        },
        sheetState = sheetState
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Profilo"
                )
            },
            bottomBar = {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RectangleShape)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lagoonguard_logo),//smartlagoon_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(top = 20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                setProfileImage()
                Spacer(modifier = Modifier.size(15.dp))

                Button(
                    colors = myButtonColors(),
                    onClick = {
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        hoveredElevation = 4.dp,
                        focusedElevation = 6.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = "Camera icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Scegli foto")
                }
                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(8.dp),
                    text = "${user?.name ?: ""} ${user?.surname ?: ""}",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.size(15.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "Account Image",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    user?.username?.let {
                        Text(
                            text = it,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(modifier = Modifier.size(15.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Filled.Mail,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                    user?.email?.let {
                        Text(
                            text = it,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.size(15.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Punti: ${user?.points ?: 0}",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Button(
                    colors = myButtonColors(),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(text = "Modifica profilo")
                }

            }
        }
    }
}


