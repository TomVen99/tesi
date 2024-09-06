package com.example.smartlagoon

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartlagoon.data.repository.UserRepository
import com.example.smartlagoon.ui.screens.login.LoginViewModel
import com.example.smartlagoon.ui.screens.quiz.QuizViewModel
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.viewmodel.ChallengesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
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
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single {UserRepository()}

    viewModel { UsersDbViewModel(get()) }

    viewModel { PhotosDbViewModel(get()) }

    viewModel { QuizViewModel() }

    viewModel { ChallengesDbViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { SigninViewModel() }

}
