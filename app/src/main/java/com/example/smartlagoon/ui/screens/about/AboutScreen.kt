package com.example.smartlagoon.ui.screens.about

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.data.database.Favourite
import com.example.smartlagoon.data.database.Track
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.viewmodel.FavouritesDbViewModel
import com.example.smartlagoon.ui.viewmodel.TracksDbState
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
import com.example.smartlagoon.ui.composables.FilterBar
import com.example.smartlagoon.ui.composables.FilterOption
import com.example.smartlagoon.ui.composables.SideBarMenu
import com.example.smartlagoon.ui.composables.TopAppBar


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutScreen(
    navController: NavHostController,
    /*user: User,
    state: AboutState,
    actions: AboutActions,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,
    favouritesDbVm: FavouritesDbViewModel,
    isSpecificTrack: Boolean*/
) {
    /*Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "About",
            )
        },
    ) { contentPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Il progetto SMARTLAGOON",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Il progetto europeo SMARTLAGOON è un'iniziativa ambiziosa che si concentra sullo studio e la gestione sostenibile delle lagune costiere altamente antropizzate, come il Mar Menor in Spagna.",
                fontSize = 16.sp
            )
            Text(
                text = "Il progetto SMARTLAGOON",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Il progetto europeo SMARTLAGOON è un'iniziativa ambiziosa che si concentra sullo studio e la gestione sostenibile delle lagune costiere altamente antropizzate, come il Mar Menor in Spagna.",
                fontSize = 16.sp
            )
            Text(
                text = "Il progetto SMARTLAGOON",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Il progetto europeo SMARTLAGOON è un'iniziativa ambiziosa che si concentra sullo studio e la gestione sostenibile delle lagune costiere altamente antropizzate, come il Mar Menor in Spagna.",
                fontSize = 16.sp
            )
            Text(
                text = "Il progetto SMARTLAGOON",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Il progetto europeo SMARTLAGOON è un'iniziativa ambiziosa che si concentra sullo studio e la gestione sostenibile delle lagune costiere altamente antropizzate, come il Mar Menor in Spagna.",
                fontSize = 16.sp
            )
            Image(
                painter = painterResource(id = R.drawable.smartlagoon_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    }*/
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "About"
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
                    painter = painterResource(id = R.drawable.smartlagoon_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // Set desired height)
                )
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Repeated items for example purposes
            repeat(6) {
                item {
                    Text(
                        text = "Il progetto SMARTLAGOON",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Il progetto europeo SMARTLAGOON è un'iniziativa ambiziosa che si concentra sullo studio e la gestione sostenibile delle lagune costiere altamente antropizzate, come il Mar Menor in Spagna.",
                        fontSize = 16.sp
                    )
                }
            }
            // Add more items as needed
        }
    }
}

