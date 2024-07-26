package com.example.smartlagoon.ui.screens.tracking

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.smartlagoon.ui.screens.addtrack.AddTrackActions
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel

@Composable
fun TrackingScreen(
    navController: NavController,
    trackingActions: TrackingActions,
    trackingState: TrackingState,
    user: User,
    tracksDbVm: TracksDbViewModel,
    addTrackActions: AddTrackActions
) {
}/*
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val registry = rememberLauncherForActivityResultRegistry()
    val presenter: MapPresenter = remember { MapPresenter(context, registry, MutableLiveData(), tracksDbVm) }
    var isTrackingStarted by remember { mutableStateOf(false) }

    val uiState = remember { MutableLiveData(Ui.EMPTY)}
    val elapsedTimeState = presenter.elapsedTime.observeAsState(0L)
    val mapView = rememberMapViewWithLifecycle()

    presenter.onMapLoaded(context)
    presenter.mySetUi(uiState)
    presenter.onViewCreated(lifecycleOwner)

    val locationService = koinInject<LocationService>()
    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_FINE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                trackingActions.setShowLocationPermissionDenied(true)

            PermissionStatus.PermanentlyDenied ->
                trackingActions.setShowLocationPermissionDenied(true)

            PermissionStatus.Unknown -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        IndicatorsLayout(uiState, elapsedTimeState.value)
        Box(modifier = Modifier.weight(1f)) {
            //MapViewComposable(presenter)
            AndroidView({ mapView }) { mapView ->
                mapView.getMapAsync { googleMap ->
                    Log.d("TAG", "prima setGoogleMap")
                    presenter.setGoogleMap(googleMap)
                    presenter.onViewCreated(lifecycleOwner)
                    Log.d("TAG", "dopo setGoogleMap")
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    presenter.enableUserLocation()
                    requestLocation(locationPermission, locationService)
                }
            }
        }

        Button(
            onClick = {
                isTrackingStarted = !isTrackingStarted
                if (isTrackingStarted) {
                    startTracking(presenter)
                } else {
                    stopTracking(presenter, addTrackActions)
                    navController.navigate(SmartlagoonRoute.AddTrackDetails.currentRoute)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(text = if (isTrackingStarted) stringResource(R.string.stop_label) else stringResource(R.string.start_label),
            color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    mapView.onDestroy()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }*/
//}

/*@Composable
fun IndicatorsLayout(uiState: MutableLiveData<Ui>, elapsedTimeState: Long) {
    val uiStateValue by uiState.observeAsState(Ui.EMPTY)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IndicatorRow(
            iconResId = R.drawable.ic_pace,
            description = stringResource(id = R.string.steps),
            label = stringResource(id = R.string.steps),
            value = uiStateValue.formattedSteps
        )
        IndicatorRow(
            iconResId = R.drawable.ic_time,
            description = stringResource(id = R.string.elapsed_time_label),
            label = stringResource(id = R.string.elapsed_time_label),
            value = uiStateValue.formattedTime(elapsedTimeState)
        )
        IndicatorRow(
            iconResId = R.drawable.ic_distance,
            description = stringResource(id = R.string.distance_label),
            label = stringResource(id = R.string.distance_label),
            value = uiStateValue.formattedDistance
        )
    }
}

@Composable
fun IndicatorRow(
    iconResId: Int,
    description: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = description,
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
        )
    }
}

private fun startTracking(presenter: MapPresenter) {
    presenter.startTracking()
}

private fun stopTracking(
    presenter: MapPresenter,
    addTrackActions: AddTrackActions
) {
    presenter.stopTracking(addTrackActions)
}

@Composable
fun rememberLauncherForActivityResultRegistry(): ActivityResultRegistry {
    val context = LocalContext.current as ComponentActivity
    return remember { context.activityResultRegistry }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

data class Ui(
    val formattedSteps: String,
    val distance: Int,
    val steps: Int,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {
    fun formattedTime(elapsedTime: Long): String {
        val minutes = elapsedTime / 60
        val seconds = elapsedTime % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    companion object {

        val EMPTY = Ui(
            formattedSteps = "0 passi",
            distance = 0,
            formattedDistance = "0 metri",
            currentLocation = null,
            userPath = emptyList(),
            steps = 0,
        )
    }
}
*/