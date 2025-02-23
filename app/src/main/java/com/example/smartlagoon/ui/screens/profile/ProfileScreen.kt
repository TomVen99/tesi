package com.example.smartlagoon.ui.screens.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Mode
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
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.AnimatedImage
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    usersDbVm: UsersDbViewModel,
) {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val usr by usersDbVm.userLiveData.observeAsState()
    val tmpUser by usersDbVm.tmpUserLiveData.observeAsState()
    val user = if(tmpUser != null) tmpUser else usr
    user?.email?.let { Log.d("profileUSerLivedata", it) }
    val showModifyButton = usersDbVm.showModifyButton.observeAsState().value ?: false

    var name by remember { mutableStateOf(user?.name ?: "") }
    var surname by remember { mutableStateOf(user?.surname ?: "") }
    var username by remember { mutableStateOf(user?.username ?: "") }

    fun createImageUri(): Uri {
        val resolver = ctx.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: imageUri
                uri?.let {
                    imageUri = it
                    usersDbVm.currentUser.let { fbUser ->
                        fbUser.value?.let { it1 ->
                            usersDbVm.uploadProfileImage(it1.uid, imageUri!!) {
                                usersDbVm.fetchUserProfile()
                                user?.let { it1 -> Log.d("uploadProf", it1.profileImageUrl) }
                            }
                        }
                    }
                }
            }
        }

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
                Toast.makeText(ctx, "Permesso camera e galleria non concesso", Toast.LENGTH_SHORT).show()
            }
        }

    @Composable
    fun setProfileImage() {
        val imageModifier = Modifier
            .size(200.dp)
            .border(BorderStroke(2.dp, MyColors().borders), CircleShape)
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
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Modifica dati profilo",
                    modifier = Modifier.fillMaxWidth()
                        .background(MyColors().myBlu, RoundedCornerShape(8.dp))
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
                    currentRoute = "Profilo",
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
                        painter = painterResource(id = R.drawable.lagoonguard_logo_nosfondo),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
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
                    if (showModifyButton) {
                        Spacer(Modifier.size(4.dp))
                        Button(
                            colors = myButtonColors(),
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 12.dp,
                                hoveredElevation = 4.dp,
                                focusedElevation = 6.dp,
                                disabledElevation = 0.dp
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                Icons.Filled.Mode,
                                contentDescription = "Matita icon",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = "Modifica profilo",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(8.dp),
                    text = "${user?.name ?: ""} ${user?.surname ?: ""}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
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
                        Text(
                            text = /*if(tmpUser != null) "${tmpUser!!.username}" else*/ "${user?.username}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                        )
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

                        Text(
                            text = "${user?.email}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
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
                        text = "Punti: ${user?.points}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", ctx.packageName, null)
                            }
                            ctx.startActivity(intent)
                        },
                        modifier = Modifier
                            .align(Alignment.Top),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text("Impostazioni")
                    }
                    Button(
                        onClick = {
                            if (sharedPreferences != null) {
                                val edit = sharedPreferences.edit()
                                edit.putBoolean("isUserLogged", false)
                                edit.putString("username", "")
                                edit.apply()
                                Log.d("SharedPreferences", "tolgo sharedPreference")
                            }
                            usersDbVm.setLoginResult()
                            navController.navigate(SmartlagoonRoute.Login.route)
                            usersDbVm.logout()
                        },
                        modifier = Modifier
                            .align(Alignment.Bottom),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text("Logout")
                    }
                }

            }
        }
    }
}


