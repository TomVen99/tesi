package com.example.smartlagoon.ui.screens.ranking

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
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.UsersState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingScreen(
    navController: NavHostController,
    user: User,
    state: UsersState,
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Classifica",
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
                            .padding(top = 5.dp),
                            //.border(1.dp, MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.Top
                    ) {
                        PrintRanking(
                            state.users
                        )
                    }
                }
            }
        }
    }
}

private fun getUserListToPrint(user: User) : List<User> {
    return listOf(user)
}

@Composable
private fun PrintRanking(users: List<User> ) {
    Log.d("classifica", "son qui")
    for(user in users) {
        /*Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {*/
            ListItem(
                headlineContent = { Text(text = user.username) },
                supportingContent = {
                    Text(text = user.name)
                },
                trailingContent = { Text(text = user.points.toString())},
                modifier = Modifier
                    .padding(top = 5.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer),
            )
        //}
    }
}



