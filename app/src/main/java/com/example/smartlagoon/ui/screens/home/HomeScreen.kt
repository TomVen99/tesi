package com.example.smartlagoon.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.smartlagoon.ui.composables.getMyDrawerState
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.viewmodel.GroupedTracksState
import com.example.smartlagoon.ui.viewmodel.TracksDbState
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
import com.example.smartlagoon.ui.composables.BottomAppBar
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    /*var isLocationActive by remember { mutableStateOf(isLocationEnabled(context)) }
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("mapViewType", Context.MODE_PRIVATE)
    actions.setMapView(
        intToMapType(
        context.getSharedPreferences("mapViewType", Context.MODE_PRIVATE)
            .getInt("mapViewType", 1))
    )*/
    var canViewCurrentPosition by remember { mutableStateOf(false) }
    var showLocationPermissionDenied by remember { mutableStateOf(false) }

    /**PER ELIMINARE TUTTI I TRACK*/
    /*tracksDbState.tracks.forEach { track ->
        tracksDbVm.deleteTrack(track)
    }*/
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "Home",
                    actions = actions,
                    showSearch = true,
                    drawerState = getMyDrawerState(),
                    scope = scope
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {/*
                //CreateMap(navController, state, actions)
                var center by remember {
                    mutableStateOf(
                        LatLng(
                            44.1528f.toDouble(),
                            12.2036f.toDouble()
                        )
                    )
                }
                var placeSearch by remember { mutableStateOf(listOf<PlaceDetails>()) }
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition(center, 10f, 0f, 0f)
                }
                val context = LocalContext.current
                var showButton by remember { mutableStateOf(false) }
                var showPopUp by remember { mutableStateOf(false) }
                val locationService = koinInject<LocationService>()

                val locationPermission = rememberPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) { status ->
                    when (status) {
                        PermissionStatus.Granted ->
                            locationService.requestCurrentLocation()
                        PermissionStatus.Denied ->
                            actions.setShowLocationPermissionDeniedAlert(true)
                        PermissionStatus.PermanentlyDenied ->
                            actions.setShowLocationPermissionPermanentlyDeniedSnackbar(true)
                        PermissionStatus.Unknown -> {}
                    }
                }
                canViewCurrentPosition = locationPermission.status.isGranted && isLocationActive
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MapView(
                            cameraPositionState,
                            navController,
                            user,
                            state.mapView,
                            tracksDbState,
                            groupedTracksState,
                            tracksDbVm,
                            canViewCurrentPosition
                        )

                        FloatingActionButton( //bottone del gps
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = {
                                requestLocation(locationPermission, locationService)
                                isLocationActive = isLocationEnabled(context)
                                showLocationPermissionDenied = true
                                if(!isLocationActive) {
                                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 10.dp, bottom = 34.dp)
                                .size(40.dp),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Outlined.GpsFixed, "Use localization")
                        }
                        val buttonPadding = if(canViewCurrentPosition) 58.dp else 8.dp
                        FloatingActionButton( //bottone per cambiare la modalità di visualizzazione
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = {
                                /**PER INSERIRE I TRACK DI TEST*/
                                if (tracksDbState.tracks.isEmpty()) {
                                    val testTracks = generateTestTracks()
                                    testTracks.forEach { testTrack ->
                                        tracksDbVm.addTrack(testTrack)
                                    }
                                }
                                showPopUp = true
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 12.dp, top = buttonPadding)
                                .size(40.dp),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Outlined.Layers, "Choose map type")
                        }

                        if (showPopUp)
                            Dialog(
                                onDismissRequest = { showPopUp = false }
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                ) {
                                    Text(text = "Tipo di mappa:", modifier = Modifier.padding(10.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                    ) {

                                        mapTypes.forEach { type ->
                                            Button(
                                                onClick = {
                                                    val edit = sharedPreferences.edit()
                                                    edit.putInt("mapViewType", type.mapTypeId.value)
                                                    edit.apply()
                                                    actions.setMapView(type.mapTypeId)
                                                    showPopUp = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.primary
                                                ),
                                                shape = RectangleShape,
                                                modifier = Modifier.padding(0.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = type.drawableId),
                                                    contentDescription = type.title,
                                                    modifier = Modifier
                                                        .size(70.dp)
                                                        .padding(0.dp)
                                                        .border(
                                                            BorderStroke(
                                                                1.dp,
                                                                MaterialTheme.colorScheme.onBackground
                                                            )
                                                        )
                                                )
                                            }
                                        }
                                    }
                                Spacer(modifier = Modifier.size(25.dp))
                                }
                            }
                        if(locationPermission.status.isDenied && showLocationPermissionDenied)
                            ShowLocationPermissionDenied(
                                context = context,
                                text = "Il permesso per la posizione è stato negato. Non è possibile visualizzare la posizione",
                                onDismiss = {
                                    showLocationPermissionDenied = false
                                }
                            )
                        if (state.showSearchBar) {
                            Column {
                                Row {
                                    SearchBar(actions = actions, onQueryChanged =  { query ->
                                            performSearch(query = query, context = context) { results ->
                                                if (results.isNotEmpty()) {
                                                    placeSearch = results
                                                }
                                            }
                                        }
                                    )
                                }
                                if(placeSearch.isNotEmpty()) {
                                    placeSearch.forEach { place ->
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 0.dp)
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.background)
                                                .border(
                                                    BorderStroke(
                                                        1.dp,
                                                        MaterialTheme.colorScheme.onBackground
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        0.dp,
                                                        0.dp,
                                                        4.dp,
                                                        4.dp
                                                    )
                                                )
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    center = place.latLng
                                                    cameraPositionState
                                                        .move(CameraUpdateFactory.newLatLngZoom(center,12f))
                                                    actions.setShowSearchBar(false)
                                                    placeSearch = listOf()
                                                },
                                                colors = ButtonDefaults.textButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.background,
                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                ),
                                                modifier = Modifier
                                                    .padding(vertical = 0.dp)
                                                    .fillMaxWidth(),
                                                shape = RectangleShape,
                                                contentPadding = PaddingValues(0.dp),
                                            ) {
                                                Text(text = place.name)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            placeSearch = emptyList()
                        }
                    }
                }
            }*/
        }
    }
    /*SideBarMenu(
        myScaffold = myScaffold,
        navController,
        tracksDbState,
    )*/
}
/*
fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun requestLocation(locationPermission: PermissionHandler, locationService: LocationService) {
    Log.d("riga377", locationPermission.status.toString())
    if (locationPermission.status.isGranted) {
        locationService.requestCurrentLocation()
    } else {
        locationPermission.launchPermissionRequest()
    }
}

fun intToMapType(intType: Int): MapType {
    return when (intType) {
        1 -> MapType.NORMAL
        3 -> MapType.TERRAIN
        4 -> MapType.HYBRID
        else -> MapType.NORMAL
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MapView(
    cameraPositionState: CameraPositionState,
    navController: NavHostController,
    user : User,
    mapViewType: MapType,
    tracksDbState: TracksDbState,
    groupedTracksState: GroupedTracksState,
    tracksDbVm: TracksDbViewModel,
    canViewCurrentPosition: Boolean
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {  },
        properties = MapProperties(
            mapType = mapViewType,
            isMyLocationEnabled = canViewCurrentPosition
        )
    ) {
        groupedTracksState.tracks.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.groupedLat, location.groupedLng)),
                onClick = {

                    tracksDbVm.getTracksInRange(location.groupedLat, location.groupedLng)
                    navController.navigate(
                        SmartlagoonRoute.Tracks.buildRoute(user.username, true)
                    )
                    true
                }
            )
        }
    }
}

@Composable
fun rememberPermission(
    permission: String,
    onResult: (status: PermissionStatus) -> Unit = {}
): PermissionHandler {
    var status by remember { mutableStateOf(PermissionStatus.Unknown) }

    val activity = (LocalContext.current as ComponentActivity)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        status = when {
            isGranted -> PermissionStatus.Granted
            activity.shouldShowRequestPermissionRationale(permission) -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
        onResult(status)
    }

    val permissionHandler by remember {
        derivedStateOf {
            object : PermissionHandler {
                override val permission = permission
                override val status = status
                override fun launchPermissionRequest() = permissionLauncher.launch(permission)
            }
        }
    }
    return permissionHandler
}

@Composable
private fun SearchBar(onQueryChanged: (String) -> Unit, actions: HomeScreenActions) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        label = { Text("Cerca luogo") },
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        singleLine = true,
        shape = RectangleShape,
        trailingIcon = {
            IconButton(onClick = { actions.setShowSearchBar(false) }) {
                Icon(Icons.Outlined.Close, contentDescription = "Chiudi")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

private fun performSearch(query: String, context: Context, onResult: (List<PlaceDetails>) -> Unit) {
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
    }
    val placesClient = Places.createClient(context)
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        val locations = mutableListOf<PlaceDetails>()
        for (prediction in response.autocompletePredictions) {
            fetchPlaceDetails(prediction.placeId, placesClient) { latLng ->
                latLng?.let {
                    locations.add(PlaceDetails(it, prediction.getFullText(null).toString()))
                    if (locations.size == response.autocompletePredictions.size) {
                        onResult(locations)
                    }
                }
            }
        }
    }.addOnFailureListener { exception ->
        Log.e("SearchPlaces", "Error fetching autocomplete predictions", exception)
    }
}

//usata per richiedere la latlng
private fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient, onResult: (LatLng?) -> Unit) {
    val placeFields = listOf(Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request).addOnSuccessListener { fetchPlaceResponse ->
        onResult(fetchPlaceResponse.place.latLng)
    }.addOnFailureListener { exception ->
        Log.e("FetchPlaceDetails", "Error fetching place details", exception)
        onResult(null)
    }
*/
}
