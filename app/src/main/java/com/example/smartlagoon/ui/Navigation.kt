package com.example.smartlagoon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartlagoon.ui.screens.about.AboutScreen
import com.example.smartlagoon.ui.screens.addtrack.AddTrackScreen
import com.example.smartlagoon.ui.screens.addtrack.AddTrackViewModel
import com.example.smartlagoon.ui.screens.addtrackdetails.AddTrackDetailsScreen
import com.example.smartlagoon.ui.screens.addtrackdetails.AddTrackDetailsViewModel
import com.example.smartlagoon.ui.screens.home.HomeScreen
import com.example.smartlagoon.ui.screens.home.HomeScreenViewModel
import com.example.smartlagoon.ui.screens.login.Login
import com.example.smartlagoon.ui.screens.login.LoginViewModel
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.screens.profile.ProfileScreen
import com.example.smartlagoon.ui.screens.challenge.ChallengeScreen
import com.example.smartlagoon.ui.screens.ranking.RankingScreen
import com.example.smartlagoon.ui.screens.settings.SettingsScreen
import com.example.smartlagoon.ui.screens.settings.SettingsViewModel
import com.example.smartlagoon.ui.screens.signin.SigninScreen
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.screens.trackdetails.TrackDetails
import com.example.smartlagoon.ui.screens.tracking.TrackingScreen
import com.example.smartlagoon.ui.screens.tracking.TrackingViewModel
import com.example.smartlagoon.ui.screens.tracks.TracksScreen
import com.example.smartlagoon.ui.screens.tracks.TracksViewModel
import com.example.smartlagoon.ui.viewmodel.FavouritesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.androidx.compose.koinViewModel

