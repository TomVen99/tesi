package com.example.smartlagoon.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class,  Photo::class, Challenge::class, UserChallenge::class], version = 7)
@TypeConverters(LatLngListConverter::class)
abstract class SmartlagoonDatabase : RoomDatabase() {
    abstract fun usersDAO(): UsersDAO

    abstract fun photosDAO(): PhotoDAO

    abstract fun challengesDAO(): ChallengesDAO

}