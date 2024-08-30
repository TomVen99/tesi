package com.example.smartlagoon.ui.screens.ranking

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.viewmodel.UsersState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingScreen(
    navController: NavHostController,
    //state: UsersState,
) {
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
            /*itemsIndexed(state.userOlds){ _, user ->
                ListItem(
                    headlineContent = { Text(text = user.username) },
                    supportingContent = {
                        Text(text = user.name)
                    },
                    leadingContent = {
                        val painter =
                            if (user.urlProfilePicture != "")
                                rememberAsyncImagePainter(Uri.parse(user.urlProfilePicture))
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
                    trailingContent = { Text(text = user.points.toString())},
                    modifier = Modifier
                        .padding(2.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                )
            }*/
        }
    }
}
