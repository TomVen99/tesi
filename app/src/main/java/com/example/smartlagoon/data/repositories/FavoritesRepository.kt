package com.example.smartlagoon.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartlagoon.data.database.Favourite
import com.example.smartlagoon.data.database.FavouritesDAO
import com.example.smartlagoon.data.database.Track


data class FavoritesRepository(
    private val favoritesDAO: FavouritesDAO,
){
    suspend fun upsert(favourite: Favourite) = favoritesDAO.upsertFavourite(favourite)

    suspend fun delete(favourite: Favourite) = favoritesDAO.deteleFavourite(favourite)

    suspend fun getFavouritesByUser(userId: Int) = favoritesDAO.getFavouritesByUser(userId)

}
