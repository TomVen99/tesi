package com.example.smartlagoon.ui.screens.photo

import android.net.Uri
import android.util.Log
import android.widget.PopupWindow
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.R
import com.example.smartlagoon.TakePhotoActivity
import com.example.smartlagoon.data.database.Photo
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.PhotosDbState
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import java.util.Date


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoScreen(
    navController: NavHostController,
    //user: User,
    photosDbVm: PhotosDbViewModel,
    usersViewModel: UsersViewModel,
    photosDbState: PhotosDbState,
    comeFromTakePhoto: Boolean,
    challengePoints: Int = 0
) {
    var showDialog by remember { mutableStateOf(comeFromTakePhoto) }
    SmartlagoonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Photo",
                )
            },
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photosDbState.photos) { photo ->
                    var user by remember { mutableStateOf<User?>(null) }

                    LaunchedEffect(photo.username) {
                        user = usersViewModel.getUser(photo.username)
                    }

                    user?.let {
                        PhotoItem(photo = photo, user = it)
                    }

                    Log.d("Log", "log.foto")
                }
            }
            if(showDialog) {
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
                            Text(text = "Hai guadagnato "+ challengePoints.toString() +" punti!")
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
        }
    }
}

/*@Composable
fun PhotoItem(photo: Photo, user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Box(

        ) {
            photo.imageUri?.let { uriString ->
                val painter = rememberAsyncImagePainter(Uri.parse(uriString))
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f), // Puoi modificare l'aspetto secondo le tue necessità
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = photo.username + " " + Date(photo.timestamp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.TopStart)
                        .background(Color.White)
                        .fillMaxWidth()
                )
            }
        }
    }
}*/

/*@Composable
fun PhotoItem(photo: Photo, user: User) {
    // Card with a more gamified look
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box {
            photo.imageUri?.let { uriString ->
                val painter = rememberAsyncImagePainter(Uri.parse(uriString))
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay for additional information
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                // User info with gamified style
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = photo.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Date Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = Date(photo.timestamp).toString(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}*/


@Composable
fun PhotoItem(photo: Photo, user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            /*.border(4.dp, MyColors().borders, RoundedCornerShape(16.dp)) // Brighter border
            .shadow(12.dp, RoundedCornerShape(16.dp)), // Stronger shadow for a "pop-out" effect
            .background(brush = Brush.linearGradient( // Gradient background for the card
                colors = listOf(Color(0xFF42A5F5), Color(0xFF00C853)),
                start = Offset(0f, 0f),
                end = Offset(1000f, 1000f)
            )),*/
        elevation = CardDefaults.cardElevation(16.dp),
    ) {
        Box {
            photo.imageUri?.let { uriString ->
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
                        /*brush = Brush.verticalGradient( // Gradient overlay
                            colors = listOf(MyColors().ultraLightBlue, MyColors().myBlu)//Color.Black.copy(alpha = 0.7f))
                        )*/
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
                        painter = rememberAsyncImagePainter(user.urlProfilePicture),
                        contentDescription = "User Badge",
                        modifier = Modifier.size(28.dp) // Larger badge
                            .border(2.dp, MyColors().borders, CircleShape)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = photo.username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        /*modifier = Modifier.clickable {
                            navController.navigate
                        }*/
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Timestamp with icon and animated color change
                /*Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "Date Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color(0xFF304FFE), shape = CircleShape) // Background for icon
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Date(photo.timestamp).toString(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = animateColorAsState(
                            targetValue = if (Date(photo.timestamp).before(Date())) Color.Red else Color.Green,
                            label = ""
                        ).value // Color change based on condition
                    )
                }*/
            }
        }
    }
}