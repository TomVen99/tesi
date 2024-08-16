package com.example.smartlagoon.ui.screens.challenge

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.AnimatedButton
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.smartlagoon.R
import com.example.smartlagoon.TakePhotoActivity
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.myButtonColors


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChallengeScreen(
    navController: NavHostController,
    user: User,
    challengesDbVm: ChallengesDbViewModel,
    challengeList: List<Challenge>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Sfide",
            )
        },
    ) { contentPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(challengeList){ _, challenge ->
                //AnimatedButton(challenge)
                AchievementCard(challenge)
                //DuolingoButton(challenge)
                /*ListItem(
                    headlineContent = { Text(text = challenge.title) },
                    supportingContent = {
                        Text(text = challenge.description)
                    },
                    modifier = Modifier
                        .padding(2.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                )*/
            }
        }
    }
}

@Composable
fun AchievementCard(
    challenge: Challenge,
    /*title: String,
    description: String,
    progress: Float,
    icon: @Composable () -> Unit,
    requiredProgress: Int,
    achievedProgress: Int*/
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current  // Ottieni il Context corrente
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                showDialog = true
            },
        colors = CardDefaults.cardColors(
            containerColor = MyColors().myBluButtonBackground
        )
    ) {
        /*Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row with Icon and Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.immagine_sfida), // Load your image
                    contentDescription = "Challenge image", // Provide a description for accessibility
                    modifier = Modifier.size(width = 40.dp, height = 40.dp) // Optional: Set image size
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = challenge.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description Text
            Text(text = challenge.description, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar and Progress Count
            /*LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "pippo", fontSize = 14.sp, color = Color.White)*/
        }*/
        Row {
            Image(
                painter = painterResource(id = R.drawable.immagine_sfida),
                contentDescription = "Challenge image",
                modifier = Modifier
                    .size(70.dp, 120.dp)
                    .padding(5.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Header Row with Title
                Text(
                    text = challenge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MyColors().myBluButtonTitle
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description Text
                Text(
                    text = challenge.description,
                    fontSize = 16.sp,
                    color = MyColors().myBluButtonText,
                    fontWeight = FontWeight.Bold
                    )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar and Progress Count
                /*LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF4285F4)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "pippo", fontSize = 14.sp, color = Color.White)*/
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = challenge.title)
                },
                text = {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),  // Sostituisci `your_image` con il nome dell'immagine nella cartella drawable
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)  // Imposta la dimensione dell'icona
                        )
                        Text(text = challenge.description)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false // Chiude il popup
                            val intent = Intent(context, TakePhotoActivity::class.java).apply {
                                putExtra("challengePoints", challenge.points)
                                putExtra("challengeId", challenge.id)
                            }
                            context.startActivity(intent)
                        },
                        colors = myButtonColors(),
                    ) {
                        Text("Scatta + " +  challenge.points.toString() + " punti")
                    }
                }
            )
        }
    }
}



