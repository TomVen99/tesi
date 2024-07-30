package com.example.smartlagoon

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.smartlagoon.data.database.SmartlagoonDatabase
import com.example.smartlagoon.data.remote.OSMDataSource
import com.example.smartlagoon.data.repositories.FavoritesRepository
import com.example.smartlagoon.data.repositories.PhotosRepository
import com.example.smartlagoon.data.repositories.SettingsRepository
import com.example.smartlagoon.data.repositories.ThemeRepository
import com.example.smartlagoon.data.repositories.TracksRepository
import com.example.smartlagoon.data.repositories.UsersRepository
import com.example.smartlagoon.ui.viewmodel.UsersViewModel
import com.example.smartlagoon.ui.screens.addtrack.AddTrackViewModel
import com.example.smartlagoon.ui.screens.addtrackdetails.AddTrackDetailsViewModel
import com.example.smartlagoon.ui.screens.home.HomeScreenViewModel
import com.example.smartlagoon.ui.screens.login.LoginViewModel
import com.example.smartlagoon.ui.screens.photo.PhotoViewModel
import com.example.smartlagoon.ui.screens.profile.ProfileViewModel
import com.example.smartlagoon.ui.screens.settings.SettingsViewModel
import com.example.smartlagoon.ui.screens.signin.SigninViewModel
import com.example.smartlagoon.ui.screens.tracking.TrackingViewModel
import com.example.smartlagoon.ui.screens.tracks.TracksViewModel
import com.example.smartlagoon.ui.theme.ThemeViewModel
import com.example.smartlagoon.ui.viewmodel.FavouritesDbViewModel
import com.example.smartlagoon.ui.viewmodel.PhotosDbViewModel
import com.example.smartlagoon.ui.viewmodel.TracksDbViewModel
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

    single { OSMDataSource(get()) }

    //single { LocationService(get()) }

    single { SettingsRepository(get()) }

    single { ThemeRepository(get()) }

    single { FavoritesRepository(get<SmartlagoonDatabase>().favoritesDAO()) }

    single { UsersRepository(get<SmartlagoonDatabase>().usersDAO()) }

    single {
        PhotosRepository(
            get<SmartlagoonDatabase>().photosDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    single {
        TracksRepository(
            get<SmartlagoonDatabase>().tracksDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { HomeScreenViewModel() }

    viewModel { TracksViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { UsersViewModel(get()) }

    viewModel { TracksDbViewModel(get()) }

    viewModel { PhotosDbViewModel(get()) }

    viewModel { FavouritesDbViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { PhotoViewModel() }

    viewModel { SigninViewModel() }

    viewModel { ProfileViewModel() }

    viewModel { AddTrackViewModel() }

    viewModel { AddTrackDetailsViewModel() }

    viewModel { TrackingViewModel() }

}
