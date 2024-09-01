package com.example.smartlagoon.ui.screens.photo

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.Photo
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.User
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel


@Composable
fun PhotoScreen(
    navController: NavHostController,
    photosDbVm: PhotosDbViewModel,
    usersDbVm: UsersDbViewModel,
    //photosDbState: PhotosDbState,
    comeFromTakePhoto: Boolean,
    challengePoints: Int = 0
) {
    var showDialog by remember { mutableStateOf(comeFromTakePhoto) }
    // Osserva i dati delle foto dal ViewModel
    val photos by photosDbVm.photosLiveData.observeAsState(emptyList()) // Utilizza LiveData per osservare le foto
    photosDbVm.fetchAllPhotos()

    SmartlagoonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Photo",
                )
            },
        ) { contentPadding ->
            if(photos.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { photo ->  // Usa l'elenco delle foto dal ViewModel
                        // Ottieni l'utente associato alla foto
                        var user by remember { mutableStateOf<User?>(null) }

                        // Chiamata per ottenere l'utente
                        LaunchedEffect(photo.userId) {
                            usersDbVm.getUser(photo.userId) { fetchedUser ->
                                user = fetchedUser
                            }
                        }
                        if (user != null) {
                            PhotoItem(photo = photo, user = user!!, navController, usersDbVm)
                        } else {
                            Text(text = "utente non trovato")
                        }
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = "Congratulazioni!!")
                        },
                        text = {
                            Column {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),  // Sostituisci `your_image` con il nome dell'immagine nella cartella drawable
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)  // Imposta la dimensione dell'icona
                                )
                                Text(text = "Hai guadagnato  $challengePoints punti!")
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false // Chiude il popup
                                },
                                colors = myButtonColors(),
                            ) {
                                Text("Ok")
                            }
                        }
                    )
                }
            } else {
                Log.d("Foto finite", "Nessuna foto presente")
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Ancora non ci sono foto, caricane una tu per primo!",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            onClick = {
                                navController.navigate(SmartlagoonRoute.Challenge.route)
                            },
                            colors = myButtonColors(),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(6.dp)
                        ) {
                            Text("Vai alle sfide!")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PhotoItem(photo: Photo, user: User, navController: NavHostController, usersDbVm: UsersDbViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(16.dp),
    ) {
        Box {
            photo.photoUrl.let { uriString ->
                val painter = rememberAsyncImagePainter(Uri.parse(uriString))
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, MyColors().borders, RoundedCornerShape(16.dp)), // Add border to image
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay with more dynamic style
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(
                        color = MyColors().myBlu
                    )
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                // Username with badge and styled text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        //painter = painterResource(id = R.drawable.ic_badge), // Badge image
                        painter = rememberAsyncImagePainter(user.profileImageUrl),
                        contentDescription = "User Badge",
                        modifier = Modifier
                            .size(28.dp) // Larger badge
                            .border(2.dp, MyColors().borders, CircleShape)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    user.username?.let {
                        Text(
                            text = user.username,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.clickable {
                                usersDbVm.fetchUserProfileByUsername(user.username)
                                val route = SmartlagoonRoute.Profile.createRoute(user.username)
                                navController.navigate(route)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

            }
        }
    }
}