package com.example.smartlagoon.ui.screens.challenge

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.AnimatedButton
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel


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
                AnimatedButton(challenge)
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



