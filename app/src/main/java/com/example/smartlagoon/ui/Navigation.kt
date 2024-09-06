package com.example.smartlagoon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartlagoon.ui.screens.about.AboutScreen
import com.example.smartlagoon.ui.screens.camera.CameraScreen
import com.example.smartlagoon.ui.screens.home.HomeScreen
import com.example.smartlagoon.ui.screens.login.Login
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.screens.profile.ProfileScreen
import com.example.smartlagoon.ui.screens.challenge.ChallengeScreen
import com.example.smartlagoon.ui.screens.loading.LoadingScreen
import com.example.smartlagoon.ui.screens.play.PlayScreen
import com.example.smartlagoon.ui.screens.quiz.QuizScreen
import com.example.smartlagoon.ui.screens.quiz.QuizViewModel
import com.example.smartlagoon.ui.screens.ranking.RankingScreen
import com.example.smartlagoon.ui.screens.signin.SigninScreen
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import org.koin.androidx.compose.koinViewModel

sealed class SmartlagoonRoute(
    val route: String,
    val title: String,
) {
    data object Login : SmartlagoonRoute("login", "Login")

    data object Signin : SmartlagoonRoute("signin", "Signin")

    data object Challenge : SmartlagoonRoute("challenge", "Challenge")

    data object Ranking : SmartlagoonRoute("ranking", "Ranking")

    data object Photo : SmartlagoonRoute("photo", "Photo")

    data object Quiz : SmartlagoonRoute("quiz", "Quiz")

    data object About : SmartlagoonRoute("about", "About")

    data object Home : SmartlagoonRoute("home", "Home")

    data object Camera : SmartlagoonRoute("camera", "Camera")

    data object Play : SmartlagoonRoute("play", "Play")

    data object Profile : SmartlagoonRoute("profile/{username}","Profile"){
        fun createRoute(username: String): String {
            return "profile/$username"
        }
    }

    companion object {
        val routes = setOf(Login, Signin, Home, Ranking, Photo, About, Quiz, Profile, Camera, Play)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SmartlagoonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: SmartlagoonRoute? = null
) {

    var usersDbVm = koinViewModel<UsersDbViewModel>()
    val photosDbVm = koinViewModel<PhotosDbViewModel>()
    val challengesDbVm = koinViewModel<ChallengesDbViewModel>()

    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
    val currentUser by usersDbVm.currentUser.observeAsState()
    val start = SmartlagoonRoute.Login.route

    Log.d("Navigation", startDestination.toString())
    NavHost(
        navController = navController,
        startDestination = start,
        modifier = modifier
    )
    {
        with(SmartlagoonRoute.Login) {
            Log.d("LoginRoute", "navgraph Login")
            composable(route) {
                usersDbVm = koinViewModel<UsersDbViewModel>()
                Login(
                    navController,
                    usersDbVm,
                    sharedPreferences
                )
            }
        }
        with(SmartlagoonRoute.Signin) {
            composable(route) {
                val signinVm = koinViewModel<SigninViewModel>()
                val signinState by signinVm.state.collectAsStateWithLifecycle()
                SigninScreen(
                    state = signinState,
                    actions = signinVm.actions,
                    navController = navController,
                    usersDbVm
                )
            }
        }
        with(SmartlagoonRoute.Home) {
            composable(route) {_ ->
                HomeScreen(
                    navController = navController,
                    photosDbVm = photosDbVm
                )
            }
        }
        with(SmartlagoonRoute.Profile) {
            composable(route) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                //var showModifyButton = false
                if (username != null) {
                    Log.d("navigation", username)
                    usersDbVm.userLiveData.value?.username?.let { Log.d("navigation2", it) }
                }
                when (username) {
                    "{username}" -> {
                        usersDbVm.setShowModifyButton(true)
                        usersDbVm.userLiveData.value?.username?.let {
                            usersDbVm.fetchUserProfileByUsername(
                                it
                            )
                        }
                    }
                    usersDbVm.userLiveData.value?.username -> {
                        usersDbVm.setShowModifyButton(true)
                        LaunchedEffect(username) {
                            usersDbVm.fetchUserProfile()
                        }
                        usersDbVm.setNullTmpUserLiveData()
                    }
                    else -> {
                        usersDbVm.setShowModifyButton(false)
                        LaunchedEffect(username) {
                            usersDbVm.fetchUserProfileByUsername(username!!)
                        }
                    }
                }
                ProfileScreen(
                    navController = navController,
                    usersDbVm = usersDbVm,
                )
            }
        }
        with(SmartlagoonRoute.Ranking) {
            composable(route) {
                RankingScreen(
                    navController = navController,
                    usersDbVm = usersDbVm,
                )
            }
        }
        with(SmartlagoonRoute.Challenge) {
            composable(route) { _ ->
                challengesDbVm.loadChallengesFromJson(ctx)
                challengesDbVm.getAllChallenges()
                usersDbVm.currentUser.value?.uid?.let {
                    ChallengeScreen(
                        navController = navController,
                        challengesDbVm = challengesDbVm,
                        userId = it
                    )
                }
            }
        }
        with(SmartlagoonRoute.Photo) {
            composable(route) {
                val isLoading by photosDbVm.isLoading.observeAsState(true)
                LaunchedEffect(Unit) {
                    photosDbVm.fetchAllPhotos()
                }
                if (isLoading) {
                    LoadingScreen()
                } else {
                    PhotoScreen(
                        photosDbVm = photosDbVm,
                        navController = navController,
                        challengesDbVm = challengesDbVm,
                        usersDbVm = usersDbVm
                    )
                }
            }
        }
        with(SmartlagoonRoute.About) {
            composable(route) {
                    AboutScreen(
                        navController = navController,
                    )
            }
        }
        with(SmartlagoonRoute.Quiz) {
            composable(route) {
                val quizVm = koinViewModel<QuizViewModel>()
                quizVm.loadQuestionsFromJson(ctx)
                quizVm.getUnconpletedQuestionsByUser()
                usersDbVm.fetchUserProfile()
                QuizScreen(
                    quizVm = quizVm,
                    usersDbVm = usersDbVm,
                    navController = navController,
                )
            }
        }
        with(SmartlagoonRoute.Camera) {
            composable(route) {

                CameraScreen(
                    photosDbVm = photosDbVm,
                    challengesDbVm = challengesDbVm,
                    usersDbVm = usersDbVm,
                    navController = navController,
                )
            }
        }
        with(SmartlagoonRoute.Play) {
            composable(route) {
                val isLoading by usersDbVm.isLoading.observeAsState(true)
                LaunchedEffect(Unit) {
                    usersDbVm.fetchUserProfile()
                }
                if (isLoading) {
                    LoadingScreen()
                } else {
                    if (currentUser != null) {
                        PlayScreen(
                            navController = navController,
                            usersDbVm = usersDbVm
                        )
                        currentUser?.email?.let { email ->
                            Log.d("navigation", email)
                        }
                    } else {
                        navController.navigate(SmartlagoonRoute.Login.route)
                    }
                }
            }
        }
    }
}

