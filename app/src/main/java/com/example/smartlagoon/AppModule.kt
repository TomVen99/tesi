package com.example.smartlagoon

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.smartlagoon.data.database.SmartlagoonDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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

    /*single { OSMDataSource(get()) }

    single { LocationService(get()) }

    single { SettingsRepository(get()) }

    single { ThemeRepository(get()) }

    single { FavoritesRepository(get<OutdoorRomagnaDatabase>().favoritesDAO()) }

    single { UsersRepository(get<OutdoorRomagnaDatabase>().usersDAO()) }

    single {
        TracksRepository(
            get<OutdoorRomagnaDatabase>().tracksDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { HomeScreenViewModel() }

    viewModel { TracksViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { UsersViewModel(get()) }

    viewModel { TracksDbViewModel(get()) }

    viewModel { FavouritesDbViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { SigninViewModel() }

    viewModel { ProfileViewModel() }

    viewModel { AddTrackViewModel() }

    viewModel { AddTrackDetailsViewModel() }

    viewModel { TrackingViewModel() }*/

}
