package com.example.smartlagoon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.database.Favourite
import com.example.smartlagoon.data.database.Track
import com.example.smartlagoon.data.repositories.FavoritesRepository
import com.example.smartlagoon.data.repositories.TracksRepository
import kotlinx.coroutines.launch

class FavouritesDbViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _specificFavouritesList = MutableLiveData<List<Int>?>()
    val specificFavouritesList: LiveData<List<Int>?> = _specificFavouritesList
    fun upsert(favourite: Favourite) =
        viewModelScope.launch {
            repository.upsert(favourite)
        }

    fun delete(favourite: Favourite) =
        viewModelScope.launch {
        repository.delete(favourite)
    }

    fun getFavouritesByUser(userId: Int) =
        viewModelScope.launch {
            val favourites = repository.getFavouritesByUser(userId)
            _specificFavouritesList.value = favourites
        }

    fun upsertOrDeleteFavourite(trackId: Int, userId: Int) =
        viewModelScope.launch {
            if (_specificFavouritesList.value?.contains(trackId) == true) {
                repository.delete(
                    Favourite(trackId, userId)
                )
            } else {
                repository.upsert(
                    Favourite(trackId, userId)
                )
            }
            val favourites = repository.getFavouritesByUser(userId)
            _specificFavouritesList.value = favourites
        }
}