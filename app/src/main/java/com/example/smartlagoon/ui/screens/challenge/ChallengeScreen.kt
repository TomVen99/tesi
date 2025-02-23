package com.example.smartlagoon.ui.screens.challenge

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.theme.MyColors
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.Challenge

@Composable
fun ChallengeScreen(
    navController: NavHostController,
    challengesDbVm: ChallengesDbViewModel,
    userId: String
) {
    challengesDbVm.getAllChallenges()
    val challengeList by challengesDbVm.allChallenges.observeAsState(
        emptyList()
    )
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
                AchievementCard(challenge, userId, navController, challengesDbVm)
            }
        }
    }
}

@Composable
fun AchievementCard(
    challenge: Challenge,
    userId: String,
    navController: NavHostController,
    challengeDbVm: ChallengesDbViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var showChallengeDone by remember { mutableStateOf(false) }
    val isCompleted = challenge.completedBy?.contains(userId) == true
    val containerColor = if(isCompleted) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MyColors().myBluButtonBackground
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                if(!isCompleted){
                    showDialog = true
                } else {
                    showChallengeDone = true
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row {
            if(!isCompleted) {
                Image(
                    painter = painterResource(id = R.drawable.immagine_sfida),
                    contentDescription = "Challenge image",
                    modifier = Modifier
                        .size(70.dp, 120.dp)
                        .padding(5.dp)
                )
            }else {
                Image(
                    painter = painterResource(id = R.drawable.immagine_sfida_bn),
                    contentDescription = "Challenge image",
                    modifier = Modifier
                        .size(70.dp, 120.dp)
                        .padding(5.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                challenge.title?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = if(!isCompleted) {
                            MyColors().myBluButtonTitle
                        }else {
                            Color.Gray
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                challenge.description?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(!isCompleted) {
                            MyColors().myBluButtonText
                        }else {
                            Color.Gray
                        },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    challenge.title?.let { Text(text = it) }
                },
                text = {
                    Column {
                        challenge.description?.let { Text(text = it) }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false

                            challengeDbVm.setCurrentChallenge(challenge)
                            Log.d("currentChallengeChallenge", challenge.toString())
                            navController.navigate(SmartlagoonRoute.Camera.route)
                        },
                        colors = myButtonColors(),
                    ) {
                        Text("Scatta + " +  challenge.points.toString() + " punti")
                    }
                }
            )
        }
        if (showChallengeDone) {
            AlertDialog(
                onDismissRequest = { showChallengeDone = false },
                title = {
                    Text(text = "Sfida completata!")
                },
                text = {
                    Text(text = "Questa sfida è già stata completata!")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showChallengeDone = false
                        },
                        colors = myButtonColors(),
                    ) {
                        Text("Ok" )
                    }
                }
            )
        }
    }
}



