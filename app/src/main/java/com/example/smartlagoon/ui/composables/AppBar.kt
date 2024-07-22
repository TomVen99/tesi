package com.example.outdoorromagna.ui.composables

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Signpost
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.screens.home.HomeScreenActions
import com.example.outdoorromagna.ui.screens.tracks.TracksActions
import com.example.outdoorromagna.ui.screens.tracks.TracksState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController,
    currentRoute: String,
    showSearch: Boolean = false,
    actions: HomeScreenActions? = null,
    trackActions: TracksActions? = null,
    filterState: TracksState? = null,
    drawerState: DrawerState,
    scope: CoroutineScope,
    showFilter: Boolean = false,
    showLogout: Boolean = false,
    sharedPreferences: SharedPreferences? = null
) {
    Log.d("TAG", drawerState.toString())
    TopAppBar(
        title = {
            Text(
                currentRoute,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Log.d("TAG", "cliccato menu")
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            }
        },
        actions = {
            if (showLogout) {
                IconButton(
                    onClick = {
                        if(sharedPreferences != null) {
                            val edit = sharedPreferences.edit()
                            edit.putBoolean("isUserLogged", true)
                            edit.putString("username", "")
                            edit.apply()
                        }
                        navController.navigate(OutdoorRomagnaRoute.Login.route)
                        }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            if (showFilter) {
                IconButton(onClick = {
                    if (filterState?.isShowFilterEnabled == true) {
                        trackActions?.setShowFilter(false)
                    } else {
                        trackActions?.setShowFilter(true)
                    }
                },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filtra"
                    )
                }
            }
            if (showSearch) {
                IconButton(onClick = { actions?.setShowSearchBar(true) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Cerca",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun BottomAppBar(
    navController: NavHostController,
    user: User
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        navController.navigate(
                            OutdoorRomagnaRoute.Home.buildWithoutPosition(
                                user.username
                            )
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    shape = RectangleShape
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Map,
                            contentDescription = "Mappa"
                        )
                        Spacer(Modifier.size(5.dp))
                        Text("Mappa")
                    }
                }
                OutdoorRomagnaRoute.AddTrack.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.AddTrack.currentRoute)
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    shape = RectangleShape
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            contentDescription = "Registra"
                        )
                        Spacer(Modifier.size(5.dp))
                        Text("Registra")
                    }
                }
                OutdoorRomagnaRoute.Tracks.buildRoute(user.username, false)
                OutdoorRomagnaRoute.Settings.buildRoute(user.username)
                OutdoorRomagnaRoute.AddTrackDetails.buildRoute(user.username)
                OutdoorRomagnaRoute.AddTrack.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.Tracks.currentRoute)
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    shape = RectangleShape
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Signpost,
                            contentDescription = "Percorsi"
                        )
                        Spacer(Modifier.size(5.dp))
                        Text("Percorsi")
                    }
                }

                OutdoorRomagnaRoute.Profile.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.Profile.currentRoute)
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    shape = RectangleShape
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profilo",
                        )
                        Spacer(Modifier.size(5.dp))
                        Text("Profilo")
                    }
                }
            }
        }
    )
}

