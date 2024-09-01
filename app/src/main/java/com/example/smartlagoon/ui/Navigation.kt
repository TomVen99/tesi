package com.example.smartlagoon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import com.example.smartlagoon.ui.screens.home.HomeScreen
import com.example.smartlagoon.ui.screens.login.Login
import com.example.smartlagoon.ui.screens.photo.PhotoScreen
import com.example.smartlagoon.ui.screens.profile.ProfileScreen
import com.example.smartlagoon.ui.screens.challenge.ChallengeScreen
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

    data object Profile : SmartlagoonRoute("profile/{username}","Profile"){
        fun createRoute(username: String): String {
            return "profile/$username"
        }
    }

    companion object {
        val routes = setOf(Login, Signin, Home, Ranking, Photo, About, Quiz, Profile,)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SmartlagoonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: SmartlagoonRoute? = null
) {

    var usersDbVm = koinViewModel<UsersDbViewModel>()
    val photosDbVm = koinViewModel<PhotosDbViewModel>()

    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
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
                    navController,
                    sharedPreferences
                )
            }
        }
        with(SmartlagoonRoute.Profile) {
            composable(route) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                var showModifyButton = false
                if (username != "{username}") {
                    LaunchedEffect(username) {
                        usersDbVm.fetchUserProfileByUsername(username!!)
                    }
                }else {
                    showModifyButton = true
                    LaunchedEffect(username) {
                        usersDbVm.fetchUserProfile()
                    }
                }
                ProfileScreen(
                    navController = navController,
                    usersDbVm = usersDbVm,
                    showModifyButton = showModifyButton
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
                val challengeDbVm = koinViewModel<ChallengesDbViewModel>()
                /*val userUncompleteChallenge by challengeDbVm.allChallenges.observeAsState(
                    emptyList()
                )*/

                challengeDbVm.loadChallengesFromJson(ctx)
                challengeDbVm.getAllChallenges()
                usersDbVm.auth.currentUser?.uid?.let {
                    ChallengeScreen(
                        navController = navController,
                        //challengeList = userUncompleteChallenge,
                        challengesDbVm = challengeDbVm,
                        userId = it
                    )
                }
            }
        }
        with(SmartlagoonRoute.Photo) {
            composable(route) {
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
                    navController = navController,
                    comeFromTakePhoto = false,
                    usersDbVm = usersDbVm
                )
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
    }
}

