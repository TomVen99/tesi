package com.example.smartlagoon.ui.screens.challenge

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChallengeScreen(
    navController: NavHostController,
    user: User,
    challengesDbVm: ChallengesDbViewModel,
    challengeList: List<Challenge>
    /*state: TracksState,
    actions: TracksActions,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,*/
) {
    //val specificTracksList by tracksDbVm.specificTracksList.observeAsState()
    //var actualFilterOption by remember { mutableIntStateOf(FilterOption.ALL_TRACKS.ordinal) }
    //val specificFavouritesList by favouritesDbVm.specificFavouritesList.observeAsState()

    //Log.d("trackList", specificTracksList.toString())
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Sfide",
                /*trackActions = actions,
                scope = scope,*/
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
            stickyHeader {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.Top
                    ) {

                        PrintChallenges(
                            challengeList
                        )
                    }
                }
            }
        }
    }
}

private fun getChallengeListToPrint(user: User) : List<User> {
    return listOf(user)
}

@Composable
private fun PrintChallenges(challenges: List<Challenge> ) {
    Log.d("challenges", challenges.toString())
    for(challenge in challenges) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            ListItem(
                headlineContent = { Text(text = challenge.title) },
                supportingContent = {
                    Text(text = challenge.description)
                },
            )
        }
    }
}



