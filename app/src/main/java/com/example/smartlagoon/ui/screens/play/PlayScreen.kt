package com.example.smartlagoon.ui.screens.play

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.AnimatedImage
import com.example.smartlagoon.ui.composables.MenuItem
import com.example.smartlagoon.ui.composables.SingleMenuItem
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.SmartlagoonTheme
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel

@Composable
fun PlayScreen(
    navController: NavHostController,
    photosDbVm: PhotosDbViewModel
){
    SmartlagoonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Menu",
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
                        painter = painterResource(id = R.drawable.lagoonguard_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            },
        ) {contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                AnimatedImage(R.raw.sea_background)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuGrid(navController, photosDbVm)
                }
            }
        }
    }
}

@Composable
fun MenuGrid(navController: NavController, photosDbVm: PhotosDbViewModel,){
    LazyColumn {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                photosDbVm.showUserPhoto(true)
                SingleMenuItem("Le mie foto", R.raw.turtle, SmartlagoonRoute.Photo, navController, 150)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Quiz", R.raw.quiz, SmartlagoonRoute.Quiz.route, navController)
                MenuItem("Sfide", R.raw.challenge, SmartlagoonRoute.Challenge.route, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Classifica", R.raw.ranking, SmartlagoonRoute.Ranking.route, navController)
                MenuItem("Profilo",R.raw.profile,SmartlagoonRoute.Profile.route, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("About", R.raw.info, SmartlagoonRoute.About.route, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


