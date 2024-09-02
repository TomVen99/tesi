package com.example.smartlagoon.ui.screens.play

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.screens.home.SingleMenuItem
import com.example.smartlagoon.ui.theme.SmartlagoonTheme

@Composable
fun PlayScreen(
    navController: NavHostController,
){
    SmartlagoonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Gioca",
                )
            },
        ) {contentPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                MenuGrid(navController)
            }
        }
    }
}

@Composable
fun MenuGrid(navController: NavController){
    LazyColumn {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Quiz", R.drawable.ic_ricicla, SmartlagoonRoute.Quiz, navController)
                MenuItem("Sfide", R.drawable.ic_sfide, SmartlagoonRoute.Challenge, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Classifica", R.drawable.ic_classifica, SmartlagoonRoute.Ranking, navController)
                MenuItem("About", R.drawable.ic_info, SmartlagoonRoute.About, navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SingleMenuItem("Profilo", R.drawable.ic_profilo, SmartlagoonRoute.Ranking, navController, 80)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MenuItem(name: String, iconId: Int, route: SmartlagoonRoute, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(150.dp)
            //.clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(route.route)
            }
            .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RoundedCornerShape(8.dp))
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
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                    //.clip(RoundedCornerShape(8.dp))
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