sealed class SmartlagoonRoute(
    val route: String,
    val title: String,
    var currentRoute: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : SmartlagoonRoute("login", "Smartlagoon - Login", "login")

    data object Signin : SmartlagoonRoute("signin", "Smartlagoon - Signin", "")

    data object Challenge : SmartlagoonRoute("challange", "Channalnge", "")

    data object Ranking : SmartlagoonRoute("ranking", "Ranking", "")

    data object Photo : SmartlagoonRoute("photo", "Photo", "")

    data object Recycle : SmartlagoonRoute("recycle", "Recycle", "")

    data object About : SmartlagoonRoute("about", "About", "")

    data object Home : SmartlagoonRoute(
        "home/{userUsername}",//{latitude}/{longitude}",
        "homePage",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            /*navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }*/
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("home/$userUsername")
            return currentRoute
        }

        fun buildWithoutPosition (userUsername: String) : String {
            setMyCurrentRoute("home/$userUsername/0/0")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object TrackDetails : SmartlagoonRoute(
        "trackdetails/{userUsername}/{trackId}",
        "trackDetails",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("trackId") { type = NavType.IntType }
        )
    ) {
        fun buildRoute(userUsername: String, trackId: Int): String{
            setMyCurrentRoute("trackdetails/$userUsername/$trackId")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object Profile : SmartlagoonRoute(
        "profile/{userUsername}",
        "Profile",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("profile/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object Tracks : SmartlagoonRoute(
        "tracks/{userUsername}/{specificTrack}",
        "tracks",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("specificTrack") { type = NavType.BoolType },
        )
    ) {
        fun buildRoute(userUsername: String, specificTrack: Boolean) : String {
            setMyCurrentRoute("tracks/$userUsername/$specificTrack")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object Tracking : SmartlagoonRoute(
        "tracking/{userUsername}",
        "tracking",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
        )
    ) {
        fun buildRoute(userUsername: String) : String {
            setMyCurrentRoute("tracking/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }


    }

    data object Settings : SmartlagoonRoute(
        "settings/{userUsername}",
        "Settings",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("settings/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object AddTrack : SmartlagoonRoute(
        "addtrack/{userUsername}",
        "AddTrack",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("addtrack/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object AddTrackDetails : SmartlagoonRoute(
        "addtrackdetails/{userUsername}",
        "AddTrack",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("addtrackdetails/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    companion object {
        val routes = setOf(Login, Signin, Home, Ranking, Photo, About, Recycle, Profile, Settings, AddTrack, Tracking, AddTrackDetails)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SmartlagoonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val usersVm = koinViewModel<UsersViewModel>()
    val usersState by usersVm.state.collectAsStateWithLifecycle()
    var userDefault by remember{ mutableStateOf("null") }
    val tracksDbVm = koinViewModel<TracksDbViewModel>()
    val photosDbVm = koinViewModel<PhotosDbViewModel>()
    val favouritesDbVm= koinViewModel<FavouritesDbViewModel>()
    val tracksDbState by tracksDbVm.state.collectAsStateWithLifecycle()
    val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
    val groupedTracksState by tracksDbVm.groupedTracksState.collectAsStateWithLifecycle()
    val addTrackVm = koinViewModel<AddTrackViewModel>()
    val addTrackState by addTrackVm.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
    var startDestination = ""
    startDestination = if (sharedPreferences.getBoolean("isUserLogged", false)) {
        val username = sharedPreferences.getString("username", "")
        if(username != null && username != "") {
            SmartlagoonRoute.Home.buildRoute(username)
        } else {
            SmartlagoonRoute.Login.route
        }
    } else {
        SmartlagoonRoute.Login.route
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    )
    {
        with(SmartlagoonRoute.Login) {
            Log.d("LoginRoute", "navgraph Login")
            composable(route) {
                usersVm.resetValues()
                val loginVm = koinViewModel<LoginViewModel>()
                val state by loginVm.state.collectAsStateWithLifecycle()
                Login(
                    state,
                    actions = loginVm.actions,
                    onSubmit = {usersVm.login(state.username, state.password)},
                    navController,
                    usersVm,
                    sharedPreferences
                )
            }
        }
        with(SmartlagoonRoute.Signin) {
            composable(route) {
                usersVm.resetValues()
                val signinVm = koinViewModel<SigninViewModel>()
                val signinState by signinVm.state.collectAsStateWithLifecycle()
                SigninScreen(
                    state = signinState,
                    actions = signinVm.actions,
                    onSubmit = { usersVm.addUser(it) },
                    navController = navController,
                    usersVm)
            }
        }
        with(SmartlagoonRoute.Home) {
            composable(route, arguments) {backStackEntry ->
                Log.d("LOG", "sono qui")
                val homeScreenVm = koinViewModel<HomeScreenViewModel>()
                val state by homeScreenVm.state.collectAsStateWithLifecycle()
                var userName =  backStackEntry.arguments?.getString("userUsername") ?: userDefault
                userName = if (userName == "null") userDefault else userName
                userDefault = userName
                //controllo per non bloccarsi
                val handler = Handler(Looper.getMainLooper())
                val runnable = Runnable {
                    if(usersState.users.isEmpty()) {
                        val edit = sharedPreferences.edit()
                        edit.putBoolean("isUserLogged", false)
                        edit.putString("username", "")
                        edit.apply()
                        navController.navigate(SmartlagoonRoute.Login.route)
                    }
                }
                handler.postDelayed(runnable, 5000L)
                if(sharedPreferences.getString("username", "") == "") {
                    navController.navigate(SmartlagoonRoute.Login.route)
                } else {
                    if (usersState.users.isNotEmpty()) {
                        handler.removeCallbacks(runnable)
                        val user = requireNotNull(usersState.users.find {
                            it.username == sharedPreferences.getString("username", "")
                        })
                        Log.d("tag", "user.username " + user.username)
                        HomeScreen(
                            navController,
                            state,
                            homeScreenVm.actions,
                            user,
                            tracksDbVm,
                            tracksDbState,
                            groupedTracksState,
                            sharedPreferences
                        )
                    }
                }
            }
        }
        with(SmartlagoonRoute.Profile) {
            composable(route, arguments) {
                if(usersState.users.isNotEmpty()) {
                    //val userTrackNumber by tracksDbVm.userTracksNumber.observeAsState(0)
                    val userPoints by usersVm.userPoints.observeAsState(0)
                    Log.d("TAGGG", usersState.users.toString())
                    sharedPreferences.getString("username", null)?.let { Log.d("TAGGG", it) }
                    /*val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })*/
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", null)
                    })
                    //tracksDbVm.getUserTracksNumber(user.id)
                    usersVm.getUserPoints(user.username)
                    ProfileScreen(
                        navController = navController,
                        user = user,
                        usersViewModel = usersVm,
                        userPoints = userPoints
                    )
                }
            }
        }
        with(SmartlagoonRoute.Ranking) {
            composable(route, arguments) {
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", null)
                    })
                    val usersViewModel = koinViewModel<UsersViewModel>()
                    val usersRankingState by usersViewModel.rankingState.collectAsStateWithLifecycle()
                    usersViewModel.getUserPoints(user.username)
                    RankingScreen(
                        navController = navController,
                        user = user,
                        state = usersRankingState,
                    )
                }
            }
        }
        with(SmartlagoonRoute.Challenge) {
            composable(route, arguments
            ) { backStackEntry ->
                val tracksVm = koinViewModel<TracksViewModel>()
                val state by tracksVm.state.collectAsStateWithLifecycle()
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", null)
                    })
                    Log.d("no", backStackEntry.arguments?.getBoolean("specificTrack").toString())
                    ChallengeScreen(
                        navController = navController,
                        user = user,
                        state = state,
                        actions = tracksVm.actions,
                        tracksDbVm = tracksDbVm,
                        tracksDbState = tracksDbState,
                    )
                }
            }
        }
        with(SmartlagoonRoute.Photo) {
            composable(route, arguments) {
                LaunchedEffect(Unit) {
                    val currentTime = System.currentTimeMillis()
                    val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                    photosDbVm.deleteOldPhoto(cutoff)
                }
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", null)
                    })
                    PhotoScreen(
                        user = user,
                        photosDbVm = photosDbVm,
                        photosDbState = photosDbState
                    )
                }
            }
        }
        with(SmartlagoonRoute.About) {
            composable(route, arguments) {
                    AboutScreen(
                        navController = navController,
                    )
            }
        }
        with(SmartlagoonRoute.Tracks) {
            composable(route, arguments) {backStackEntry ->
                val tracksVm = koinViewModel<TracksViewModel>()
                val state by tracksVm.state.collectAsStateWithLifecycle()
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    Log.d("no", backStackEntry.arguments?.getBoolean("specificTrack").toString())
                    val isSpecificTrack = backStackEntry.arguments?.getBoolean("specificTrack") ?: false
                    Log.d("non devo passa", isSpecificTrack.toString())
                    TracksScreen(
                        navController = navController,
                        user = user,
                        state = state,
                        actions = tracksVm.actions,
                        tracksDbVm = tracksDbVm,
                        tracksDbState = tracksDbState,
                        //showFilter = !isSpecificTrack,
                        favouritesDbVm = favouritesDbVm,
                        isSpecificTrack = isSpecificTrack
                        )
                }
            }
        }

        with(SmartlagoonRoute.Settings) {
            composable(route) {backStackEntry ->
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    val settingsVm = koinViewModel<SettingsViewModel>()
                    SettingsScreen(
                        settingsVm = settingsVm,
                        navController = navController,
                        user = user,
                        tracksDbState = tracksDbState,
                    )
                }
            }
        }

        with(SmartlagoonRoute.AddTrack) {
            composable(route) {backStackEntry ->
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    AddTrackScreen(
                        navController = navController,
                        user = user,
                        tracksDbState = tracksDbState
                    )
                }
            }
        }

        with(SmartlagoonRoute.Tracking) {
            composable(route) {backStackEntry ->
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    val trackingVm = koinViewModel<TrackingViewModel>()
                    val trackingState by trackingVm.state.collectAsStateWithLifecycle()
                    TrackingScreen(
                        navController = navController,
                        trackingState = trackingState,
                        user = user,
                        trackingActions = trackingVm.actions,
                        tracksDbVm = tracksDbVm,
                        addTrackActions = addTrackVm.actions
                    )
                }
            }
        }
        with(SmartlagoonRoute.AddTrackDetails) {
            composable(route) {backStackEntry ->
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    val addTrackDetailsVm = koinViewModel<AddTrackDetailsViewModel>()
                    val addTrackDetailsState by addTrackDetailsVm.state.collectAsStateWithLifecycle()
                    Log.d("viewModel", addTrackState.trackPositions.toString())
                    AddTrackDetailsScreen(
                        navController = navController,
                        addTrackDetailsState = addTrackDetailsState,
                        addTrackDetailsActions = addTrackDetailsVm.actions,
                        addTrackState = addTrackState,
                        tracksDbVm = tracksDbVm,
                        user = user,
                    )
                }
            }
        }

        with(SmartlagoonRoute.TrackDetails) {
            composable(route, arguments) { backStackEntry ->
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == backStackEntry.arguments?.getString("userUsername")
                    })
                    val track = requireNotNull(tracksDbState.tracks.find {
                        it.id == backStackEntry.arguments?.getInt("trackId")
                    })
                    TrackDetails(
                        navController = navController,
                        user = user,
                        track = track,
                        tracksDbState = tracksDbState
                    )
                }
            }
        }
    }
}

