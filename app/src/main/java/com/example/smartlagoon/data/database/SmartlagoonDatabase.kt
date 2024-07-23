package com.example.smartlagoon.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.outdoorromagna.data.database.FavouritesDAO
import com.example.outdoorromagna.data.database.LatLngListConverter
import com.example.outdoorromagna.data.database.TracksDAO
import com.example.outdoorromagna.data.database.UsersDAO

@Database(entities = [User::class, Track::class, Favourite::class], version = 1)
@TypeConverters(LatLngListConverter::class)
abstract class SmartlagoonDatabase : RoomDatabase() {
    abstract fun usersDAO(): UsersDAO

    abstract fun tracksDAO(): TracksDAO

    abstract fun favoritesDAO(): FavouritesDAO

}