package com.example.smartlagoon.ui.screens.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.smartlagoon.R
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.viewmodel.TracksDbState
import com.example.smartlagoon.ui.UsersViewModel
import com.example.smartlagoon.ui.composables.BottomAppBar
import com.example.smartlagoon.ui.composables.SideBarMenu
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.composables.getMyDrawerState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    user: User,
    usersViewModel : UsersViewModel,
    sharedPreferences: SharedPreferences,
    tracksDbState: TracksDbState,
    userTracksNumber: Int,
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var userImage = user.urlProfilePicture

    Log.d("userImage", userImage.toString())

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
                usersViewModel.updateProfileImg(user.username, uri.toString())
            } ?: run {
                imageUri?.let { uri ->
                    usersViewModel.updateProfileImg(user.username, uri.toString())
                }
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

    @Composable
    fun setProfileImage() {
        val imageModifier = Modifier
            .size(200.dp)
            .border(
                BorderStroke(2.dp, Color.Black),
                CircleShape
            )
            .clip(CircleShape)
        when {
            (imageUri != null && userImage != user.urlProfilePicture) -> {
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "image taken",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
                userImage = user.urlProfilePicture
            }
            user.urlProfilePicture?.isNotEmpty() == true -> {
                Log.d("IMG", "Immagine profilo")
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(user.urlProfilePicture!!.toUri())
                        .crossfade(true)
                        .build(),
                    contentDescription = "profile img",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Log.d("IMG", "Placeholder")
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "image placeholder",
                    modifier = imageModifier.background(MaterialTheme.colorScheme.background),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }

    val myScaffold: @Composable () -> Unit = {

        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Profilo",
                    drawerState = getMyDrawerState(),
                    scope = scope,
                    showLogout = true,
                    sharedPreferences = sharedPreferences
                )
            },
            bottomBar = {
                BottomAppBar(
                    navController = navController,
                    user = user
                )
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    onClick = {
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                    },
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
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape )
                        .padding(8.dp),
                    text = user.name + " " + user.surname,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.size(15.dp))
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape )
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "account image"
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = user.username,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.size(15.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape )
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Filled.Mail,
                        contentDescription = "email"
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                    Text(
                        text = user.mail,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.size(15.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape )
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    /*Icon(
                        Icons.Filled.Numbers,
                        contentDescription = "n percorsi"
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))*/
                    Text(
                        text = "Numero percorsi: $userTracksNumber",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    SideBarMenu(
        myScaffold = myScaffold,
        navController,
        tracksDbState,
    )
}

