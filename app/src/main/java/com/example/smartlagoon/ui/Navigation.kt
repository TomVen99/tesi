package com.example.smartlagoon.ui

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.smartlagoon.ui.screens.home.HomeScreen
import com.example.smartlagoon.ui.screens.login.Login
import com.example.smartlagoon.ui.screens.login.LoginViewModel
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.screens.profile.ProfileScreen
import com.example.smartlagoon.ui.screens.challenge.ChallengeScreen
import com.example.smartlagoon.ui.screens.ranking.RankingScreen
import com.example.smartlagoon.ui.screens.signin.SigninScreen
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import org.koin.androidx.compose.koinViewModel

sealed class SmartlagoonRoute(
    val route: String,
    val title: String,
    var currentRoute: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : SmartlagoonRoute("login", "Smartlagoon - Login", "login")

    data object Signin : SmartlagoonRoute("signin", "Smartlagoon - Signin", "")

    data object Challenge : SmartlagoonRoute("challenge", "Challenge", "")

    data object Ranking : SmartlagoonRoute("ranking", "Ranking", "")

    data object Photo : SmartlagoonRoute("photo", "Photo", "")

    data object Recycle : SmartlagoonRoute("recycle", "Recycle", "")

    data object About : SmartlagoonRoute("about", "About", "")

    data object Home : SmartlagoonRoute("home", "Home", "")

    /*data object Home : SmartlagoonRoute(
        "home",
        "homePage",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("home/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }*/

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

    companion object {
        val routes = setOf(Login, Signin, Home, Ranking, Photo, About, Recycle, Profile,)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SmartlagoonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: SmartlagoonRoute? = null
) {

    val usersVm = koinViewModel<UsersViewModel>()
    var usersDbVm = koinViewModel<UsersDbViewModel>()
    //val usersState by usersVm.state.collectAsStateWithLifecycle()
    var userDefault by remember{ mutableStateOf("null") }
    val photosDbVm = koinViewModel<PhotosDbViewModel>()
    //val photosDbState by photosDbVm.state.collectAsStateWithLifecycle()
    /*val challengeDbVm = koinViewModel<ChallengesDbViewModel>()
    val challengeDbState = challengeDbVm.state.collectAsStateWithLifecycle()
    val userUncompleteChallenge by challengeDbVm.userUncompleteChallenges.observeAsState(
        emptyList()
    )
    val userChallengeVm = koinViewModel<UserChallengeViewModel>()
    if (challengeDbState.value.challenges.isEmpty()) {
        challengeDbVm.createChallangeTest()
        //userChallengeVm.insertTest()
        Log.d("Challenge", "challange di test caricato ")
    }*/

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
    var start = ""
    Log.d("Navigation", startDestination.toString())
    start = if (sharedPreferences.getBoolean("isUserLogged", false)) {
        Log.d("Navigation", "1")
        val username = sharedPreferences.getString("username", "")
        Log.d("Navigation", "2")
        if(username != null && username != "") {
            Log.d("Navigation", "3")
            if(startDestination != null) {
                Log.d("Navigation", "4")
                SmartlagoonRoute.Challenge.route
            } else {
                SmartlagoonRoute.Home.route//buildRoute(username)
            }
        } else {
            SmartlagoonRoute.Login.route
        }
    } else {
        SmartlagoonRoute.Login.route
    }
    NavHost(
        navController = navController,
        startDestination = start,
        modifier = modifier
    )
    {
        with(SmartlagoonRoute.Login) {
            Log.d("LoginRoute", "navgraph Login")
            composable(route) {
                //usersVm.resetValues()
                usersDbVm = koinViewModel<UsersDbViewModel>()
                val loginVm = koinViewModel<LoginViewModel>()
                val state by loginVm.state.collectAsStateWithLifecycle()
                //LoginScreen()
                Login(
                    navController,
                    usersDbVm,
                    sharedPreferences
                )
            }
        }
        with(SmartlagoonRoute.Signin) {
            composable(route) {
                //usersVm.resetValues()
                val signinVm = koinViewModel<SigninViewModel>()
                val signinState by signinVm.state.collectAsStateWithLifecycle()
                SigninScreen(
                    state = signinState,
                    actions = signinVm.actions,
                    //onSubmit = { usersVm.addUser(it) },
                    navController = navController,
                    usersDbVm
                )
            }
        }
        with(SmartlagoonRoute.Home) {
            composable(route, arguments) {_ ->
                Log.d("LOG", "home route")
                HomeScreen(
                    navController,
                    sharedPreferences
                )
            }
        }
        with(SmartlagoonRoute.Profile) {
            composable(route, arguments) {
                LaunchedEffect(Unit) {
                    usersDbVm.fetchUserProfile()
                }
                ProfileScreen(
                    navController = navController,
                    usersDbVm = usersDbVm,
                    //userOld = user,
                    //usersViewModel = usersVm,
                    //userPoints = userPoints
                )
            }
        }
        with(SmartlagoonRoute.Ranking) {
            composable(route, arguments) {
                val usersViewModel = koinViewModel<UsersViewModel>()
                //val usersRankingState by usersViewModel.rankingState.collectAsStateWithLifecycle()
                RankingScreen(
                    navController = navController,
                    //state = usersRankingState,
                )
            }
        }
        with(SmartlagoonRoute.Challenge) {
            composable(route, arguments
            ) { _ ->
                val challengeDbVm = koinViewModel<ChallengesDbViewModel>()
                val userUncompleteChallenge by challengeDbVm.userUncompleteChallenges.observeAsState(
                    emptyList()
                )
                Log.d("sharePreferences", sharedPreferences.getBoolean("isUserLogged", false).toString())
                /*sharedPreferences.getString("username", null)
                    ?.let { challengeDbVm.getUncompletedChallengesForUser(it) }*/
                challengeDbVm.getUnconpletedChallengeByUser()
                /*Log.d("Challenge",userUncompleteChallenge.toString())
                Log.d("Challenge2",challengeDbVm.userUncompleteChallenges.value.toString())*/
                ChallengeScreen(
                    navController = navController,
                    challengeList = userUncompleteChallenge,
                    challengesDbVm = challengeDbVm,
                )
            }
        }
        with(SmartlagoonRoute.Photo) {
            composable(route, arguments) {
                LaunchedEffect(Unit) {
                    val currentTime = System.currentTimeMillis()
                    val cutoff = currentTime - 24 * 60 * 60 * 1000 // 24 ore in millisecondi
                    photosDbVm.deleteOldPhoto(cutoff)
                }
                LaunchedEffect(Unit) {
                    val userId = photosDbVm.currentUser?.uid
                    if (userId != null) {
                        photosDbVm.fetchPhotosByUser(userId)
                    }
                }
                PhotoScreen(
                    photosDbVm = photosDbVm,
                    //photosDbState = photosDbState,
                    navController = navController,
                    comeFromTakePhoto = false,
                    usersDbVm = usersDbVm
                )
            }
        }
        with(SmartlagoonRoute.About) {
            composable(route, arguments) {
                    AboutScreen(
                        navController = navController,
                    )
            }
        }
    }
}

