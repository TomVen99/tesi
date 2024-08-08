package com.example.smartlagoon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.database.Photo
import com.example.smartlagoon.data.repositories.PhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PhotosDbState(val photos: List<Photo>)

class PhotosDbViewModel(
    private val repository: PhotosRepository
) : ViewModel() {
    val state = repository.photos.map { PhotosDbState(photos = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PhotosDbState(emptyList())
    )

    private val _userPhotosNumber = MutableLiveData<Int>()
    val userPhotosNumber: LiveData<Int> = _userPhotosNumber

    fun addPhoto(photo: Photo) = viewModelScope.launch {
        repository.upsertPhoto(photo)
    }

    fun deleteOldPhoto(cutoff: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteOldPhoto(cutoff)
    }

    fun getAllPhotos() = viewModelScope.launch {
        repository.getAllPhotos()
    }

    fun getUserPhotos(user: String) = viewModelScope.launch {
        repository.getUserPhotos(user)
    }

    fun getUserPhotoNumber(username: String) = viewModelScope.launch {
        val userPhotosNum = repository.getUserPhotoNumber(username)
        _userPhotosNumber.value = userPhotosNum
    }

}