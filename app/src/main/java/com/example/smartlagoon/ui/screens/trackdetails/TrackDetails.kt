package com.example.smartlagoon.ui.screens.trackdetails

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.smartlagoon.data.database.Track
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.viewmodel.TracksDbState
import com.example.smartlagoon.ui.composables.BottomAppBar
import com.example.smartlagoon.ui.composables.SideBarMenu
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.composables.getMyDrawerState

/*import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline*/

@Composable
fun TrackDetails(
    navController: NavHostController,
    user: User,
    track: Track,
    tracksDbState: TracksDbState
) {
    val scope = rememberCoroutineScope()
    val myScaffold: @Composable () -> Unit = {
        val context = LocalContext.current
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "SMARTLAGOON" +
                                "\nTitolo: "+ track.name +
                                    "\nDescrizione: " + track.description +
                            "\nCittà di partenza: " + track.city +
                            "\nTempo: " + track.duration + " secondi")
                            type = "text/plain"
                        }
                        if (sendIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(sendIntent)
                        } else {
                            Toast.makeText(context, "Nessuna app per condividere il messaggio trovata", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Condividi",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Dettagli Percorso",
                    showSearch = false,
                    drawerState = getMyDrawerState(),
                    scope = scope,
                    showFilter = false
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            Card(
                modifier = Modifier
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState()),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = track.name,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = track.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, bottom = 10.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily.Default
                )
                var output = ""
                val (hours, minutes, seconds) = convertSeconds(track.duration)
                output = if(hours > 0 )
                    "$hours h $minutes min $seconds s"
                else if (minutes > 0)
                    "$minutes min $seconds s"
                else
                    "$seconds s"
                Text(
                    text = "Durata: $output",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
                var align = TextAlign.Start
                if (track.imageUri != null) {
                    align = TextAlign.End
                    Row(modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                        .padding(0.dp)
                    ) {
                        val painter = rememberAsyncImagePainter(model = track.imageUri)
                        Image(
                            painter = painter,
                            contentDescription = "Track Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(150.dp, 300.dp)
                        )
                    }
                }

                Text(
                    text = "Città: ${track.city}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = align
                )

                Card(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    /*GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        cameraPositionState = CameraPositionState(
                            CameraPosition(LatLng(track.startLat, track.startLng), 14f, 0f, 0f)
                        )
                    ) {
                        Marker(
                            state = MarkerState(position = LatLng(track.startLat, track.startLng)),

                            )

                        Polyline(
                            points = track.trackPositions,
                            color = Color.Blue
                        )
                    }*/
                }
            }
        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController,
        tracksDbState,
    )
}

private fun convertSeconds(seconds: Long): Triple<Long, Long, Long> {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return Triple(hours, minutes, remainingSeconds)
}