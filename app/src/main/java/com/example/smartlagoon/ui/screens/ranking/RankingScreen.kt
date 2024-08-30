package com.example.smartlagoon.ui.screens.ranking

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingScreen(
    navController: NavHostController,
    usersDbVm: UsersDbViewModel
) {
    usersDbVm.getRanking()
    val ranking by usersDbVm.rankingLiveData.observeAsState(emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Classifica",
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
                    painter = painterResource(id = R.drawable.lagoonguard_logo),//smartlagoon_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    ) { contentPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(ranking) { _, user ->
                if (user != null) {
                    ListItem(
                        headlineContent = { user.username?.let { Text(text = it) } },
                        supportingContent = {
                            user.name?.let { Text(text = it) }
                        },
                        leadingContent = {
                            val painter =
                                if (user.profileImageUrl != "")
                                    rememberAsyncImagePainter(Uri.parse(user.profileImageUrl))
                                else {
                                    painterResource(id = R.drawable.ic_launcher_foreground)
                                }
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Crop
                            )
                        },
                        trailingContent = { Text(text = user.points.toString()) },
                        modifier = Modifier
                            .padding(2.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            ),
                    )
                }
            }
        }
    }
}
