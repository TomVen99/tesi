package com.example.smartlagoon.ui.screens.photo

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.data.database.Photo
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.ui.viewmodel.PhotosDbState
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import java.util.Date


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoScreen(
    navController: NavHostController,
    user: User,
    photosDbVm: PhotosDbViewModel,
    photosDbState: PhotosDbState,
) {
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
                    PhotoItem(photo = photo, user = user)
                }
            }
        }
    }
}

@Composable
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
}



