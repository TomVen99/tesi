package com.example.outdoorromagna.ui.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.outdoorromagna.R
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbState
import com.example.outdoorromagna.ui.TracksDbViewModel
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: OutdoorRomagnaRoute
)

var drawerState: DrawerState = DrawerState(DrawerValue.Closed)

fun getMyDrawerState() : DrawerState {
    return drawerState
}

val items = listOf(
    NavigationItem(
        title = "Mappa",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        route = OutdoorRomagnaRoute.Home
    ),
    NavigationItem(
        title = "Registra",
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircle,
        route = OutdoorRomagnaRoute.AddTrack
    ),
    NavigationItem(
        title = "Percorsi",
        selectedIcon = Icons.Filled.Directions,
        unselectedIcon = Icons.Outlined.Directions,
        badgeCount = 0,
        route = OutdoorRomagnaRoute.Tracks
    ),
    NavigationItem(
        title = "Profilo",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = OutdoorRomagnaRoute.Profile
    ),
    NavigationItem(
        title = "Impostazioni",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = OutdoorRomagnaRoute.Settings
    ),
)
var currentRoute by mutableStateOf("")
@Composable
fun SideBarMenu (
    myScaffold: @Composable () -> Unit,
    navController: NavHostController,
    tracksDbState: TracksDbState? = null,
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }

        ModalNavigationDrawer(
            gesturesEnabled = false,
            drawerContent = {
                ModalDrawerSheet (
                    drawerShape = RectangleShape,
                    drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
                ){
                    FloatingActionButton(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isOpen) close()
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(all = 5.dp)
                            .size(48.dp)
                    ) {
                        Icon(Icons.Outlined.Close, "Back to menu")
                    }

                    Image(
                        painter = painterResource(id = R.drawable.logooudoorromagnarectangle),
                        contentDescription = "logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        alignment = Alignment.Center
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 5.dp)
                                .border(
                                    shape = RoundedCornerShape(25.dp),
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ),

                            label = {
                                Text(text = item.title)
                            },
                            selected = true,
                            onClick = {
                                navController.navigate(item.route.currentRoute)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = tracksDbState?.tracks?.count().toString())//item.badgeCount.toString())
                                }
                            },
                        )

                    }
                }
            },
            drawerState = drawerState
        ) {
            myScaffold()
        }
    }
}