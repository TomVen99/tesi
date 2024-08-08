package com.example.smartlagoon.ui.screens.home

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.TakePhotoActivity
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.SmartlagoonRoute

@Composable
fun HomeScreen(
    navController: NavHostController,
    user : User,
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
            MenuGrid(navController, user)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if(sharedPreferences != null) {
                        val edit = sharedPreferences.edit()
                        edit.putBoolean("isUserLogged", true)
                        edit.putString("username", "")
                        edit.apply()
                    }
                    Log.d("Logout",SmartlagoonRoute.Login.route)
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
fun MenuGrid(navController: NavController, user: User) {
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
            MenuItem("Photo", R.drawable.ic_badge, SmartlagoonRoute.Photo, navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            MenuItem("Scatta", R.drawable.ic_ricicla, SmartlagoonRoute.Recycle, navController, user)
            MenuItem("About", R.drawable.ic_info, SmartlagoonRoute.About, navController)
        }
    }
}

@Composable
fun MenuItem(name: String, iconId: Int, route: SmartlagoonRoute, navController: NavController, user: User? = null) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(170.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                if(route.route == "recycle")
                {
                    val intent = Intent(context, TakePhotoActivity::class.java).apply{
                        putExtra("username", user?.username)
                        putExtra("userId", user?.id)
                    }
                    context.startActivity(intent)
                }else {
                    navController.navigate(route.route)
                }
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