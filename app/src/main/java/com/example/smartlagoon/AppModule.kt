package com.example.smartlagoon

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.smartlagoon.data.database.SmartlagoonDatabase
import com.example.smartlagoon.data.repositories.ChallengeRepository
import com.example.smartlagoon.data.repositories.PhotosRepository
import com.example.smartlagoon.data.repositories.SettingsRepository
import com.example.smartlagoon.data.repositories.ThemeRepository
import com.example.smartlagoon.data.repositories.UserChallengeRepository
import com.example.smartlagoon.data.repositories.UsersRepository
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import com.example.smartlagoon.ui.screens.home.HomeScreenViewModel
import com.example.smartlagoon.ui.screens.login.LoginViewModel
import com.example.smartlagoon.ui.screens.profile.ProfileViewModel
import com.example.smartlagoon.ui.screens.quiz.QuizViewModel
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.theme.ThemeViewModel
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.UserChallengeViewModel
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            SmartlagoonDatabase::class.java,
            "smartlagoon"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { ChallengeRepository(get<SmartlagoonDatabase>().challengesDAO()) }

    single { UserChallengeRepository(get<SmartlagoonDatabase>().userChallengeDAO()) }

    single { SettingsRepository(get()) }

    single { ThemeRepository(get()) }

    single { UsersRepository(get<SmartlagoonDatabase>().usersDAO()) }

    single {
        PhotosRepository(
            get<SmartlagoonDatabase>().photosDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { HomeScreenViewModel() }

    viewModel { ThemeViewModel(get()) }

    viewModel { UsersViewModel(get()) }

    viewModel { UsersDbViewModel() }

    viewModel { PhotosDbViewModel() }

    viewModel { QuizViewModel() }

    viewModel { ChallengesDbViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { UserChallengeViewModel(get()) }

    viewModel { SigninViewModel() }

    viewModel { ProfileViewModel() }

}
