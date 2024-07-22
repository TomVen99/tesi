package com.example.outdoorromagna.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, Track::class, Favourite::class], version = 15)
@TypeConverters(LatLngListConverter::class)
abstract class SmartlagoonDatabase : RoomDatabase() {
    abstract fun usersDAO(): UsersDAO

    abstract fun tracksDAO(): TracksDAO

    abstract fun favoritesDAO(): FavouritesDAO

}