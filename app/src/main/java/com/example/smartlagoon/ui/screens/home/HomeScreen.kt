package com.example.smartlagoon.ui.screens.home

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.getMyDrawerState
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.GroupedTracksState
import com.example.smartlagoon.ui.viewmodel.TracksDbState
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
import com.example.smartlagoon.ui.composables.BottomAppBar
import com.example.smartlagoon.ui.composables.PasswordTextField
import com.example.smartlagoon.ui.composables.TopAppBar
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    state: HomeScreenState,
    actions: HomeScreenActions,
    user : User,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,
    groupedTracksState: GroupedTracksState,
    sharedPreferences: SharedPreferences? = null
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.smartlagoon_logo),
                contentDescription = "Smart Lagoon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGrid(navController)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if(sharedPreferences != null) {
                        val edit = sharedPreferences.edit()
                        edit.putBoolean("isUserLogged", true)
                        edit.putString("username", "")
                        edit.apply()
                    }
                    Log.d("Log",SmartlagoonRoute.Login.route)
                    navController.navigate(SmartlagoonRoute.Login.route)
                          },
                modifier = Modifier
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun MenuGrid(navController: NavController) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            MenuItem("Sfide", R.drawable.ic_sfide, SmartlagoonRoute.Challenge, navController)
            MenuItem("Classifica", R.drawable.ic_classifica, SmartlagoonRoute.Ranking, navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            MenuItem("Profilo", R.drawable.ic_profilo, SmartlagoonRoute.Profile, navController)
            MenuItem("Badge", R.drawable.ic_badge, SmartlagoonRoute.Badge, navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            MenuItem("Ricicla", R.drawable.ic_ricicla, SmartlagoonRoute.Recycle, navController)
            MenuItem("Info", R.drawable.ic_info, SmartlagoonRoute.Info, navController)
        }
    }
}

@Composable
fun MenuItem(name: String, iconId: Int, route: SmartlagoonRoute, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(170.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(

            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}


    /*Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.CenterVertically))
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RectangleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.smartlagoon_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = {

                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Accedi")
            }
            Text(text = "Oppure")
            TextButton(
                onClick = {
                    navController.navigate(SmartlagoonRoute.Signin.route)
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Registrati ora")
            }
        }
    }*/
