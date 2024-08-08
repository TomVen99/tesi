package com.example.smartlagoon.ui.composables

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ExitToApp
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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.smartlagoon.data.database.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController?,
    currentRoute: String,
) {
    Log.d("TAG", drawerState.toString())
    TopAppBar(
        title = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    currentRoute,
                    fontWeight = FontWeight.Medium,
                )
            }
        },

        navigationIcon = {
                IconButton(onClick = {
                    Log.d("TAG", "cliccato indietro")
                    navController?.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }

        },
        actions = {
            /*if (showLogout) {
                IconButton(
                    onClick = {
                        if(sharedPreferences != null) {
                            val edit = sharedPreferences.edit()
                            edit.putBoolean("isUserLogged", true)
                            edit.putString("username", "")
                            edit.apply()
                        }
                        navController.navigate(SmartlagoonRoute.Login.route)
                        }
                ){
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
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
            }*/
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
    /*BottomAppBar(
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
                            SmartlagoonRoute.Home.currentRoute
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
                SmartlagoonRoute.AddTrack.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = {
                        navController.navigate(SmartlagoonRoute.AddTrack.currentRoute)
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
                SmartlagoonRoute.Tracks.buildRoute(user.username, false)
                SmartlagoonRoute.Settings.buildRoute(user.username)
                SmartlagoonRoute.AddTrackDetails.buildRoute(user.username)
                SmartlagoonRoute.AddTrack.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        navController.navigate(SmartlagoonRoute.Tracks.currentRoute)
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

                SmartlagoonRoute.Profile.buildRoute(user.username)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(0.dp),
                    onClick = {
                        navController.navigate(SmartlagoonRoute.Profile.currentRoute)
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
    )*/
}

