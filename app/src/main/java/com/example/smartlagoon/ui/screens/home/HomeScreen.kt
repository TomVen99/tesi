package com.example.smartlagoon.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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
/*import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState*/

//data class MapTypes(val mapTypeId: MapType, val title: String, val drawableId: Int)

data class PlaceDetails(val latLng: LatLng, val name: String)

/*val mapTypes = listOf(
    MapTypes(MapType.NORMAL, "Default", R.drawable.defaultmap),
    MapTypes(MapType.HYBRID, "Satellite", R.drawable.satellitemap),
    MapTypes(MapType.TERRAIN, "Rilievo", R.drawable.reliefmap)
)*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    state: HomeScreenState,
    actions: HomeScreenActions,
    user : User,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,
    groupedTracksState: GroupedTracksState
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.CenterVertically))
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
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
    }
}